package com.lynx.lynx_wrs.db.repositories;

import com.lynx.lynx_wrs.db.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    <Optional>Users findByEmail(String email);
}
