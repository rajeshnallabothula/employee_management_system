package com.brinta.ems.tenant;

import com.brinta.ems.tenant.TenantContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class TenantFilterInterceptor implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String tenantIdHeader = httpRequest.getHeader("X-Tenant-ID");

        if (tenantIdHeader != null) {
            try {
                Long tenantId = Long.parseLong(tenantIdHeader);
                TenantContext.setTenantId(tenantId);
            } catch (NumberFormatException e) {
                // Optionally: throw custom exception or log
            }
        }

        try {
            chain.doFilter(request, response);
        } finally {
            TenantContext.clear(); // Clear after request
        }
    }

    // Optional: if you're seeing "must implement getName()", you can safely ignore or add this dummy method.
    public String getName() {
        return "TenantFilterInterceptor";
    }
}
