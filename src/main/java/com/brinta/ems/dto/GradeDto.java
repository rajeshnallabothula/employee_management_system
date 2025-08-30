package com.brinta.ems.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GradeDto {

    private Long id;

    @NotNull(message = "Level name cannot be null.")
    private String gradename;

    @NotNull(message="grade code name cannot be null")
    private String gradeCode;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @NotNull(message = "Created by cannot be null.")
    private String createdBy;

    @Size(max = 100, message = "Updated by cannot exceed 100 characters.")
    private String updatedBy;

}

