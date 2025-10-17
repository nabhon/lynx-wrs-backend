package com.lynx.lynx_wrs.db.repositories;

import com.lynx.lynx_wrs.db.entities.ProjectMembers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMembers, Long> {
}
