package com.lynx.lynx_wrs.db.repositories;

import com.lynx.lynx_wrs.db.entities.Cycles;
import com.lynx.lynx_wrs.db.entities.Sprints;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SprintRepository extends JpaRepository<Sprints, Long> {
    Sprints findBySprintCountAndCycle(Integer sprintCount, Cycles cycle);
}
