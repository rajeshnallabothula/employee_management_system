package com.brinta.ems.controller;

import com.brinta.ems.dto.RefreshTokenRequest;
import com.brinta.ems.dto.RegisterRequest;
import com.brinta.ems.dto.TokenPair;
import com.brinta.ems.request.registerRequest.LoginRequest;
import com.brinta.ems.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;

    // Open endpoint to register a SUPER_ADMIN (First time setup)
    @PostMapping("/register-superadmin")
    public ResponseEntity<String> registerSuperAdmin(@Valid @RequestBody RegisterRequest request) {
        if (!request.getRole().name().equals("SUPER_ADMIN")) {
            return ResponseEntity.badRequest().body("Only SUPER_ADMIN registration allowed here");
        }
        authService.registerUser(request);
        return ResponseEntity.ok("SuperAdmin registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenPair> login(@Valid @RequestBody LoginRequest request) {
        TokenPair tokenPair = authService.login(request);
        return ResponseEntity.ok(tokenPair);
    }
    @PostMapping("/login-superadmin")
    public ResponseEntity<TokenPair> loginSuperAdmin(@Valid @RequestBody LoginRequest request) {
        TokenPair tokenPair = authService.loginSuperAdmin(request);
        return ResponseEntity.ok(tokenPair);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenPair> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

}

