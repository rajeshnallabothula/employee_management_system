package com.brinta.ems.service.impl;

import com.brinta.ems.entity.Tenant;
import com.brinta.ems.repository.TenantRepository;
import com.brinta.ems.request.registerRequest.TenantRequest;
import com.brinta.ems.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;

    @Override
    public void createTenant(TenantRequest request) {
        // Optional: check if code already exists
        if (tenantRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Tenant with code already exists");
        }

        Tenant tenant = new Tenant();
        tenant.setName(request.getName());
        tenant.setCode(request.getCode());
        tenant.setIsActive(true); // default

        tenantRepository.save(tenant);
    }
}
