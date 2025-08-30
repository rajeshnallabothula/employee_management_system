package com.brinta.ems.filter;

import com.brinta.ems.exception.exceptionHandler.JwtExpiredException;
import com.brinta.ems.exception.exceptionHandler.JwtInvalidException;
import com.brinta.ems.service.JwtService;
import com.brinta.ems.tenant.TenantContext;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        String contextPath = request.getContextPath();

        // ✅ Step 1: Allow public endpoints
        if (shouldSkipAuthentication(path, contextPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ Step 2: Get JWT token from header
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7); // Remove "Bearer "
        String username;

        try {
            // ✅ Step 3: Validate & extract user
            jwtService.isValidToken(jwt);
            username = jwtService.extractAllClaims(jwt).getSubject();

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.validateTokenForUser(jwt, userDetails)) {
                    Claims claims = jwtService.extractAllClaims(jwt);

                    // ✅ Step 4: Inject tenantId into context
                    Long tenantId = claims.get("tenantId", Long.class);
                    TenantContext.setTenantId(tenantId);

                    List<String> roles = claims.get("roles", List.class);
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(role -> "ROLE_" + role)
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    sendErrorResponse(response, "Invalid token", HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }

            filterChain.doFilter(request, response);

        } catch (JwtExpiredException ex) {
            sendErrorResponse(response, "Token expired", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (JwtInvalidException ex) {
            sendErrorResponse(response, "Invalid token", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception ex) {
            sendErrorResponse(response, "Something went wrong", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            // ✅ Always clear tenant context at the end of request
            TenantContext.clear();
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }

    private Boolean shouldSkipAuthentication(String path, String contextPath) {
        return path.equals(contextPath + "user/userAuthenticate")
                || path.equalsIgnoreCase(contextPath + "/user/logoutUserFromConcurrentSession")
                || path.startsWith(contextPath + "/swagger-ui")
                || path.startsWith(contextPath + "/api/auth/register")
                || path.startsWith(contextPath + "/api/auth/login")
                || path.startsWith(contextPath + "/health/check");
    }
}
