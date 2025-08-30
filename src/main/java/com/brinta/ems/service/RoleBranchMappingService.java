package com.brinta.ems.service;

import com.brinta.ems.entity.RoleBranchMapping;
import com.brinta.ems.request.registerRequest.RoleBranchMapRequest;
import com.brinta.ems.response.BranchResponse;
import com.brinta.ems.response.RoleBranchFeatureAccessResponse;
import com.brinta.ems.response.RoleBranchMappingResponse;

import java.util.List;

public interface RoleBranchMappingService {

    RoleBranchFeatureAccessResponse mapRoleToBranch(RoleBranchMapRequest request);


    List<BranchResponse> getBranchesForRole(Long roleId);
}

