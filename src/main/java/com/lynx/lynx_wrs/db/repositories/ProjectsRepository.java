package com.lynx.lynx_wrs.db.repositories;

import com.lynx.lynx_wrs.db.entities.Projects;
import com.lynx.lynx_wrs.db.entities.Users;
import com.lynx.lynx_wrs.http.domain.projects.dto.ProjectList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectsRepository extends JpaRepository<Projects, Long> {

    @Query("""
    SELECT new com.lynx.lynx_wrs.http.domain.projects.dto.ProjectList(
        p.id,
        p.key,
        p.name
    )
    FROM Projects p
    JOIN p.members pm
    JOIN pm.user u
    WHERE u.id = :userId
""")
    List<ProjectList> findProjectsListByUserId(@Param("userId") Long userId);

    Projects findByName(String name);
}
