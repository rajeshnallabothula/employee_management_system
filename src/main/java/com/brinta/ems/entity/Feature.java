package com.brinta.ems.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "feature")
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @NotBlank(message = "Feature name cannot be blank")
    @Size(max = 255, message = "Feature name must be less than 255 characters")
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Size(max = 500, message = "Feature description must be less than 500 characters")
    @Column(name = "description", length = 500)
    private String description;

    @NotNull(message = "Feature status (isActive) is required")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @NotNull(message = "Created by cannot be null")
    @Size(min = 3, max = 100, message = "Created by must be between 3 and 100 characters")
    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @Size(max = 100, message = "Updated by cannot exceed 100 characters")
    @Column(name = "updated_by")
    private String updatedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tenant tenant;

    @Column(name = "tenant_id", insertable = false, updatable = false)
    private Long tenantId;

    public Feature(Object o, @NotBlank(message = "Feature name is required") String name, String description, Tenant tenant, Long id) {
    }
}

