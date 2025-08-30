package com.brinta.ems.repository;

import com.brinta.ems.entity.AccessLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessLevelRepository extends JpaRepository<AccessLevel, Long> {
    List<AccessLevel> findByTenantId(Long tenantId);
}
