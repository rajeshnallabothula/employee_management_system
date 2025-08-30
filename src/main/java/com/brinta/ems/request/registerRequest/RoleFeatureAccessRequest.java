package com.brinta.ems.request.registerRequest;

import lombok.Data;
import java.util.List;

@Data
public class RoleFeatureAccessRequest {
    private Long featureId;
    private List<Long> accessLevelIds;
    private Long branchId; // optional
}
