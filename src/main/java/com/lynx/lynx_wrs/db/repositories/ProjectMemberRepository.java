package com.lynx.lynx_wrs.db.repositories;

import com.lynx.lynx_wrs.db.entities.ProjectMembers;
import com.lynx.lynx_wrs.db.entities.ProjectRole;
import com.lynx.lynx_wrs.db.entities.Projects;
import com.lynx.lynx_wrs.db.entities.Users;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMembers, Long> {
    boolean existsByProjectIdAndUserId(Long projectId, Long userId);

    @Query("""
        select pm.user
        from ProjectMembers pm
        where pm.project.id = :projectId
    """)
    List<Users> findUsersByProjectId(@Param("projectId") Long projectId);


    // ProjectMemberRepository.java
    @Query("""
              select u 
              from Users u 
              where u.id not in (
                select pm.user.id from ProjectMembers pm where pm.project.id = :projectId
              )
            """)
    List<Users> findUsersNotInProject(@Param("projectId") Long projectId);


    boolean existsByUserIdAndRole(Long userId, ProjectRole role);

    ProjectMembers findByUserAndProject(Users user, Projects project);
}
