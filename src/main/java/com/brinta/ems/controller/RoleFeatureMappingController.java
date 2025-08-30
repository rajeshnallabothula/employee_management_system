package com.brinta.ems.controller;


import com.brinta.ems.dto.RoleFeatureMappingDTO;
import com.brinta.ems.entity.RoleFeatureMapping;
import com.brinta.ems.response.RoleFeatureMapResponse;
import com.brinta.ems.service.RoleFeatureMappingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/1.0/role-feature-mapping")
public class RoleFeatureMappingController {

    @Autowired
    private RoleFeatureMappingService service;

    @PostMapping("assignFeatureToRole")
    public ResponseEntity<RoleFeatureMapResponse> assignFeatureToRole(@Valid @RequestBody RoleFeatureMappingDTO dto) {
        // Call the service method to assign the feature to the role and get the response object
        RoleFeatureMapResponse response = service.assignFeatureToRole(dto);

        // Return the response wrapped in ResponseEntity with HTTP status CREATED (201)
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("getFeaturesByRoleId/{roleId}")
    public ResponseEntity<List<RoleFeatureMapping>> getFeaturesByRoleId(@PathVariable Long roleId) {
        List<RoleFeatureMapping> mappings = service.getFeaturesByRoleId(roleId);
        return new ResponseEntity<>(mappings, HttpStatus.OK);
    }

    @GetMapping("/getRoleFeatureMappingById/{mappingId}")
    public ResponseEntity<RoleFeatureMapping> getRoleFeatureMappingById(@PathVariable Long mappingId) {
        RoleFeatureMapping mapping = service.getRoleFeatureMappingById(mappingId);
        return new ResponseEntity<>(mapping, HttpStatus.OK);
    }

    @GetMapping("getAllRoleFeatureMappings")
    public ResponseEntity<List<RoleFeatureMapping>> getAllRoleFeatureMappings() {
        List<RoleFeatureMapping> mappings = service.getAllRoleFeatureMappings();
        return new ResponseEntity<>(mappings, HttpStatus.OK);
    }

    @DeleteMapping("removeFeatureFromRole/{id}")
    public ResponseEntity<Void> removeFeatureFromRole(@PathVariable Long id) {
        service.removeFeatureFromRole(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

