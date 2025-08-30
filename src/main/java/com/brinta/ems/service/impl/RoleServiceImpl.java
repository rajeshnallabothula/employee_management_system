package com.brinta.ems.service.impl;

import com.brinta.ems.dto.RoleDto;
import com.brinta.ems.entity.Role;
import com.brinta.ems.entity.Tenant;
import com.brinta.ems.exception.exceptionHandler.EntityNotFoundException;
import com.brinta.ems.repository.RoleRepository;
import com.brinta.ems.repository.TenantRepository;
import com.brinta.ems.response.GenericResponse;
import com.brinta.ems.service.RoleFeatureAccessService;
import com.brinta.ems.service.RoleService;
import com.brinta.ems.tenant.TenantContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final TenantRepository tenantRepository;
    private final RoleFeatureAccessService roleFeatureAccessService;


    @Override
    @Transactional
    public Role createRole(RoleDto dto, BindingResult result) {
        Long tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant ID is missing from context");
        }

        if (roleRepository.existsByRoleName(dto.getRoleName())) {
            throw new IllegalArgumentException("Role with name " + dto.getRoleName() + " already exists.");
        }

        if (result.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            result.getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append(" "));
            throw new IllegalArgumentException("Validation failed: " + errorMessages.toString());
        }

        boolean exists = roleRepository.existsByRoleNameAndTenantId(dto.getRoleName(), tenantId);
        if (exists) {
            throw new IllegalArgumentException("Role with name '" + dto.getRoleName() + "' already exists for this tenant");
        }

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Tenant not found for ID: " + tenantId));

        Role role = new Role();
        BeanUtils.copyProperties(dto, role);
        role.setTenant(tenant);

        Role savedRole = roleRepository.save(role);

        // 🧠 Optional: Clone features/access from UserGroup if requested
        if (dto.getUserGroupId() != null) {
            roleFeatureAccessService.cloneFromUserGroupToRole(dto.getUserGroupId(), savedRole.getId(), null);
            savedRole.setClonedFromUserGroupId(dto.getUserGroupId());
            roleRepository.save(savedRole); // Save updated clone info
        }

        return savedRole;
    }

    @Override
    public void cloneFromUserGroupToRole(Long userGroupId, Long roleId, Long branchId) {

    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateRole(Long id, RoleDto dto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with ID: " + id));

        BeanUtils.copyProperties(dto, role);
        role.setUpdatedAt(LocalDateTime.now());

        roleRepository.save(role);
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse(true, "Role updated successfully"));
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with ID: " + id));
        roleRepository.delete(role);
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse(true, "Role deleted successfully"));
    }
}
