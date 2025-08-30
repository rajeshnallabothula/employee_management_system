// RoleBranchFeatureAccessResponse.java
package com.brinta.ems.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RoleBranchFeatureAccessResponse {
    private Long roleId;
    private String roleName;
    private Long branchId;
    private String branchName;
    private Long tenantId;
    private List<BranchFeatureAccessResponse> features;
}
