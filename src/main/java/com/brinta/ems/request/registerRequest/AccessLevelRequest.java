package com.brinta.ems.request.registerRequest;

import lombok.Data;

@Data
public class AccessLevelRequest {
    private Long tenantId;
    private String levelName;
    private String description;
}
