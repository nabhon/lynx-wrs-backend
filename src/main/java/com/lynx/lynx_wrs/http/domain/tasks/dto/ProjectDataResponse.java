package com.lynx.lynx_wrs.http.domain.tasks.dto;

import com.lynx.lynx_wrs.db.entities.Tasks;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDataResponse {
    private List<TaskDto> task;
    private Long projectId;
    private String projectName;
    private String projectKey;
}
