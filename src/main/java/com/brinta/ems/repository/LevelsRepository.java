package com.brinta.ems.repository;

import com.brinta.ems.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelsRepository extends JpaRepository<Level, Long> {
    boolean existsByName(String name);
}

