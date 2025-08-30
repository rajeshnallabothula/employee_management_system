package com.brinta.ems.service.impl;

import com.brinta.ems.entity.Feature;
import com.brinta.ems.entity.Tenant;
import com.brinta.ems.exception.exceptionHandler.ResourceNotFoundException;
import com.brinta.ems.repository.FeatureRepository;
import com.brinta.ems.repository.TenantRepository;
import com.brinta.ems.request.registerRequest.FeatureRequest;
import com.brinta.ems.response.FeatureResponse;
import com.brinta.ems.service.FeatureService;
import com.brinta.ems.tenant.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements FeatureService {

    private final FeatureRepository featureRepository;
    private final TenantRepository tenantRepository;

    @Override
    public FeatureResponse create(FeatureRequest request) {

        Long tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant ID is missing in context");
        }

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id: " + tenantId));

        Feature feature = new Feature();
        feature.setName(request.getName());
        feature.setDescription(request.getDescription());
        feature.setIsActive(request.getIsActive());
        feature.setTenant(tenant);
        feature.setTenantId(tenant.getId());
        feature.setCreatedBy(request.getCreatedBy());
        feature.setUpdatedBy(request.getUpdatedBy());
        feature.setCreatedAt(new Date());
        feature.setUpdatedAt(new Date());

        feature = featureRepository.save(feature);

        return new FeatureResponse(feature.getId(), feature.getName(), feature.getDescription(), feature.getTenantId());
    }

    @Override
    public List<FeatureResponse> getAll(Long tenantId) {
        return featureRepository.findByTenantId(tenantId).stream()
                .map(f -> new FeatureResponse(f.getId(), f.getName(), f.getDescription(), f.getTenantId()))
                .toList();
    }

    @Override
    public FeatureResponse update(Long id, FeatureRequest request) {
        Feature feature = featureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feature not found with id: " + id));

        feature.setName(request.getName());
        feature.setDescription(request.getDescription());
        feature.setIsActive(request.getIsActive());
        feature.setUpdatedBy(request.getUpdatedBy());
        feature.setUpdatedAt(new Date());

        feature = featureRepository.save(feature);

        return new FeatureResponse(feature.getId(), feature.getName(), feature.getDescription(), feature.getTenantId());
    }

    @Override
    public void delete(Long id) {
        Feature feature = featureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feature not found with id: " + id));
        featureRepository.delete(feature);
    }


}
