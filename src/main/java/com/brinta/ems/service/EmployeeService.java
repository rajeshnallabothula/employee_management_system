package com.brinta.ems.service;


import com.brinta.ems.request.registerRequest.EmployeeRequest;
import com.brinta.ems.response.EmployeeResponse;

import java.util.List;

public interface EmployeeService {
    void createEmployee(EmployeeRequest request);
    EmployeeResponse getEmployeeById(Long id);
    void updateEmployee(Long id, EmployeeRequest request);
    void deleteEmployee(Long id);
    List<EmployeeResponse> getAllEmployees();
    EmployeeResponse getEmployeeAccess(Long id);
}