package com.brinta.ems.request.registerRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RegisterAdminRequest {

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotBlank
    private String contact;

    @NotBlank
    private String password;

    @NotNull(message = "Tenant ID is required")
    private Long tenantId;

    
}
