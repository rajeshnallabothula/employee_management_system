package com.brinta.ems.repository;

import com.brinta.ems.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

    boolean existsByCode(String code);
}
