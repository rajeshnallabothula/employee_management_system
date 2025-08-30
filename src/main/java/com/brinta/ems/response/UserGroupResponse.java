package com.brinta.ems.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupResponse {
    private Long id;
    private String name;
    private List<BranchFeatureAccessResponse> features;
}
