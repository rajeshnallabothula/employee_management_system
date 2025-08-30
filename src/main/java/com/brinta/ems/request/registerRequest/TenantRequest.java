package com.brinta.ems.request.registerRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantRequest {
    @NotBlank(message = "Tenant name is required")
    private String name;

    @NotBlank(message = "Tenant code is required")
    private String code;
}
