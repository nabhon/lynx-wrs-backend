package com.lynx.lynx_wrs.db.repositories;

import com.lynx.lynx_wrs.http.domain.tasks.dto.TaskDto;
import org.springframework.data.jpa.repository.JpaRepository;
import com.lynx.lynx_wrs.db.entities.Tasks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Tasks, Long> {

    @Query("""
        SELECT new com.lynx.lynx_wrs.http.domain.tasks.dto.TaskDto(
            t.id,
            p.id,
            p.key,
            c.id,
            c.cycleCount,
            s.id,
            s.sprintCount,
            t.key,
            t.title,
            t.description,
            t.type,
            t.status,
            t.priorities,
            t.estimatePoints,
            t.actualPoints,
            CAST(t.startDate AS string),
            CAST(t.dueDate AS string),
            CAST(t.finishedAt AS string),
            assigned.id,
            assigned.displayName,
            audited.id,
            audited.displayName,
            created.id,
            created.displayName,
            updated.id,
            CAST(t.createdAt AS string),
            CAST(t.updatedAt AS string)
        )
        FROM Tasks t
        JOIN t.project p
        LEFT JOIN t.cycle c
        LEFT JOIN t.sprint s
        LEFT JOIN t.assignedTo assigned
        LEFT JOIN t.auditedBy audited
        LEFT JOIN t.createdBy created
        LEFT JOIN t.updatedBy updated
        WHERE p.id = :projectId
    """)
    List<TaskDto> findByProjectId(@Param("projectId") Long projectId);

    @Query("""
    SELECT new com.lynx.lynx_wrs.http.domain.tasks.dto.TaskDto(
        t.id, p.id, p.key, c.id, c.cycleCount, s.id, s.sprintCount,
        t.key, t.title, t.description, t.type, t.status, t.priorities,
        t.estimatePoints, t.actualPoints,
        CAST(t.startDate AS string), CAST(t.dueDate AS string), CAST(t.finishedAt AS string),
        assigned.id, assigned.displayName,
        audited.id,  audited.displayName,
        created.id,  created.displayName,
        updated.id,
        CAST(t.createdAt AS string), CAST(t.updatedAt AS string)
    )
    FROM Tasks t
    JOIN t.project p
    LEFT JOIN t.cycle   c
    LEFT JOIN t.sprint  s
    LEFT JOIN t.assignedTo assigned
    LEFT JOIN t.auditedBy audited
    LEFT JOIN t.createdBy created
    LEFT JOIN t.updatedBy updated
    WHERE assigned.id = :userId
      AND t.status NOT IN (
        com.lynx.lynx_wrs.db.entities.TaskStatus.CANCELED,
        com.lynx.lynx_wrs.db.entities.TaskStatus.BLOCKED,
        com.lynx.lynx_wrs.db.entities.TaskStatus.DONE
      )
""")
    List<TaskDto> findAssignedOpenByUser(@Param("userId") Long userId);

    @Query("""
    SELECT new com.lynx.lynx_wrs.http.domain.tasks.dto.TaskDto(
        t.id, p.id, p.key, c.id, c.cycleCount, s.id, s.sprintCount,
        t.key, t.title, t.description, t.type, t.status, t.priorities,
        t.estimatePoints, t.actualPoints,
        CAST(t.startDate AS string), CAST(t.dueDate AS string), CAST(t.finishedAt AS string),
        assigned.id, assigned.displayName,
        audited.id,  audited.displayName,
        created.id,  created.displayName,
        updated.id,
        CAST(t.createdAt AS string), CAST(t.updatedAt AS string)
    )
    FROM Tasks t
    JOIN t.project p
    LEFT JOIN t.cycle   c
    LEFT JOIN t.sprint  s
    LEFT JOIN t.assignedTo assigned
    LEFT JOIN t.auditedBy audited
    LEFT JOIN t.createdBy created
    LEFT JOIN t.updatedBy updated
    WHERE audited.id = :userId
      AND t.status = com.lynx.lynx_wrs.db.entities.TaskStatus.REVIEW
""")
    List<TaskDto> findPendingReviewByAuditor(@Param("userId") Long userId);

    @Query("""
    SELECT new com.lynx.lynx_wrs.http.domain.tasks.dto.TaskDto(
        t.id,
        p.id,
        p.key,
        c.id,
        c.cycleCount,
        s.id,
        s.sprintCount,
        t.key,
        t.title,
        t.description,
        t.type,
        t.status,
        t.priorities,
        t.estimatePoints,
        t.actualPoints,
        CAST(t.startDate AS string),
        CAST(t.dueDate AS string),
        CAST(t.finishedAt AS string),
        assigned.id,
        assigned.displayName,
        audited.id,
        audited.displayName,
        created.id,
        created.displayName,
        updated.id,
        CAST(t.createdAt AS string),
        CAST(t.updatedAt AS string)
    )
    FROM Tasks t
    JOIN t.project p
    LEFT JOIN t.cycle c
    LEFT JOIN t.sprint s
    LEFT JOIN t.assignedTo assigned
    LEFT JOIN t.auditedBy audited
    LEFT JOIN t.createdBy created
    LEFT JOIN t.updatedBy updated
    WHERE t.id = :taskId
""")
    TaskDto findDtoById(@Param("taskId") Long taskId);
}
