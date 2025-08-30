package com.brinta.ems.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FeatureResponse {
    private Long id;
    private String name;
    private String description;
    private Long tenantId;
}
