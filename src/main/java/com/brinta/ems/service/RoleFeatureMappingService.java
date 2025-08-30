package com.brinta.ems.service;

import com.brinta.ems.dto.RoleFeatureMappingDTO;
import com.brinta.ems.entity.RoleFeatureMapping;
import com.brinta.ems.response.RoleFeatureMapResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RoleFeatureMappingService {

    public RoleFeatureMapResponse assignFeatureToRole(RoleFeatureMappingDTO dto);

    RoleFeatureMapping getRoleFeatureMappingById(Long mappingId);

    public List<RoleFeatureMapping> getFeaturesByRoleId(Long roleId);

    // Get all RoleFeatureMappings with validation
    List<RoleFeatureMapping> getAllRoleFeatureMappings();

    public void removeFeatureFromRole(Long mappingId);

}

