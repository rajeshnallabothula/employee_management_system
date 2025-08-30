package com.brinta.ems.request.registerRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DepartmentRequest {
    @NotBlank(message = "Department name is required")
    private String name;

    @NotBlank(message = "Department code is required")
    private String code;

    @NotNull(message = "Active status is required")
    private Boolean isActive;

    private String createdBy;
    private String updatedBy;

}
