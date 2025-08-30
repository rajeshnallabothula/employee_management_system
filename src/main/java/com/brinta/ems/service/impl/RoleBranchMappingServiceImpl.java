package com.brinta.ems.service.impl;

import com.brinta.ems.entity.Branch;
import com.brinta.ems.entity.Role;
import com.brinta.ems.entity.RoleBranchMapping;
import com.brinta.ems.entity.Tenant;
import com.brinta.ems.repository.BranchRepository;
import com.brinta.ems.repository.RoleBranchMappingRepository;
import com.brinta.ems.repository.RoleRepository;
import com.brinta.ems.repository.TenantRepository;
import com.brinta.ems.request.registerRequest.RoleBranchMapRequest;
import com.brinta.ems.response.BranchFeatureAccessResponse;
import com.brinta.ems.response.BranchResponse;
import com.brinta.ems.response.RoleBranchFeatureAccessResponse;
import com.brinta.ems.response.RoleBranchMappingResponse;
import com.brinta.ems.service.RoleBranchMappingService;
import com.brinta.ems.tenant.TenantContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleBranchMappingServiceImpl implements RoleBranchMappingService {

    private final RoleRepository roleRepository;
    private final BranchRepository branchRepository;
    private final TenantRepository tenantRepository;
    private final RoleBranchMappingRepository mappingRepository;

    @Override
    @Transactional
    public RoleBranchFeatureAccessResponse mapRoleToBranch(RoleBranchMapRequest request) {
        Long tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant ID is missing from context");
        }

        if (mappingRepository.existsByRoleIdAndBranchId(request.getRoleId(), request.getBranchId())) {
            throw new IllegalArgumentException("Mapping already exists");
        }

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        RoleBranchMapping mapping = new RoleBranchMapping();
        mapping.setRole(role);
        role.getRoleBranchMappings().add(mapping);
        mapping.setBranch(branch);
        mapping.setTenant(tenant);
        mapping.setTenantId(tenantId);
        mapping.setCreatedAt(LocalDateTime.now());
        mappingRepository.save(mapping);

        // Prepare feature-accessLevel mapping
        List<BranchFeatureAccessResponse> featureAccessList = branch.getFeatureAccesses().stream()
                .collect(Collectors.groupingBy(
                        bfa -> bfa.getFeature().getName(),
                        Collectors.mapping(
                                bfa -> bfa.getAccessLevel().getLevelName(),
                                Collectors.toList()
                        )
                ))
                .entrySet().stream()
                .map(e -> new BranchFeatureAccessResponse(e.getKey(), e.getValue()))
                .toList();

        return new RoleBranchFeatureAccessResponse(
                role.getId(),
                role.getRoleName(),
                branch.getId(),
                branch.getName(),
                tenantId,
                featureAccessList
        );
    }
    @Transactional
    public List<BranchResponse> getBranchesForRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        return role.getRoleBranchMappings().stream()
                .map(mapping -> buildBranchResponse(mapping.getBranch()))
                .collect(Collectors.toList());
    }


    private BranchResponse buildBranchResponse(Branch branch) {
        // Safe inside @Transactional
        return new BranchResponse(
                branch.getId(),
                branch.getName(),
                branch.getLocation(),
                branch.getTenant().getId(),
                null


        );
    }
}

