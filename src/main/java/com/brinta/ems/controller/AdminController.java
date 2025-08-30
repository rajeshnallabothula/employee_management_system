package com.brinta.ems.controller;

import com.brinta.ems.dto.TokenPair;
import com.brinta.ems.entity.Admin;
import com.brinta.ems.request.registerRequest.LoginRequest;
import com.brinta.ems.request.registerRequest.RegisterAdminRequest;
import com.brinta.ems.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/1.0/admin")
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    private final AdminService adminService;

    // Admin registration
    @PostMapping("/register")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Admin> registerAdmin(@Valid @RequestBody RegisterAdminRequest request) {
        Admin registeredAdmin = adminService.registerAdmin(request);
        return ResponseEntity.ok(registeredAdmin);
    }

    // Admin login
    @PostMapping("/login")
    public ResponseEntity<TokenPair> loginAdmin(@Valid @RequestBody LoginRequest request) {
        TokenPair tokenPair = adminService.loginAdmin(request);
        return ResponseEntity.ok(tokenPair);
    }
}
