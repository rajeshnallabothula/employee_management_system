package com.brinta.ems.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchFeatureAccessResponse {
    private String featureName;
    private List<String> accessLevels;
}
