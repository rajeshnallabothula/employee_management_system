package com.brinta.ems.service;

import com.brinta.ems.request.registerRequest.FeatureRequest;
import com.brinta.ems.response.FeatureResponse;
import java.util.List;

public interface FeatureService {

    FeatureResponse create(FeatureRequest request);

    List<FeatureResponse> getAll(Long tenantId);

    FeatureResponse update(Long id, FeatureRequest request);

    void delete(Long id);

}

