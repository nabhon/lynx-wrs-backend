package com.lynx.lynx_wrs.db.repositories;

import com.lynx.lynx_wrs.db.entities.Cycles;
import com.lynx.lynx_wrs.db.entities.Projects;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CycleRepository extends JpaRepository<Cycles, Long> {
    Cycles findByCycleCountAndProject(Integer cycleCount, Projects project);
}
