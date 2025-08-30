package com.brinta.ems.request.registerRequest;

import lombok.Data;

import java.util.List;

@Data
public class BranchRequest {
    private Long tenantId;
    private String name;
    private String location;
    private Long featureId;
    private List<Long> accessLevelIds;
    private List<BranchFeatureAccessRequest> featureAccessMap;
}