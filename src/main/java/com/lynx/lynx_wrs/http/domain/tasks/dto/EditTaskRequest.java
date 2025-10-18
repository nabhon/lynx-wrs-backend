package com.lynx.lynx_wrs.http.domain.tasks.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditTaskRequest {
    private Long taskId;
    private Long projectId;
    private Long cycleId;
    private Long sprintId;
    private String taskKey;
    private String taskName;
    private String description;
    private String type;
    private String status;
    private String priority;
    private Integer estimatePoints;
    private Integer actualPoints;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long assigneeId;
    private Long auditorId;
}
