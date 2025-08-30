package com.brinta.ems.service.impl;

import com.brinta.ems.dto.RoleFeatureMappingDTO;
import com.brinta.ems.entity.Feature;
import com.brinta.ems.entity.Level;
import com.brinta.ems.entity.Role;
import com.brinta.ems.entity.RoleFeatureMapping;
import com.brinta.ems.exception.exceptionHandler.DuplicateMappingException;
import com.brinta.ems.exception.exceptionHandler.EntityNotFoundException;
import com.brinta.ems.repository.FeatureRepository;
import com.brinta.ems.repository.LevelsRepository;
import com.brinta.ems.repository.RoleFeatureMappingRepository;
import com.brinta.ems.repository.RoleRepository;
import com.brinta.ems.response.RoleFeatureMapResponse;
import com.brinta.ems.service.RoleFeatureMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleFeatureMappingServiceImpl implements RoleFeatureMappingService {

    @Autowired
    private RoleFeatureMappingRepository repository;

    @Autowired
    private FeatureRepository featureRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private LevelsRepository levelsRepository;
    @Override
    public RoleFeatureMapResponse assignFeatureToRole(RoleFeatureMappingDTO dto) {
        // Validate that the input DTO is not null
        if (dto == null) {
            throw new IllegalArgumentException("Input data cannot be null");
        }

        // Check for duplicate mapping
        if (repository.existsByRoleIdAndFeatureId(dto.getRoleId(), dto.getFeatureId())) {
            throw new DuplicateMappingException("Feature " + dto.getFeatureId() +
                    " is already assigned to role " + dto.getRoleId());
        }

        // Retrieve entities by ID, throwing an exception if not found
        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new EntityNotFoundException("Role with ID " + dto.getRoleId() + " not found."));

        Feature feature = featureRepository.findById(dto.getFeatureId())
                .orElseThrow(() -> new EntityNotFoundException("Feature with ID " + dto.getFeatureId() + " not found."));

        Level level = levelsRepository.findById(dto.getAccessLevelId())
                .orElseThrow(() -> new EntityNotFoundException("Level with ID " + dto.getAccessLevelId() + " not found."));

        // Create the RoleFeatureMapping
        RoleFeatureMapping mapping = new RoleFeatureMapping();
        mapping.setCreatedAt(LocalDateTime.now());
        mapping.setFeature(feature);
        mapping.setRole(role);
        mapping.setAccessLevel(level);

        // Set levelOfAccess as an array of strings
        mapping.setLevelOfAccess(new String[]{level.getName()});  // Now setting it as an array
        mapping.setFeatureName(feature.getName());
        mapping.setRoleName(role.getRoleName());
        mapping.setCreatedBy(dto.getCreatedBy());

        // Save the mapping
        repository.save(mapping);

        // Create and populate the RoleFeatureMapResponse object
        RoleFeatureMapResponse response = new RoleFeatureMapResponse();
        response.setRoleName(role.getRoleName());
        response.setFeatureName(feature.getName());
        response.setLevelName(level.getName());
        response.setLevelDescription(level.getDescription());
        response.setFeatureDescription(feature.getDescription());
        response.setRoleFeatureMapResponseId(role.getId());
        response.setRoleId(role.getId());
        response.setLevelId(level.getId());
        response.setFeatureId(feature.getId());// Use the correct grade ID from the Role entity
        // Use the correct department ID from the Role entity

        // Return the populated response object
        return response;
    }

    public RoleFeatureMapping convertToDTO(RoleFeatureMapping mapping) {
        // Create a new RoleFeatureMapping instance for the DTO
        RoleFeatureMapping dto = new RoleFeatureMapping();

        // Set the basic properties from the original entity
        dto.setId(mapping.getId());
        dto.setRoleName(mapping.getRole().getRoleName());
        dto.setFeatureName(mapping.getFeature().getName());
        dto.setCreatedBy(mapping.getCreatedBy());
        dto.setRole(mapping.getRole());
        dto.setFeature(mapping.getFeature());
        dto.setAccessLevel(mapping.getAccessLevel());

        // Now, set levelOfAccess as an array of strings
        if (mapping.getAccessLevel() != null && mapping.getAccessLevel().getName() != null) {
            dto.setLevelOfAccess(new String[]{mapping.getAccessLevel().getName()});  // Set as an array of strings
        }

        // Make sure the 'role' and 'feature' are properly initialized and not null
        if (mapping.getRole() != null) {
            dto.setRoleName(mapping.getRole().getRoleName());
        }
        if (mapping.getFeature() != null) {
            dto.setFeatureName(mapping.getFeature().getName());
        }

        // Adding created/updated timestamps if required
        dto.setCreatedAt(mapping.getCreatedAt());
        dto.setUpdatedAt(mapping.getUpdatedAt());

        return dto;
    }


    @Override
    public RoleFeatureMapping getRoleFeatureMappingById(Long mappingId) {
        if (mappingId == null) {
            throw new IllegalArgumentException("Mapping ID cannot be null.");
        }

        // Retrieve the mapping by ID
        Optional<RoleFeatureMapping> mappingOptional = repository.findById(mappingId);

        if (mappingOptional.isEmpty()) {
            throw new EntityNotFoundException("Mapping with ID " + mappingId + " not found.");
        }

        return mappingOptional.get();
    }

    @Override
    public List<RoleFeatureMapping> getFeaturesByRoleId(Long roleId) {
        if (roleId == null) {
            throw new IllegalArgumentException("Role ID cannot be null.");
        }

        // Retrieve mappings based on roleId
        List<RoleFeatureMapping> mappings = repository.findByRoleId(roleId);
        if (mappings.isEmpty()) {
            throw new EntityNotFoundException("No features found for role ID: " + roleId);
        }

        // Convert to DTO to avoid lazy-loaded entities being serialized
        return mappings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get all RoleFeatureMappings with validation
    @Override
    public List<RoleFeatureMapping> getAllRoleFeatureMappings() {
        // Retrieve all mappings
        List<RoleFeatureMapping> allMappings = repository.findAll();
        if (allMappings.isEmpty()) {
            throw new EntityNotFoundException("No role-feature mappings found.");
        }

        // Convert to DTO before returning
        return allMappings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void removeFeatureFromRole(Long mappingId) {
        // Validate the mappingId is not null
        if (mappingId == null) {
            throw new IllegalArgumentException("Mapping ID cannot be null.");
        }

        // Ensure the mapping exists before attempting to delete
        Optional<RoleFeatureMapping> mapping = repository.findById(mappingId);
        if (mapping.isEmpty()) {
            throw new EntityNotFoundException("Mapping with ID " + mappingId + " not found");
        }

        // Delete the mapping
        repository.deleteById(mappingId);
    }

}

