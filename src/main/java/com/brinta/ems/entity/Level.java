package com.brinta.ems.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    private Long id;

    @NotNull(message = "Level name cannot be null.")
    @Size(min = 3, max = 100, message = "Level name must be between 3 and 100 characters.")
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255, message = "Description cannot exceed 255 characters.")
    @Column(name = "description")
    private String description;

    @NotNull(message = "Created at cannot be null.")
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @NotNull(message = "Created by cannot be null.")
    @Size(min = 3, max = 100, message = "Created by must be between 3 and 100 characters.")
    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @Size(max = 100, message = "Updated by cannot exceed 100 characters.")
    @Column(name = "updated_by")
    private String updatedBy;

}

