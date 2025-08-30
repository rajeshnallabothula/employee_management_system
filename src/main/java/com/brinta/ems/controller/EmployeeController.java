package com.brinta.ems.controller;

import com.brinta.ems.request.registerRequest.EmployeeRequest;
import com.brinta.ems.response.EmployeeResponse;
import com.brinta.ems.response.GenericResponse;
import com.brinta.ems.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<GenericResponse> createEmployee(@RequestBody EmployeeRequest request) {
        employeeService.createEmployee(request);
        return ResponseEntity.ok(new GenericResponse(true, "Employee created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse> updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequest request) {
        employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(new GenericResponse(true, "Employee updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(new GenericResponse(true, "Employee deleted successfully"));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}/access")
    public ResponseEntity<EmployeeResponse> getEmployeeAccess(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeAccess(id));
    }
}
