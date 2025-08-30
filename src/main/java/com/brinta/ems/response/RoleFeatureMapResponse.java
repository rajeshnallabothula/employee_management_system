package com.brinta.ems.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RoleFeatureMapResponse {

    @NotNull(message = "Role ID cannot be null")
    private Long roleId;

    @NotNull(message = "Role name cannot be null")
    @Size(min = 2, max = 100, message = "Role name must be between 2 and 100 characters")
    private String roleName;

    @NotNull(message = "Feature name cannot be null")
    @Size(min = 2, max = 100, message = "Feature name must be between 2 and 100 characters")
    private String featureName;

    @NotNull(message = "Feature ID cannot be null")
    private Long featureId;

    @Size(max = 255, message = "Feature description cannot exceed 255 characters")
    private String featureDescription;

    @NotNull(message = "Level name cannot be null")
    @Size(min = 2, max = 100, message = "Level name must be between 2 and 100 characters")
    private String levelName;

    @NotNull(message = "Level ID cannot be null")
    private Long levelId;

    @Size(max = 255, message = "Level description cannot exceed 255 characters")
    private String levelDescription;

    @NotNull(message = "Grade code cannot be null")
    @Size(min = 2, max = 50, message = "Grade code must be between 2 and 50 characters")
    private String gradeCode;

    @NotNull(message = "Grade ID cannot be null")
    private Long gradeId;

    @NotNull(message = "Grade name cannot be null")
    @Size(min = 2, max = 100, message = "Grade name must be between 2 and 100 characters")
    private String gradeName;

    @NotNull(message = "RoleFeatureMapResponse ID cannot be null")
    private Long roleFeatureMapResponseId;

    @NotNull(message = "Department ID cannot be null")
    private Long departmentId;

}
