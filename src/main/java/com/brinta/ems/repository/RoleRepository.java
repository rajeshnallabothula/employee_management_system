package com.brinta.ems.repository;


import com.brinta.ems.entity.Role;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

    boolean existsByRoleNameAndTenantId(String roleName, Long tenantId);

    @EntityGraph(attributePaths = "roleBranchMappings")
    Optional<Role> findWithMappingsById(Long id);

    boolean existsByRoleName(String roleName);
}

