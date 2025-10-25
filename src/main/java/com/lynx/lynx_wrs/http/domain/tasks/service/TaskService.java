package com.lynx.lynx_wrs.http.domain.tasks.service;

import com.lynx.lynx_wrs.db.entities.Cycles;
import com.lynx.lynx_wrs.db.entities.Projects;
import com.lynx.lynx_wrs.db.entities.Role;
import com.lynx.lynx_wrs.db.entities.Sprints;
import com.lynx.lynx_wrs.db.entities.TaskPriority;
import com.lynx.lynx_wrs.db.entities.TaskStatus;
import com.lynx.lynx_wrs.db.entities.TaskType;
import com.lynx.lynx_wrs.db.entities.Tasks;
import com.lynx.lynx_wrs.db.entities.Users;
import com.lynx.lynx_wrs.db.repositories.CycleRepository;
import com.lynx.lynx_wrs.db.repositories.ProjectMemberRepository;
import com.lynx.lynx_wrs.db.repositories.ProjectsRepository;
import com.lynx.lynx_wrs.db.repositories.SprintRepository;
import com.lynx.lynx_wrs.db.repositories.TaskRepository;
import com.lynx.lynx_wrs.db.repositories.UserRepository;
import com.lynx.lynx_wrs.http.domain.tasks.dto.CreateTaskRequest;
import com.lynx.lynx_wrs.http.domain.tasks.dto.EditTaskRequest;
import com.lynx.lynx_wrs.http.domain.tasks.dto.ProjectDataResponse;
import com.lynx.lynx_wrs.http.domain.tasks.dto.TaskDto;
import com.lynx.lynx_wrs.http.domain.users.services.AuthService;
import com.lynx.lynx_wrs.http.exception.AppException;
import com.lynx.lynx_wrs.http.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final AuthService authService;
    private final ProjectMemberRepository projectMemberRepository;
    private final TaskRepository taskRepository;
    private final ProjectsRepository projectsRepository;
    private final UserRepository userRepository;
    private final CycleRepository cycleRepository;
    private final SprintRepository sprintRepository;

    public ProjectDataResponse getProjectTasks(String projectName) {
        Users requester = authService.getUserByToken();
        Projects projects = projectsRepository.findByName(projectName);
        if (projects == null) {
            throw new AppException(ErrorCode.PROJECT_NOT_FOUND);
        }
        if (!requester.getRole().equals(Role.ADMIN)) {
            checkPermission(requester, projects.getId());
        }
        List<TaskDto> task = taskRepository.findByProjectId(projects.getId());
        ProjectDataResponse projectDataResponse = ProjectDataResponse.builder()
                .projectId(projects.getId())
                .projectName(projects.getName())
                .projectKey(projects.getKey())
                .task(task)
                .build();
        return projectDataResponse;
    }

    @Transactional
    public Tasks addTasks(CreateTaskRequest req) {
        Users requester = authService.getUserByToken();
        if (!requester.getRole().equals(Role.ADMIN)) {
            checkPermission(requester, req.getProjectId());
        }
        Projects project = findProject(req.getProjectId());
        Cycles cycle = cycleRepository.findByCycleCountAndProject(req.getCycleCount(),project);
        if (req.getCycleCount() != null && req.getCycleCount() > 0) {
            if (cycle == null) {
                cycle = Cycles.builder()
                        .cycleCount(req.getCycleCount())
                        .project(project)
                        .build();
                cycleRepository.save(cycle);
            }
        }
        Sprints sprint = sprintRepository.findBySprintCountAndCycle(req.getSprintCount(), cycle);
        if (  req.getSprintCount() != null &&  req.getSprintCount() > 0 ) {
            if (sprint == null) {
                sprint = Sprints.builder()
                        .sprintCount(req.getSprintCount())
                        .cycle(cycle)
                        .build();
                sprintRepository.save(sprint);
            }
        }
        Users assignee = null;
        if (req.getAssigneeId() != null) {
            assignee = userRepository.findById(req.getAssigneeId()).orElse(null);
        }

        Users auditor = null;
        if (req.getAuditorId() != null) {
            auditor = userRepository.findById(req.getAuditorId()).orElse(null);
        }

        Tasks task = Tasks.builder()
                .project(project)
                .cycle(cycle)
                .sprint(sprint)
                .key(req.getTaskKey())
                .title(req.getTaskName())
                .description(req.getDescription())
                .type(TaskType.valueOf(req.getType()))
                .status(TaskStatus.valueOf(req.getStatus()))
                .priorities(TaskPriority.valueOf(req.getPriority()))
                .startDate(req.getStartDate())
                .dueDate(req.getEndDate())
                .assignedTo(assignee)
                .auditedBy(auditor)
                .createdBy(requester)
                .build();
        if (req.getActualPoints() != null && req.getActualPoints() >= 0) {
            task.setActualPoints(req.getActualPoints());
        }
        if (req.getEstimatePoints() != null && req.getEstimatePoints() >= 0) {
            task.setEstimatePoints(req.getEstimatePoints());
        }
        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTasks(Long taskId,Long projectId) {
        Users requester = authService.getUserByToken();
        checkPermission(requester,projectId);
        taskRepository.deleteById(taskId);
    }

    @Transactional
    public void editTasks(EditTaskRequest req) {
        Users requester = authService.getUserByToken();

        if (!requester.getRole().equals(Role.ADMIN)) {
            checkPermission(requester, req.getProjectId());
        }
        Tasks task = findTask(req.getTaskId());

        if (req.getCycleId() != null) {
            Cycles cycle = findCycle(req.getCycleId());
            task.setCycle(cycle);
        }
        if (req.getSprintId() != null) {
            Sprints sprint = findSprint(req.getSprintId());
            task.setSprint(sprint);
        }
        if (req.getAssigneeId() != null) {
            Users assignee = findUser(req.getAssigneeId());
            task.setAssignedTo(assignee);
        }
        if (req.getAuditorId() != null) {
            Users auditor = findUser(req.getAuditorId());
            task.setAuditedBy(auditor);
        }
        if (req.getTaskKey() != null) task.setKey(req.getTaskKey());
        if (req.getTaskName() != null) task.setTitle(req.getTaskName());
        if (req.getDescription() != null) task.setDescription(req.getDescription());
        if (req.getType() != null) task.setType(TaskType.valueOf(req.getType()));
        if (req.getStatus() != null) task.setStatus(TaskStatus.valueOf(req.getStatus()));
        if (req.getPriority() != null) task.setPriorities(TaskPriority.valueOf(req.getPriority()));
        if (req.getEstimatePoints() >= 0) task.setEstimatePoints(req.getEstimatePoints());
        if (req.getActualPoints() >= 0) task.setActualPoints(req.getActualPoints());
        if (req.getStartDate() != null) task.setStartDate(req.getStartDate());
        if (req.getEndDate() != null) task.setDueDate(req.getEndDate());
        task.setUpdatedBy(requester);
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);
    }


    private void checkPermission(Users requester, Long projectId) {
        if (requester == null) {
            throw new AppException(ErrorCode.UNAUTHORIZED,"UNAUTHORIZED");
        }
        boolean exists = projectMemberRepository.existsByProjectIdAndUserId(projectId,requester.getId());
        if (!exists) {
            throw new AppException(ErrorCode.UNAUTHORIZED,"NOT PROJECT MEMBER");
        }
    }

    private Projects findProject(Long id) {
        return projectsRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));
    }

    private Tasks findTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMUNITY_NOT_FOUND));
    }

    private Cycles findCycle(Long id) {
        return cycleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMUNITY_NOT_FOUND));
    }

    private Sprints findSprint(Long id) {
        return sprintRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMUNITY_NOT_FOUND));
    }

    private Users findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public Map<String, Object> getMyAssignedOpenTasks() {
        Users me = authService.getUserByToken();
        List<TaskDto> items = taskRepository.findAssignedOpenByUser(me.getId());
        return Map.of("message", "success", "items", items);
    }

    public Map<String, Object> getMyPendingReviewTasks() {
        Users me = authService.getUserByToken();
        List<TaskDto> items = taskRepository.findPendingReviewByAuditor(me.getId());
        return Map.of("message", "success", "items", items);
    }

    public TaskDto getTaskById(Long taskId) {
        Users requester = authService.getUserByToken();
        TaskDto dto = taskRepository.findDtoById(taskId);
        if (dto == null) {
            throw new AppException(ErrorCode.COMMUNITY_NOT_FOUND, "Task not found");
        }
        if (!requester.getRole().equals(Role.ADMIN)) {
            checkPermission(requester, dto.getProjectId());
        }
        return dto;
    }

}
