package com.brinta.ems.service.impl;


import com.brinta.ems.entity.Department;
import com.brinta.ems.repository.DepartmentRepository;
import com.brinta.ems.request.registerRequest.DepartmentRequest;
import com.brinta.ems.response.GenericResponse;
import com.brinta.ems.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    DepartmentRepository departmentRepository;

    @Override
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    public ResponseEntity<?> getById(Long id) {
        Optional<Department> departmentOpn = departmentRepository.findById(id);

        if (departmentOpn.isPresent()) {
            Department department = departmentOpn.get();
            return ResponseEntity.ok().body(department);
        }

        return ResponseEntity.ok().body(new GenericResponse(false, "Department not found"));
    }

    @Override
    public ResponseEntity<?> createDept(@Valid @RequestBody DepartmentRequest request) {
        // Check if a department with the same name or code already exists
        if (departmentRepository.existsByName(request.getName())) {
            return ResponseEntity.badRequest().body(new GenericResponse(false, "Department name already exists"));
        }

        if (departmentRepository.existsByCode(request.getCode())) {
            return ResponseEntity.badRequest().body(new GenericResponse(false, "Department code already exists"));
        }

        // Create department
        Department department = new Department();
        department.setName(request.getName());
        department.setCode(request.getCode());
        department.setIsActive(request.getIsActive());
        department.setCreatedAt(new Date());
        department.setUpdatedAt(new Date());
        department.setCreatedBy(request.getCreatedBy());

        // Save to DB
        departmentRepository.save(department);

        return ResponseEntity.ok(new GenericResponse(true, "Department created successfully"));
    }

}
