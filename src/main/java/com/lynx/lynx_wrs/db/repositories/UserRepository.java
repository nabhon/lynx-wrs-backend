package com.lynx.lynx_wrs.db.repositories;

import com.lynx.lynx_wrs.db.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);

    Optional<Users> findByDisplayName(String displayName);


    @Query("SELECT MONTH(u.createdAt) AS month, COUNT(u.id) AS count " +
            "FROM Users u " +
            "WHERE YEAR(u.createdAt) = :year" +
            " GROUP BY MONTH(u.createdAt)" +
            "ORDER BY MONTH(u.createdAt) ASC")
    List<Object[]> countUsersByMonth(@Param("year") int year);

}
