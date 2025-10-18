package com.lynx.lynx_wrs.db.repositories;

import com.lynx.lynx_wrs.db.entities.ProjectMembers;
import com.lynx.lynx_wrs.db.entities.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMembers, Long> {
    boolean existsByProjectIdAndUserId(Long projectId, Long userId);

    boolean existsByUserIdAndRole(Long userId, ProjectRole role);
}
