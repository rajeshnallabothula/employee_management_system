package com.brinta.ems.service;

import com.brinta.ems.request.registerRequest.BranchRequest;
import com.brinta.ems.response.BranchResponse;

import java.util.List;

public interface BranchService {

    BranchResponse createBranch(BranchRequest request);
    List<BranchResponse> getAllBranches(Long tenantId);
    BranchResponse updateBranch(Long id, BranchRequest request);
    void deleteBranch(Long id);
}
