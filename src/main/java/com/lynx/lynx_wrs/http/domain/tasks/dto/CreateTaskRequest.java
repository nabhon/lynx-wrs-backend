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
public class CreateTaskRequest {
    private Long projectId;
    private Integer cycleCount;
    private Integer sprintCount;
    private String taskKey;
    private String taskName;
    private String description;
    private String type;
    private String status;
    private String priority;
    private Integer actualPoints;
    private Integer estimatePoints;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long assigneeId;
    private Long auditorId;
}
