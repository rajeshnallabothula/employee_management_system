package com.brinta.ems.service;

import com.brinta.ems.entity.Department;
import com.brinta.ems.request.registerRequest.DepartmentRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DepartmentService
{
    public List<Department> getAll();

    public ResponseEntity<?> getById(Long id);

    public ResponseEntity<?> createDept(DepartmentRequest request);
}
