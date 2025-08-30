// UserGroupServiceImpl.java
package com.brinta.ems.service.impl;

import com.brinta.ems.dto.FeatureAccessDto;
import com.brinta.ems.entity.*;
import com.brinta.ems.repository.*;
import com.brinta.ems.request.registerRequest.UserGroupRequest;
import com.brinta.ems.response.BranchFeatureAccessResponse;
import com.brinta.ems.response.UserGroupResponse;
import com.brinta.ems.service.UserGroupService;
import com.brinta.ems.tenant.TenantContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserGroupServiceImpl implements UserGroupService {

    private final UserGroupRepository userGroupRepository;
    private final FeatureRepository featureRepository;
    private final AccessLevelRepository accessLevelRepository;
    private final UserGroupFeatureAccessRepository userGroupFeatureAccessRepository;

    @Override
    @Transactional
    public void createUserGroup(UserGroupRequest request) {
        Long tenantId = TenantContext.getTenantId();

        UserGroup userGroup = new UserGroup();
        userGroup.setName(request.getName());
        userGroup.setTenantId(tenantId);

        userGroupRepository.save(userGroup);

        List<UserGroupFeatureAccess> accesses = new ArrayList<>();
        for (FeatureAccessDto dto : request.getFeatureAccessList()) {
            Feature feature = featureRepository.findById(dto.getFeatureId())
                    .orElseThrow(() -> new RuntimeException("Feature not found"));
            for (Long levelId : dto.getAccessLevelIds()) {
                AccessLevel level = accessLevelRepository.findById(levelId)
                        .orElseThrow(() -> new RuntimeException("Access level not found"));

                UserGroupFeatureAccess access = new UserGroupFeatureAccess();
                access.setUserGroup(userGroup);
                access.setFeature(feature);
                access.setAccessLevel(level);
                accesses.add(access);
            }
        }

        userGroupFeatureAccessRepository.saveAll(accesses);
    }

    @Override
    public UserGroupResponse getUserGroupById(Long id) {
        UserGroup group = userGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User group not found"));

        List<UserGroupFeatureAccess> accessList = userGroupFeatureAccessRepository.findByUserGroupId(id);

        Map<String, List<String>> grouped = accessList.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getFeature().getName(),
                        Collectors.mapping(a -> a.getAccessLevel().getLevelName(), Collectors.toList())
                ));

        List<BranchFeatureAccessResponse> responses = grouped.entrySet().stream()
                .map(e -> new BranchFeatureAccessResponse(e.getKey(), e.getValue()))
                .toList();

        return new UserGroupResponse(group.getId(), group.getName(), responses);
    }

    @Override
    public void updateUserGroup(Long id, UserGroupRequest request) {
        UserGroup group = userGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User group not found"));

        group.setName(request.getName());
        userGroupRepository.save(group);

        userGroupFeatureAccessRepository.deleteAllByUserGroupId(id);

        List<UserGroupFeatureAccess> newAccesses = new ArrayList<>();
        for (FeatureAccessDto dto : request.getFeatureAccessList()) {
            Feature feature = featureRepository.findById(dto.getFeatureId())
                    .orElseThrow(() -> new RuntimeException("Feature not found"));
            for (Long levelId : dto.getAccessLevelIds()) {
                AccessLevel level = accessLevelRepository.findById(levelId)
                        .orElseThrow(() -> new RuntimeException("Access level not found"));

                UserGroupFeatureAccess access = new UserGroupFeatureAccess();
                access.setUserGroup(group);
                access.setFeature(feature);
                access.setAccessLevel(level);
                newAccesses.add(access);
            }
        }

        userGroupFeatureAccessRepository.saveAll(newAccesses);
    }

    @Override
    public void deleteUserGroup(Long id) {
        userGroupFeatureAccessRepository.deleteAllByUserGroupId(id);
        userGroupRepository.deleteById(id);
    }

    @Override
    public List<UserGroupResponse> getAllUserGroups() {
        return userGroupRepository.findAll().stream()
                .map(group -> getUserGroupById(group.getId()))
                .collect(Collectors.toList());
    }
}
