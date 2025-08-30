package com.brinta.ems.service;


import com.brinta.ems.request.registerRequest.TenantRequest;

public interface TenantService {
    void createTenant(TenantRequest request);
}
