package com.brinta.ems.controller;

import com.brinta.ems.request.registerRequest.RoleBranchMapRequest;
import com.brinta.ems.response.BranchResponse;
import com.brinta.ems.service.RoleBranchMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role-branch")
@RequiredArgsConstructor
public class RoleBranchMappingController {

    private final RoleBranchMappingService roleBranchMappingService;

    @PostMapping("/map")
    public ResponseEntity<?> mapRoleToBranch(@RequestBody RoleBranchMapRequest request) {
        return ResponseEntity.ok(roleBranchMappingService.mapRoleToBranch(request));
    }


    @GetMapping("/branches/{roleId}")
    public ResponseEntity<List<BranchResponse>> getBranchesForRole(@PathVariable Long roleId) {
        return ResponseEntity.ok(roleBranchMappingService.getBranchesForRole(roleId));
    }
}
