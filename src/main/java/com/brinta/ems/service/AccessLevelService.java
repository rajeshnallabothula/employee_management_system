package com.brinta.ems.service;

import com.brinta.ems.request.registerRequest.AccessLevelRequest;
import com.brinta.ems.response.AccessLevelResponse;

import java.util.List;

public interface AccessLevelService {
    AccessLevelResponse create(AccessLevelRequest request);
    List<AccessLevelResponse> getAll(Long tenantId);
    AccessLevelResponse update(Long id, AccessLevelRequest request);
    void delete(Long id);
}
