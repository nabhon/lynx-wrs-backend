package com.lynx.lynx_wrs.http.domain.projects;

import com.lynx.lynx_wrs.db.entities.Projects;
import com.lynx.lynx_wrs.http.domain.projects.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/list")
    public ResponseEntity<?> getProjectList() {
        return ResponseEntity.ok(projectService.getProjectLists());
    }
}
