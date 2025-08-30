package com.brinta.ems.service.impl;

import com.brinta.ems.entity.*;
import com.brinta.ems.repository.*;
import com.brinta.ems.request.registerRequest.RoleFeatureAccessRequest;
import com.brinta.ems.service.RoleFeatureAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleFeatureAccessServiceImpl implements RoleFeatureAccessService {

    private final RoleRepository roleRepository;
    private final FeatureRepository featureRepository;
    private final AccessLevelRepository accessLevelRepository;
    private final BranchRepository branchRepository;
    private final RoleFeatureAccessRepository roleFeatureAccessRepository;

    private final  UserGroupRepository userGroupRepository;

    private final UserGroupFeatureAccessRepository  userGroupFeatureAccessRepository;

    @Override
    public void addFeatureAccessToRole(Long roleId, RoleFeatureAccessRequest request) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Feature feature = featureRepository.findById(request.getFeatureId())
                .orElseThrow(() -> new RuntimeException("Feature not found"));

        Branch branch = null;
        if (request.getBranchId() != null) {
            branch = branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
        }

        for (Long accessLevelId : request.getAccessLevelIds()) {
            AccessLevel accessLevel = accessLevelRepository.findById(accessLevelId)
                    .orElseThrow(() -> new RuntimeException("Access Level not found"));

            boolean exists = roleFeatureAccessRepository.existsByRoleAndFeatureAndAccessLevelAndBranch(
                    role, feature, accessLevel, branch
            );

            if (!exists) {
                RoleFeatureAccess access = new RoleFeatureAccess();
                access.setRole(role);
                access.setFeature(feature);
                access.setAccessLevel(accessLevel);
                access.setBranch(branch);

                roleFeatureAccessRepository.save(access);
            }
        }
    }

    @Override
    public void updateFeatureAccessForRole(Long roleId, RoleFeatureAccessRequest request) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Feature feature = featureRepository.findById(request.getFeatureId())
                .orElseThrow(() -> new RuntimeException("Feature not found"));

        List<RoleFeatureAccess> existingAccess = roleFeatureAccessRepository
                .findAllByRoleAndFeature(role, feature);

        roleFeatureAccessRepository.deleteAll(existingAccess);

        Branch branch = null;
        if (request.getBranchId() != null) {
            branch = branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
        }

        for (Long accessLevelId : request.getAccessLevelIds()) {
            AccessLevel accessLevel = accessLevelRepository.findById(accessLevelId)
                    .orElseThrow(() -> new RuntimeException("AccessLevel not found"));

            RoleFeatureAccess newAccess = new RoleFeatureAccess();
            newAccess.setRole(role);
            newAccess.setFeature(feature);
            newAccess.setAccessLevel(accessLevel);
            newAccess.setBranch(branch);

            roleFeatureAccessRepository.save(newAccess);
        }
    }

    @Override
    public List<Map<String, Object>> getAllAccessByRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        List<RoleFeatureAccess> accessList = roleFeatureAccessRepository.findAllByRole(role);

        Map<String, List<String>> grouped = accessList.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getFeature().getName(),
                        Collectors.mapping(a -> a.getAccessLevel().getLevelName(), Collectors.toList())
                ));

        return grouped.entrySet().stream().map(entry -> {
            Map<String, Object> map = new HashMap<>();
            map.put("feature", entry.getKey());
            map.put("accessLevels", entry.getValue());
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteFeatureAccessForRole(Long roleId, RoleFeatureAccessRequest request) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Feature feature = featureRepository.findById(request.getFeatureId())
                .orElseThrow(() -> new RuntimeException("Feature not found"));

        List<RoleFeatureAccess> existing = roleFeatureAccessRepository.findAllByRoleAndFeature(role, feature);
        roleFeatureAccessRepository.deleteAll(existing);
    }

    @Override
    public void cloneFromUserGroupToRole(Long userGroupId, Long roleId, Long branchId) {
        UserGroup userGroup = userGroupRepository.findById(userGroupId)
                .orElseThrow(() -> new RuntimeException("UserGroup not found"));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        List<UserGroupFeatureAccess> groupAccessList = userGroupFeatureAccessRepository.findByUserGroupId(userGroupId);

        List<RoleFeatureAccess> roleAccesses = groupAccessList.stream().map(groupAccess -> {
            RoleFeatureAccess access = new RoleFeatureAccess();
            access.setRole(role);
            access.setFeature(groupAccess.getFeature());
            access.setAccessLevel(groupAccess.getAccessLevel());
            access.setBranch(branchId != null ? new Branch(branchId) : null); // Optional
            return access;
        }).toList();

        roleFeatureAccessRepository.saveAll(roleAccesses);
    }

}
