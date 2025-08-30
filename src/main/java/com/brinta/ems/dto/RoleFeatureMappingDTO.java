package com.brinta.ems.dto;

import com.brinta.ems.entity.Feature;
import com.brinta.ems.entity.Level;
import com.brinta.ems.entity.Role;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleFeatureMappingDTO {

    @NotNull(message = "Role ID is mandatory")
    private Long roleId;

    @NotNull(message = "Access Level ID is mandatory")
    private Long accessLevelId;

    @NotNull(message = "Feature ID is mandatory")
    private Long featureId;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "created_by")
    private String createdBy;

    private
    Feature feature;

    private Role role;

    private Level accessLevel;

    // Change levelOfAccess from String to String[]
    private String[] levelOfAccess;

}

