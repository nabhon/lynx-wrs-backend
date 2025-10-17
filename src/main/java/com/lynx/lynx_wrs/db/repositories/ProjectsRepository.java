package com.lynx.lynx_wrs.db.repositories;

import com.lynx.lynx_wrs.db.entities.Projects;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectsRepository extends JpaRepository<Projects, Long> {
}
