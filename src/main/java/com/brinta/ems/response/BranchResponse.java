package com.brinta.ems.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchResponse {
    private Long branchId;
    private String name;
    private String location;
    private Long tenantId;
    private List<BranchFeatureAccessResponse> features;
}
