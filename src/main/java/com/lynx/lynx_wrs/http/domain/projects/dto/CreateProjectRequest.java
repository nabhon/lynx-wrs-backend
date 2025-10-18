package com.lynx.lynx_wrs.http.domain.projects.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectRequest {
    private String projectName;
    private String projectKey;
    private String projectDescription;
}
