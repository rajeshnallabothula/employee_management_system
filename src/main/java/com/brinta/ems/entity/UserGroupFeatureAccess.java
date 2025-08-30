package com.brinta.ems.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_group_feature_access")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupFeatureAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserGroup userGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    private Feature feature;

    @ManyToOne(fetch = FetchType.LAZY)
    private AccessLevel accessLevel;
}
