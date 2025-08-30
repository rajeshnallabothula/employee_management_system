package com.brinta.ems.service.impl;

import com.brinta.ems.entity.AccessLevel;
import com.brinta.ems.entity.Tenant;
import com.brinta.ems.exception.exceptionHandler.ResourceNotFoundException;
import com.brinta.ems.repository.AccessLevelRepository;
import com.brinta.ems.repository.TenantRepository;
import com.brinta.ems.request.registerRequest.AccessLevelRequest;
import com.brinta.ems.response.AccessLevelResponse;
import com.brinta.ems.service.AccessLevelService;
import com.brinta.ems.tenant.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessLevelServiceImpl implements AccessLevelService {

    private final AccessLevelRepository accessLevelRepository;
    private final TenantRepository tenantRepository;

    @Override
    public AccessLevelResponse create(AccessLevelRequest request) {
        Long tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant ID is missing in context");
        }

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id: " + tenantId));

        AccessLevel accessLevel = new AccessLevel();
        accessLevel.setLevelName(request.getLevelName());
        accessLevel.setDescription(request.getDescription());
        accessLevel.setTenant(tenant);

        accessLevel = accessLevelRepository.save(accessLevel);

        return new AccessLevelResponse(
                accessLevel.getId(),
                accessLevel.getLevelName(),
                accessLevel.getDescription(),
                tenant.getId()
        );
    }


    public List<AccessLevelResponse> getAll(Long tenantId) {
        return accessLevelRepository.findByTenantId(tenantId).stream()
                .map(al -> new AccessLevelResponse(al.getId(), al.getLevelName(), al.getDescription(), al.getTenantId()))
                .toList();
    }

    public AccessLevelResponse update(Long id, AccessLevelRequest request) {
        AccessLevel al = accessLevelRepository.findById(id).orElseThrow();
        al.setLevelName(request.getLevelName());
        al.setDescription(request.getDescription());
        accessLevelRepository.save(al);
        return new AccessLevelResponse(al.getId(), al.getLevelName(), al.getDescription(), al.getTenantId());
    }

    @Override
    public void delete(Long id) {
        AccessLevel level = accessLevelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AccessLevel not found with id: " + id));
        accessLevelRepository.delete(level);
    }
}
