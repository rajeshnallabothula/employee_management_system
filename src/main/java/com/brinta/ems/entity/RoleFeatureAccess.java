package com.brinta.ems.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "role_feature_access",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"role_id", "feature_id", "access_level_id", "branch_id"})
        }
)
public class RoleFeatureAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Role role;

    @ManyToOne
    private Feature feature;

    @ManyToOne
    private AccessLevel accessLevel;

    @ManyToOne(optional = true)
    private Branch branch;
}