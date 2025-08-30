package com.brinta.ems.repository;

import com.brinta.ems.entity.Branch;
import com.brinta.ems.entity.RoleBranchMapping;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleBranchMappingRepository extends JpaRepository<RoleBranchMapping, Long> {
    List<RoleBranchMapping> findByRoleId(Long roleId);
    boolean existsByRoleIdAndBranchId(Long roleId, Long branchId);
    @EntityGraph(attributePaths = {"tenant", "featureAccesses"})
    Optional<Branch> findWithDetailsById(Long id);

}

