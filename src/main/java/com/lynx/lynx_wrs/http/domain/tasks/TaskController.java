package com.lynx.lynx_wrs.http.domain.tasks;


import com.lynx.lynx_wrs.http.domain.tasks.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/get")
    public ResponseEntity<?> getProjectTasks(@RequestParam("projectId") Long projectId) {
        return ResponseEntity.ok(taskService.getProjectTasks(projectId));
    }
}
