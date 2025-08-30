package com.brinta.ems.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessLevelResponse {
    private Long id;
    private String levelName;
    private String description;
    private Long tenantId;
}
