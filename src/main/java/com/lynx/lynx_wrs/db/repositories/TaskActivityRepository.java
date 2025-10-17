package com.lynx.lynx_wrs.db.repositories;

import com.lynx.lynx_wrs.db.entities.TaskActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskActivityRepository extends JpaRepository<TaskActivity, Long> {
}
