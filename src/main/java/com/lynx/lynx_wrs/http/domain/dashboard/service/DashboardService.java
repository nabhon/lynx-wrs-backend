package com.lynx.lynx_wrs.http.domain.dashboard.service;

import com.lynx.lynx_wrs.db.entities.TaskStatus;
import com.lynx.lynx_wrs.db.repositories.ProjectsRepository;
import com.lynx.lynx_wrs.db.repositories.TaskRepository;
import com.lynx.lynx_wrs.db.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final UserRepository userRepository;
    private final ProjectsRepository projectsRepository;
    private final TaskRepository taskRepository;

    public Map<String, Object> getOverviewStats() {
        long users = userRepository.count();
        long projects = projectsRepository.count();

        long activeTasks = taskRepository.countByStatusNotIn(
                List.of(TaskStatus.DONE, TaskStatus.BLOCKED, TaskStatus.CANCELED)
        );

        long overdueTasks = taskRepository.countByDueDateBeforeAndStatusNotIn(
                LocalDate.now(),
                List.of(TaskStatus.DONE, TaskStatus.CANCELED)
        );

        return Map.of(
                "message", "success",
                "users", users,
                "projects", projects,
                "activeTasks", activeTasks,
                "overdueTasks", overdueTasks
        );
    }

    public Map<String, Object> getUserGrowth(int year) {
        List<Object[]> result = userRepository.countUsersByMonth(year);
        // Query result -> [[1, 50], [2, 80], ...]
        List<Map<String, Object>> data = result.stream()
                .map(row -> Map.of("month", row[0], "count", row[1]))
                .toList();
        return Map.of("message", "success", "items", data);
    }
}
