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
import com.lynx.lynx_wrs.http.domain.users.services.AuthService;
import com.lynx.lynx_wrs.http.exception.AppException;
import com.lynx.lynx_wrs.http.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final AuthService authService;
    private final ProjectsRepository projectsRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public Map<String,Object> getProjectLists() {
        Users requester = authService.getUserByToken();
        if (requester == null) {
            throw new AppException(ErrorCode.UNAUTHORIZED,"UNAUTHORIZED");
        }
        List<ProjectList> projectLists = projectsRepository.findProjectsListByUserId(requester.getId());
        return Map.of("message","success","items",projectLists);
    }

    public Map<String,Object> getAllProject() {
        Users requester = authService.getUserByToken();
        if (requester == null) {
            throw new AppException(ErrorCode.UNAUTHORIZED,"UNAUTHORIZED");
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

    public void createProject(CreateProjectRequest req) {
        Users requester = authService.getUserByToken();
        if (requester == null) {
            throw new AppException(ErrorCode.UNAUTHORIZED,"UNAUTHORIZED");
        }
        if (requester.getRole() == Role.USER) {
            throw new AppException(ErrorCode.UNAUTHORIZED,"UNAUTHORIZED");
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
}
