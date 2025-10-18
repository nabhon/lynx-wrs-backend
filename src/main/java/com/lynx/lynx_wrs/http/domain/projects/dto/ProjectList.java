package com.lynx.lynx_wrs.http.domain.projects.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ProjectList {
    private Long projectId;
    private String projectKey;
    private String projectName;
}
