package com.lynx.lynx_wrs.http.domain.tasks.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TaskDto {

    @Data
    public static class ProjectTasksRequest {
        private Long projectId;
    }
}
