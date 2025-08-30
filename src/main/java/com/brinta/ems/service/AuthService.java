package com.brinta.ems.service;

import com.brinta.ems.entity.Tenant;
import com.brinta.ems.enums.Status;
import com.brinta.ems.repository.TenantRepository;
import com.brinta.ems.repository.UserRepository;
import com.brinta.ems.dto.RefreshTokenRequest;
import com.brinta.ems.dto.RegisterRequest;
import com.brinta.ems.dto.TokenPair;
import com.brinta.ems.entity.User;
import com.brinta.ems.request.registerRequest.LoginRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AuthService {

    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TenantRepository tenantRepository;

    @Transactional
    public void registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new IllegalArgumentException("Username is already in use");
        }

        User.UserBuilder userBuilder = User.builder()
                .name(registerRequest.getName())
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole());

        // ✅ SUPER_ADMIN should not have tenant
        if (registerRequest.getRole().name().equals("SUPER_ADMIN")) {
            userBuilder.tenant(null);
            userBuilder.tenant(null);
        } else {
            // ✅ Validate tenant ID
            if (registerRequest.getTenantId() == null) {
                throw new IllegalArgumentException("Tenant ID is required for non-SUPER_ADMIN roles");
            }

            // ✅ Load Tenant entity from DB
            Tenant tenant = tenantRepository.findById(registerRequest.getTenantId())
                    .orElseThrow(() -> new RuntimeException("Tenant not found"));

            userBuilder.tenant(tenant);
        }

        userRepository.save(userBuilder.build());
    }

    public TokenPair login(LoginRequest loginRequest) {
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // Set authentication in security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate Token Pair
        return jwtService.generateTokenPair(authentication);
    }

    public TokenPair loginSuperAdmin(LoginRequest loginRequest) {
        // Authenticate the super admin
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // ✅ Use a new method that skips tenant injection
        return jwtService.generateTokenPairForSuperAdmin(authentication);
    }


    public TokenPair refreshToken(@Valid RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();
        // check if it is a valid refresh token
        if(!jwtService.isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String user = jwtService.extractUsernameFromToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user);

        if (userDetails == null) {
            throw new IllegalArgumentException("User not found");
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        String accessToken = jwtService.generateAccessToken(authentication);
        return new TokenPair(accessToken, refreshToken);
    }

}