package com.brinta.ems.request.registerRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FeatureRequest {

    @NotBlank(message = "Feature name is required")
    private String name;

    @NotNull(message = "Feature status is required")
    private Boolean isActive;

    private String description;

    private String createdBy;

    private String updatedBy;

}

