package com.lynx.lynx_wrs.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.config.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
