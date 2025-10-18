package com.lynx.lynx_wrs.http.domain.tasks.dto;

import com.lynx.lynx_wrs.db.entities.TaskPriority;
import com.lynx.lynx_wrs.db.entities.TaskStatus;
import com.lynx.lynx_wrs.db.entities.TaskType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private Long id;
    private Long projectId;
    private String projectKey;
    private Long cycleId;
    private Integer cycleCount;
    private Long sprintId;
    private Integer sprintCount;
    private String key;
    private String title;
    private String description;
    private TaskType type;
    private TaskStatus status;
    private TaskPriority priorities;
    private Integer estimatePoints;
    private Integer actualPoints;
    private String startDate;
    private String dueDate;
    private String finishedAt;
    private Long assignedToId;
    private String assignedToName;
    private Long auditedById;
    private String auditedByName;
    private Long createdById;
    private String createdByName;
    private Long updatedById;
    private String createdAt;
    private String updatedAt;
}
