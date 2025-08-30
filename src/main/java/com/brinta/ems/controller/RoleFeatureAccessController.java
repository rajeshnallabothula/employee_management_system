package com.brinta.ems.controller;

import com.brinta.ems.request.registerRequest.RoleFeatureAccessRequest;
import com.brinta.ems.response.GenericResponse;
import com.brinta.ems.service.RoleFeatureAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleFeatureAccessController {

    private final RoleFeatureAccessService roleFeatureAccessService;

    @PostMapping("/{roleId}/feature-access")
    public ResponseEntity<GenericResponse> addFeatureAccessToRole(
            @PathVariable Long roleId,
            @RequestBody RoleFeatureAccessRequest request) {
        roleFeatureAccessService.addFeatureAccessToRole(roleId, request);
        return ResponseEntity.ok(new GenericResponse(true, "Access added successfully"));
    }

    @PutMapping("/{roleId}/feature-access")
    public ResponseEntity<GenericResponse> updateFeatureAccessForRole(
            @PathVariable Long roleId,
            @RequestBody RoleFeatureAccessRequest request) {
        roleFeatureAccessService.updateFeatureAccessForRole(roleId, request);
        return ResponseEntity.ok(new GenericResponse(true, "Access updated successfully"));
    }

    @GetMapping("/{roleId}/feature-access")
    public ResponseEntity<?> getAllAccessForRole(@PathVariable Long roleId) {
        return ResponseEntity.ok(roleFeatureAccessService.getAllAccessByRole(roleId));
    }

    @DeleteMapping("/{roleId}/feature-access")
    public ResponseEntity<GenericResponse> deleteFeatureAccessForRole(
            @PathVariable Long roleId,
            @RequestBody RoleFeatureAccessRequest request) {
        roleFeatureAccessService.deleteFeatureAccessForRole(roleId, request);
        return ResponseEntity.ok(new GenericResponse(true, "Access deleted successfully"));
    }
}
