package com.brinta.ems.controller;

import com.brinta.ems.request.registerRequest.BranchRequest;
import com.brinta.ems.response.BranchResponse;
import com.brinta.ems.response.GenericResponse;
import com.brinta.ems.service.BranchService;
import com.brinta.ems.tenant.TenantContext; // Assuming you have a TenantContext for tenant management
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    public ResponseEntity<BranchResponse> create(@RequestBody BranchRequest request) {
        // Automatically set tenantId from TenantContext if not already set
        Long tenantId = TenantContext.getTenantId();
        request.setTenantId(tenantId); // Ensure tenantId is set

        return ResponseEntity.ok(branchService.createBranch(request));
    }

    // Automatically uses tenantId from TenantContext for retrieving branches
    @GetMapping
    public ResponseEntity<List<BranchResponse>> getAll() {
        Long tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(branchService.getAllBranches(tenantId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.ok(new GenericResponse(true, "Branch deleted successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchResponse> update(@PathVariable Long id, @RequestBody BranchRequest request) {
        return ResponseEntity.ok(branchService.updateBranch(id, request));
    }
}
