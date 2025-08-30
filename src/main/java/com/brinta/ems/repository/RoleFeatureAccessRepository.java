package com.brinta.ems.repository;

import com.brinta.ems.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoleFeatureAccessRepository extends JpaRepository<RoleFeatureAccess, Long> {
    boolean existsByRoleAndFeatureAndAccessLevelAndBranch(Role role, Feature feature, AccessLevel accessLevel, Branch branch);
    List<RoleFeatureAccess> findAllByRole(Role role);
    List<RoleFeatureAccess> findAllByRoleAndFeature(Role role, Feature feature);
}
