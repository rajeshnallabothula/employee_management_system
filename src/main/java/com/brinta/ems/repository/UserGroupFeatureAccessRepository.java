package com.brinta.ems.repository;

import com.brinta.ems.entity.UserGroupFeatureAccess;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGroupFeatureAccessRepository extends JpaRepository<UserGroupFeatureAccess, Long> {

    @EntityGraph(attributePaths = {"feature", "accessLevel"})
    List<UserGroupFeatureAccess> findByUserGroupId(Long userGroupId);

    void deleteAllByUserGroupId(Long id);
}
