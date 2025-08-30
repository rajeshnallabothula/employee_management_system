package com.brinta.ems.controller;


import com.brinta.ems.dto.RoleDto;
import com.brinta.ems.entity.Role;
import com.brinta.ems.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/1.0/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // Create a new role
    @PostMapping("/createRole")
    public ResponseEntity<?> createRole(@RequestBody @Valid RoleDto dto, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            result.getAllErrors().forEach(error -> {
                errorMessages.append(error.getDefaultMessage()).append(" ");
            });
            return ResponseEntity.badRequest().body("Validation failed: " + errorMessages.toString());
        }

        Role savedRole = roleService.createRole(dto, result);
        return ResponseEntity.ok(savedRole);
    }

    // Get all roles
    @GetMapping("/getAllRoles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    // Get a specific role by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(@PathVariable Long id) {
        // Validate that id is a positive number
        if (id <= 0) {
            // Return bad request with a message
            return ResponseEntity.badRequest().body("Invalid ID: ID must be a positive number.");
        }

        // Fetch the role by ID
        Optional<Role> role = roleService.getRoleById(id);

        if (role.isPresent()) {
            // If role exists, return the role with status OK
            return ResponseEntity.ok(role.get());
        } else {
            // If role doesn't exist, return not found with a message
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found with ID: " + id);
        }
    }

    // Update a role by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@PathVariable Long id, @RequestBody RoleDto dto) {
        return (roleService.updateRole(id, dto));
    }

    // Delete a role by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        return roleService.deleteRole(id);
    }

}

