// EmployeeServiceImpl.java
package com.brinta.ems.service.impl;

import com.brinta.ems.entity.Employee;
import com.brinta.ems.entity.UserGroup;
import com.brinta.ems.repository.EmployeeRepository;
import com.brinta.ems.repository.UserGroupRepository;
import com.brinta.ems.request.registerRequest.EmployeeRequest;
import com.brinta.ems.response.EmployeeResponse;
import com.brinta.ems.response.BranchFeatureAccessResponse;
import com.brinta.ems.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserGroupRepository userGroupRepository;

    @Override
    public void createEmployee(EmployeeRequest request) {
        UserGroup group = userGroupRepository.findById(request.getUserGroupId())
                .orElseThrow(() -> new RuntimeException("User Group not found"));

        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setUserGroup(group);

        employeeRepository.save(employee);
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getUserGroup().getName()
        );
    }

    @Override
    public void updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        UserGroup group = userGroupRepository.findById(request.getUserGroupId())
                .orElseThrow(() -> new RuntimeException("User Group not found"));

        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setUserGroup(group);

        employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(emp -> new EmployeeResponse(
                        emp.getId(),
                        emp.getName(),
                        emp.getEmail(),
                        emp.getUserGroup().getName()
                )).collect(Collectors.toList());
    }

    @Override
    public EmployeeResponse getEmployeeAccess(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        List<BranchFeatureAccessResponse> access = employee.getUserGroup().getFeatureAccesses().stream()
                .collect(Collectors.groupingBy(
                        fa -> fa.getFeature().getName(),
                        Collectors.mapping(fa -> fa.getAccessLevel().getLevelName(), Collectors.toList())
                ))
                .entrySet().stream()
                .map(e -> new BranchFeatureAccessResponse(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getUserGroup().getName()
        );
    }

}
