package com.brinta.ems.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "grades")
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private Long id;

    @NotNull(message = "Grade code cannot be null")
    @Size(min = 1, max = 5, message = "Grade code must be between 1 and 5 characters")
    @Column(name = "grade_code", nullable = false)
    private String gradeCode;

    @NotNull(message = "Grade name cannot be null")
    @Size(min = 3, max = 100, message = "Grade name must be between 3 and 100 characters")
    @Column(name = "grade_name", nullable = false)
    private String gradename;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @NotNull(message = "Created by cannot be null")
    @Size(min = 3, max = 100, message = "Created by must be between 3 and 100 characters")
    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @Size(max = 100, message = "Updated by cannot exceed 100 characters")
    @Column(name = "updated_by")
    private String updatedBy;

}

