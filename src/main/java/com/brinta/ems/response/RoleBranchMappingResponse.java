package com.brinta.ems.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleBranchMappingResponse {
    private Long roleId;
    private String roleName;
    private Long branchId;
    private String branchName;
    private Long tenantId;
}
