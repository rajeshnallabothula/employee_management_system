package com.brinta.ems.repository;

import com.brinta.ems.entity.BranchFeatureAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchFeatureAccessRepository extends JpaRepository<BranchFeatureAccess, Long> {}

