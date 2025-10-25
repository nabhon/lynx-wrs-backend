package com.lynx.lynx_wrs.http.domain.projects.service;

import com.lynx.lynx_wrs.db.entities.ProjectMembers;
import com.lynx.lynx_wrs.db.entities.ProjectRole;
import com.lynx.lynx_wrs.db.entities.Projects;
import com.lynx.lynx_wrs.db.entities.Role;
import com.lynx.lynx_wrs.db.entities.Users;
import com.lynx.lynx_wrs.db.repositories.ProjectMemberRepository;
import com.lynx.lynx_wrs.db.repositories.ProjectsRepository;
import com.lynx.lynx_wrs.db.repositories.UserRepository;
import com.lynx.lynx_wrs.http.domain.projects.dto.CreateProjectRequest;
import com.lynx.lynx_wrs.http.domain.projects.dto.ProjectList;
import com.lynx.lynx_wrs.http.domain.users.dto.UserDto;
import com.lynx.lynx_wrs.http.domain.users.services.AuthService;
import com.lynx.lynx_wrs.http.exception.AppException;
import com.lynx.lynx_wrs.http.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final AuthService authService;
    private final ProjectsRepository projectsRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;

    public Map<String,Object> getProjectLists() {
        Users requester = authService.getUserByToken();
        if (requester == null) {
            throw new AppException(ErrorCode.UNAUTHORIZED,"ไม่เจอ token");
        }
        List<ProjectList> projectLists = projectsRepository.findProjectsListByUserId(requester.getId());
        return Map.of("message","success","items",projectLists);
    }

    @Transactional
    public Map<String,Object> addProjectMember(Long projectId, Long userId) {
        Users requester = authService.getUserByToken();
        if (requester == null || requester.getRole().equals(Role.USER)) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "ไม่มีสิทธิ์");
        }
        Users userToAdd = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "ไม่พบผู้ใช้"));
        Projects projects = projectsRepository.findById(projectId).orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND, "Project not found"));
        boolean isOwner = projectMemberRepository.existsByProjectIdAndUserId(projectId, requester.getId()) &&
                projectMemberRepository.existsByUserIdAndRole(requester.getId(), ProjectRole.OWNER);
        if (!isOwner && !requester.getRole().equals(Role.ADMIN)) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "ไม่มีสิทธิ์");
        }
        ProjectMembers newMember = ProjectMembers.builder()
                .project(projects)
                .user(userToAdd)
                .role(ProjectRole.MEMBER)
                .build();
        projectMemberRepository.save(newMember);
        return Map.of("message", "success"," addedUser", userToAdd.getDisplayName(), "projectId", projects.getName());
    }



    public List<UserDto> getAllUsersInProjects(Long projectId) {
        Projects projects = projectsRepository.findById(projectId)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND,"Project not found"));
        List<Users> usersInProject = projectMemberRepository.findUsersByProjectId(projects.getId());
        List<UserDto> userDtos = new ArrayList<>();
        for (Users user : usersInProject) {
            UserDto userDto = UserDto.builder()
                    .id(user.getId())
                    .name(user.getDisplayName())
                    .role(user.getRole().name())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .lastLogin(user.getLastLoginAt())
                    .build();
            userDtos.add(userDto);
        }
        return userDtos;
    }

    public Map<String,Object> getAllProject() {
        Users requester = authService.getUserByToken();
        if (requester == null) {
            throw new AppException(ErrorCode.UNAUTHORIZED,"ไม่เจอ token");
        }
        List<Projects> projectAll = projectsRepository.findAll();
        List<ProjectList> projectLists = new ArrayList<>();
        for (Projects project : projectAll) {
            ProjectList projectList = ProjectList.builder()
                    .projectId(project.getId())
                    .projectKey(project.getKey())
                    .projectName(project.getName())
                    .build();
            projectLists.add(projectList);
        }
        return Map.of("message","success","items",projectLists);
    }

    @Transactional
    public void createProject(CreateProjectRequest req) {
        Users requester = authService.getUserByToken();
        if (requester == null) {
            throw new AppException(ErrorCode.UNAUTHORIZED,"ไม่เจอ token");
        }
        if (requester.getRole() == Role.USER) {
            throw new AppException(ErrorCode.UNAUTHORIZED,"Role USER ไม่สามารถสร้างโปรเจคได้");
        }
        Projects project = Projects.builder()
                .createdBy(requester)
                .name(req.getProjectName())
                .key(req.getProjectKey())
                .description(req.getProjectDescription())
                .build();
        ProjectMembers projectMembers = ProjectMembers.builder()
                .project(project)
                .user(requester)
                .role(ProjectRole.OWNER)
                .build();
        projectsRepository.save(project);
        projectMemberRepository.save(projectMembers);
    }

    @Transactional
    public void deleteProject(Long id) {
        Users requester = authService.getUserByToken();
        if (!requester.getRole().equals(Role.ADMIN)) {
            throw new AppException(ErrorCode.UNAUTHORIZED,"ไม่เจอ token");
        }
        projectsRepository.deleteById(id);
    }

    @Transactional
    public Map<String,Object> removeProjectMember(Long projectId, Long userId) {
        Users requester = authService.getUserByToken();
        if (requester == null || requester.getRole().equals(Role.USER)) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "UNAUTHORIZED");
        }
        Users userToRemove = userRepository.findById(userId).orElse(null);
        Projects projects = projectsRepository.findById(projectId).orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND, "Project not found"));
        boolean isOwner = projectMemberRepository.existsByProjectIdAndUserId(projectId, requester.getId()) &&
                projectMemberRepository.existsByUserIdAndRole(requester.getId(), ProjectRole.OWNER);
        if (!isOwner && !requester.getRole().equals(Role.ADMIN)) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "UNAUTHORIZED");
        }
        ProjectMembers memberToRemove = projectMemberRepository.findByUserAndProject(userToRemove, projects);
        if (memberToRemove == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND_IN_PROJECT, "User not found in project");
        }
        projectMemberRepository.delete(memberToRemove);
        return Map.of("message", "success"," removedUser", userToRemove.getDisplayName(), "projectId", projects.getName());
    }


    public List<UserDto> getCandidatesForProject(Long projectId) {
        Users requester = authService.getUserByToken();
        if (requester == null || requester.getRole().equals(Role.USER)) {
            throw new AppException(ErrorCode.UNAUTHORIZED,"UNAUTHORIZED");
        }

        Projects project = projectsRepository.findById(projectId)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND,"Project not found"));

        // อนุญาต OWNER หรือ ADMIN (จะรวม MODERATOR ด้วยก็ได้หากต้องการ)
        boolean isOwner = projectMemberRepository.existsByProjectIdAndUserId(projectId, requester.getId()) &&
                projectMemberRepository.existsByUserIdAndRole(requester.getId(), ProjectRole.OWNER);
        if (!isOwner && !requester.getRole().equals(Role.ADMIN)) {
            throw new AppException(ErrorCode.UNAUTHORIZED,"UNAUTHORIZED");
        }

        List<Users> notMembers = projectMemberRepository.findUsersNotInProject(project.getId());
        List<UserDto> result = new ArrayList<>();
        for (Users u : notMembers) {
            result.add(UserDto.builder()
                    .id(u.getId())
                    .name(u.getDisplayName())
                    .role(u.getRole().name())
                    .createdAt(u.getCreatedAt())
                    .updatedAt(u.getUpdatedAt())
                    .lastLogin(u.getLastLoginAt())
                    .build());
        }
        return result;
    }

}
