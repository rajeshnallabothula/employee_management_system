package com.brinta.ems.request.registerRequest;

import com.brinta.ems.dto.FeatureAccessDto;
import lombok.Data;
import java.util.List;

@Data
public class UserGroupRequest {
    private String name;
    private List<FeatureAccessDto> featureAccessList;
}
