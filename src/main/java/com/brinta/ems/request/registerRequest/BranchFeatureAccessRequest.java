package com.brinta.ems.request.registerRequest;

import lombok.Data;

import java.util.List;

@Data
public class BranchFeatureAccessRequest {
    private Long featureId;
    private List<Long> accessLevelIds;
}
