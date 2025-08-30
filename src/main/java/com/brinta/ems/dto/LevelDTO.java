package com.brinta.ems.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LevelDTO {


    private Long id;

    @NotNull(message = "Level name cannot be null")
    @Size(min = 3, max = 100, message = "Level name must be between 3 and 100 characters")
    private String name;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    @NotNull(message = "Created by cannot be null")
    @Size(min = 3, max = 100, message = "Created by must be between 3 and 100 characters")
    private String createdBy;

    @Size(max = 100, message = "Updated by cannot exceed 100 characters")
    private String updatedBy;

}

