package com.brinta.ems.service.impl;

import com.brinta.ems.entity.*;
import com.brinta.ems.repository.*;
import com.brinta.ems.request.registerRequest.BranchFeatureAccessRequest;
import com.brinta.ems.request.registerRequest.BranchRequest;
import com.brinta.ems.response.BranchFeatureAccessResponse;
import com.brinta.ems.response.BranchResponse;
import com.brinta.ems.service.BranchService;
import com.brinta.ems.tenant.TenantContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final TenantRepository tenantRepository;
    private final FeatureRepository featureRepository;
    private final AccessLevelRepository accessLevelRepository;
    private final BranchFeatureAccessRepository branchFeatureAccessRepository;

    @Override
    @Transactional
    public BranchResponse createBranch(BranchRequest request) {
        // Fetch tenant from the repository
        Tenant tenant = tenantRepository.findById(request.getTenantId())
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        // Create new branch entity
        Branch branch = new Branch();
        branch.setName(request.getName());
        branch.setLocation(request.getLocation());
        branch.setTenant(tenant); // Set tenant to branch

        // Fetch the feature by ID
        Feature feature = featureRepository.findById(request.getFeatureId())
                .orElseThrow(() -> new RuntimeException("Feature not found"));

        // Create BranchFeatureAccess entries for each access level
        List<BranchFeatureAccess> accessList = new ArrayList<>();
        for (Long accessLevelId : request.getAccessLevelIds()) {
            AccessLevel accessLevel = accessLevelRepository.findById(accessLevelId)
                    .orElseThrow(() -> new RuntimeException("Access Level not found"));

            BranchFeatureAccess bfa = new BranchFeatureAccess();
            bfa.setBranch(branch);
            bfa.setFeature(feature);
            bfa.setAccessLevel(accessLevel);
            accessList.add(bfa);
        }

        // Set the feature access list to the branch
        branch.setFeatureAccesses(accessList);

        // Save branch and its associated feature access levels
        Branch savedBranch = branchRepository.save(branch);
        branchFeatureAccessRepository.saveAll(accessList);

        // Prepare and return the BranchResponse
        BranchResponse response = new BranchResponse();
        response.setBranchId(savedBranch.getId());
        response.setName(savedBranch.getName());
        response.setLocation(savedBranch.getLocation());
        response.setTenantId(tenant.getId());

        Map<String, List<String>> featureLevelMap = new HashMap<>();
        for (BranchFeatureAccess bfa : accessList) {
            String featureName = bfa.getFeature().getName();
            featureLevelMap.computeIfAbsent(featureName, k -> new ArrayList<>())
                    .add(bfa.getAccessLevel().getLevelName());
        }

        List<BranchFeatureAccessResponse> featureResponses = featureLevelMap.entrySet().stream()
                .map(e -> {
                    BranchFeatureAccessResponse r = new BranchFeatureAccessResponse();
                    r.setFeatureName(e.getKey());
                    r.setAccessLevels(e.getValue());
                    return r;
                }).toList();

        response.setFeatures(featureResponses);
        return response;
    }

    @Override
    @Transactional
    public List<BranchResponse> getAllBranches(Long tenantId) {
        return branchRepository.findAll().stream()
                .filter(b -> Objects.equals(b.getTenantId(), tenantId))
                .map(this::buildResponse)
                .toList();
    }

    private BranchResponse buildResponse(Branch branch) {
        Map<String, List<String>> featureMap = new HashMap<>();
        for (BranchFeatureAccess bfa : branch.getFeatureAccesses()) {
            featureMap
                    .computeIfAbsent(bfa.getFeature().getName(), k -> new ArrayList<>())
                    .add(bfa.getAccessLevel().getLevelName());
        }

        List<BranchFeatureAccessResponse> features = featureMap.entrySet().stream()
                .map(e -> new BranchFeatureAccessResponse(e.getKey(), e.getValue()))
                .toList();

        return new BranchResponse(
                branch.getId(),
                branch.getName(),
                branch.getLocation(),
                branch.getTenantId(),
                features
        );
    }


    @Override
    public BranchResponse updateBranch(Long id, BranchRequest request) {
        Branch branch = branchRepository.findById(id).orElseThrow();
        branch.setName(request.getName());
        branch.setLocation(request.getLocation());

        branchFeatureAccessRepository.deleteAll(branch.getFeatureAccesses());
        List<BranchFeatureAccess> newAccessList = new ArrayList<>();

        for (BranchFeatureAccessRequest f : request.getFeatureAccessMap()) {
            Feature feature = featureRepository.findById(f.getFeatureId()).orElseThrow();
            for (Long accessLevelId : f.getAccessLevelIds()) {
                AccessLevel level = accessLevelRepository.findById(accessLevelId).orElseThrow();
                BranchFeatureAccess bfa = new BranchFeatureAccess();
                bfa.setBranch(branch);
                bfa.setFeature(feature);
                bfa.setAccessLevel(level);
                newAccessList.add(bfa);
            }
        }

        branch.setFeatureAccesses(newAccessList);
        branchFeatureAccessRepository.saveAll(newAccessList);
        return buildResponse(branch);
    }

    @Override
    public void deleteBranch(Long id) {
        branchRepository.deleteById(id);
    }
}
