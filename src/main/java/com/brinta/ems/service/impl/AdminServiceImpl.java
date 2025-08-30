package com.brinta.ems.service.impl;

import com.brinta.ems.dto.TokenPair;
import com.brinta.ems.entity.Admin;
import com.brinta.ems.entity.Tenant;
import com.brinta.ems.entity.User;
import com.brinta.ems.enums.Roles;
import com.brinta.ems.enums.Status;
import com.brinta.ems.exception.exceptionHandler.DuplicateEntryException;
import com.brinta.ems.repository.AdminRepository;
import com.brinta.ems.repository.TenantRepository;
import com.brinta.ems.repository.UserRepository;
import com.brinta.ems.request.registerRequest.LoginRequest;
import com.brinta.ems.request.registerRequest.RegisterAdminRequest;
import com.brinta.ems.service.AdminService;
import com.brinta.ems.service.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
;
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private  UserRepository userRepository;

    @Override
    @Transactional
    public Admin registerAdmin(RegisterAdminRequest request) {

        Tenant tenant = tenantRepository.findById(request.getTenantId())
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEntryException("Email already exists");
        }
        if (adminRepository.existsByContact(request.getContact())) {
            throw new DuplicateEntryException("Contact already exists");
        }

        // 1. Create and save Admin
        Admin admin = new Admin();
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setContact(request.getContact());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole(Roles.ADMIN);
        admin.setTenant(tenant);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());

        Admin savedAdmin = adminRepository.save(admin);

        // 2. ✅ Also create User entity for JWT and security
        User user = new User();
        user.setName(savedAdmin.getName());
        user.setUsername(savedAdmin.getEmail()); // important: should match login
        user.setEmail(savedAdmin.getEmail());
        user.setPassword(savedAdmin.getPassword()); // already encoded
        user.setRole(savedAdmin.getRole());
        user.setTenant(tenant);

        userRepository.save(user); // 🔐 this is the key for JWT login to work

        return savedAdmin;
    }


    @Override
    public TokenPair loginAdmin(LoginRequest request) {
        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                admin.getEmail(),
                admin.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + admin.getRole().name()))
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                admin.getPassword(),
                userDetails.getAuthorities()
        );

        return jwtService.generateTokenPair(authentication);
    }

}

