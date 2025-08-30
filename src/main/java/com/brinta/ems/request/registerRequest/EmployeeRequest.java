package com.brinta.ems.request.registerRequest;

import lombok.Data;

@Data
public class EmployeeRequest {
    private String name;
    private String email;
    private Long userGroupId;
}
