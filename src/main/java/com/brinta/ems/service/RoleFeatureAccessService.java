package com.brinta.ems.service;

import com.brinta.ems.request.registerRequest.RoleFeatureAccessRequest;

import java.util.List;
import java.util.Map;

public interface RoleFeatureAccessService {
    void addFeatureAccessToRole(Long roleId, RoleFeatureAccessRequest request);
    void updateFeatureAccessForRole(Long roleId, RoleFeatureAccessRequest request);
    List<Map<String, Object>> getAllAccessByRole(Long roleId);
    void deleteFeatureAccessForRole(Long roleId, RoleFeatureAccessRequest request);
    public void cloneFromUserGroupToRole(Long userGroupId, Long roleId, Long branchId);
}