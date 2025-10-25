package com.lynx.lynx_wrs.http.domain.projects;

import com.lynx.lynx_wrs.db.entities.Projects;
import com.lynx.lynx_wrs.http.domain.projects.dto.CreateProjectRequest;
import com.lynx.lynx_wrs.http.domain.projects.service.ProjectService;
import com.lynx.lynx_wrs.http.domain.tasks.dto.ProjectDataResponse;
import com.lynx.lynx_wrs.http.domain.tasks.service.TaskService;
import com.lynx.lynx_wrs.http.domain.users.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;


    @GetMapping("/list")
    public ResponseEntity<?> getProjectList() {
        return ResponseEntity.ok(projectService.getProjectLists());
    }

    @GetMapping
    public ResponseEntity<?> getProjectData(@RequestParam("projectName") String projectName) {
        ProjectDataResponse projectData = taskService.getProjectTasks(projectName);
        return ResponseEntity.ok(Map.of("message","success","items", projectData.getTask(),"projectName",projectData.getProjectName(),"projectKey",projectData.getProjectKey(),"projectId",projectData.getProjectId()));
    }

    @GetMapping("/members")
    public ResponseEntity<?> getUsersInProject(@RequestParam(name = "projectId") Long projectId) {
        List<UserDto> users = projectService.getAllUsersInProjects(projectId);
        return ResponseEntity.ok(Map.of("message","success","users", users));
    }

    @PostMapping("/members/add")
    public ResponseEntity<?> addProjectMember(
            @RequestParam(name = "projectId") Long projectId,
            @RequestParam(name = "userId") Long userId
    ) {
        return ResponseEntity.ok(projectService.addProjectMember(projectId, userId));
    }

    @GetMapping("/candidates")
    public ResponseEntity<?> getProjectCandidates(@RequestParam("projectId") Long projectId) {
        List<UserDto> users = projectService.getCandidatesForProject(projectId);
        return ResponseEntity.ok(Map.of("message","success","items", users));
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody CreateProjectRequest req) {
        projectService.createProject(req);
        return ResponseEntity.ok(Map.of("message","success"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProject(@RequestParam(name = "id") Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(Map.of("message","success"));
    }

    @DeleteMapping("/members/remove")
    public ResponseEntity<?> removeProjectMember(@RequestParam(name = "projectId") Long projectId,
                                                 @RequestParam(name = "userId") Long userId) {
        return ResponseEntity.ok(projectService.removeProjectMember(projectId, userId));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProject());
    }
}
