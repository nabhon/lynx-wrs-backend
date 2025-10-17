package com.lynx.lynx_wrs.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lynx.lynx_wrs.db.entities.Tasks;

public interface TaskRepository extends JpaRepository<Tasks, Long> {
}
