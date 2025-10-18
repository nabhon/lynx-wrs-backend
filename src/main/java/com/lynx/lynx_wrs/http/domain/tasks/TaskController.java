package com.lynx.lynx_wrs.http.domain.tasks;


import com.lynx.lynx_wrs.db.entities.Tasks;
import com.lynx.lynx_wrs.http.domain.tasks.dto.CreateTaskRequest;
import com.lynx.lynx_wrs.http.domain.tasks.dto.EditTaskRequest;
import com.lynx.lynx_wrs.http.domain.tasks.dto.TaskDto;
import com.lynx.lynx_wrs.http.domain.tasks.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<?> getProjectTasks(@RequestParam("projectId") Long projectId) {
        List<TaskDto> tasks = taskService.getProjectTasks(projectId);
        return ResponseEntity.ok(Map.of("message","success","items", tasks));
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody CreateTaskRequest req) {
        taskService.addTasks(req);
        return ResponseEntity.ok(Map.of("message", "success"));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTask(@RequestParam("taskId") Long taskId,@RequestParam("projectId") Long projectId) {
        taskService.deleteTasks(taskId,projectId);
        return ResponseEntity.ok(Map.of("message", "success"));
    }

    @PutMapping
    public ResponseEntity<?> editTask(@RequestBody EditTaskRequest req) {
        taskService.editTasks(req);
        return ResponseEntity.ok(Map.of("message", "success"));
    }
}
