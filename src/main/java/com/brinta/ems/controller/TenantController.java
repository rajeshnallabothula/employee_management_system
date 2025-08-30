package com.brinta.ems.controller;

import com.brinta.ems.request.registerRequest.TenantRequest;
import com.brinta.ems.service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tenant")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> createTenant(@Valid @RequestBody TenantRequest request) {
        tenantService.createTenant(request);
        return ResponseEntity.ok("Tenant created successfully");
    }
}
