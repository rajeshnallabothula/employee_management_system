package com.brinta.ems.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

    @NotNull(message = "Created by cannot be null")
    private String createdBy;

    private Boolean isActive = true;

    @NotNull(message = "Role description cannot be null")
    private String roleDescription;

    @NotNull(message = "Role name cannot be null")
    private String roleName;

    @NotNull(message = "Updated by cannot be null")
    private String updatedBy;

    // NEW FIELD
    private Long userGroupId;


}

