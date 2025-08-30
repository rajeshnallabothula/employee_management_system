package com.brinta.ems.controller;


import com.brinta.ems.entity.Department;
import com.brinta.ems.request.registerRequest.DepartmentRequest;
import com.brinta.ems.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/1.0/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    @RequestMapping("getAll")
    public List<Department> getDepartments() {
        return departmentService.getAll();
    }

    @GetMapping
    @RequestMapping("/getById")
    public ResponseEntity<?> getDepartmentById(@RequestParam Long id)
    {
        return departmentService.getById(id);
    }

    @PostMapping
    @RequestMapping("/create")
    public ResponseEntity<?> createDepartment(@Valid @RequestBody DepartmentRequest request) {
return departmentService.createDept(request);
    }

}
