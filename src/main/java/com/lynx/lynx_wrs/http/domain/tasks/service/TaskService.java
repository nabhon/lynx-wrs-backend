package com.lynx.lynx_wrs.http.domain.tasks.service;

import com.lynx.lynx_wrs.db.entities.Projects;
import com.lynx.lynx_wrs.db.entities.Tasks;
import com.lynx.lynx_wrs.db.entities.Users;
import com.lynx.lynx_wrs.db.repositories.ProjectMemberRepository;
import com.lynx.lynx_wrs.db.repositories.ProjectsRepository;
import com.lynx.lynx_wrs.db.repositories.TaskRepository;
import com.lynx.lynx_wrs.db.repositories.UserRepository;
import com.lynx.lynx_wrs.http.domain.tasks.dto.TaskDto;
import com.lynx.lynx_wrs.http.domain.users.services.AuthService;
import com.lynx.lynx_wrs.http.exception.AppException;
import com.lynx.lynx_wrs.http.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final TaskRepository taskRepository;
    private final ProjectsRepository projectsRepository;

    public Map<String,Object> getProjectTasks(Long projectId) {
        Users requester = authService.getUserByToken();
        if (requester == null) {
            throw new AppException(ErrorCode.UNAUTHORIZED,"UNAUTHORIZED");
        }
        projectsRepository.findById(projectId).orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));
        boolean exists = projectMemberRepository.existsByProjectIdAndUserId(projectId,requester.getId());
        if (!exists) {
            throw new AppException(ErrorCode.UNAUTHORIZED,"NOT PROJECT MEMBER");
        }
        List<TaskDto> tasks = taskRepository.findByProjectId(projectId);
        return Map.of("message","success","items",tasks);
    }
}
