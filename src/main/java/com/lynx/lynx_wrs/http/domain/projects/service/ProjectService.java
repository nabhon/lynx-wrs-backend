package com.lynx.lynx_wrs.http.domain.projects.service;

import com.lynx.lynx_wrs.db.entities.Projects;
import com.lynx.lynx_wrs.db.entities.Users;
import com.lynx.lynx_wrs.db.repositories.ProjectsRepository;
import com.lynx.lynx_wrs.db.repositories.UserRepository;
import com.lynx.lynx_wrs.http.domain.projects.dto.ProjectList;
import com.lynx.lynx_wrs.http.domain.users.services.AuthService;
import com.lynx.lynx_wrs.http.exception.AppException;
import com.lynx.lynx_wrs.http.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final AuthService authService;
    private final ProjectsRepository projectsRepository;

    public Map<String,Object> getProjectLists() {
        Users requester = authService.getUserByToken();
        if (requester == null) {
            throw new AppException(ErrorCode.UNAUTHORIZED,"UNAUTHORIZED");
        }
        List<ProjectList> projectLists = projectsRepository.findProjectsListByUserId(requester.getId());
        return Map.of("message","success","items",projectLists);
    }
}
