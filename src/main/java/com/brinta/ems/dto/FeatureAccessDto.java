package com.brinta.ems.dto;

import lombok.Data;

import java.util.List;

@Data
public class FeatureAccessDto {
    private Long featureId;
    private List<Long> accessLevelIds;
}
