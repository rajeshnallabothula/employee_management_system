package com.brinta.ems.service;

import com.brinta.ems.dto.RoleDto;
import com.brinta.ems.entity.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    public Optional<Role> getRoleById(Long id);

    public ResponseEntity<?> updateRole(Long id, RoleDto dto);

    public ResponseEntity<?> deleteRole(Long id);

    public List<Role> getAllRoles();

    public Role createRole(RoleDto dto, BindingResult result);
    void cloneFromUserGroupToRole(Long userGroupId, Long roleId, Long branchId);

}

