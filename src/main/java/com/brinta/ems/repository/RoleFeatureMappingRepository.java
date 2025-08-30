package com.brinta.ems.repository;

import com.brinta.ems.entity.RoleFeatureMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleFeatureMappingRepository extends JpaRepository<RoleFeatureMapping, Long> {

    List<RoleFeatureMapping> findByRoleId(Long roleId);

    boolean existsByRoleIdAndFeatureId(Long roleId, Long featureId);

    List<RoleFeatureMapping> findByFeatureId(Long featureId);

    Optional<RoleFeatureMapping> findById(Long id);

}

