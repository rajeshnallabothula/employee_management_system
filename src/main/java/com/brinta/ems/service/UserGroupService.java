package com.brinta.ems.service;


import com.brinta.ems.request.registerRequest.UserGroupRequest;
import com.brinta.ems.response.UserGroupResponse;

import java.util.List;

public interface UserGroupService {
    void createUserGroup(com.brinta.ems.request.registerRequest.UserGroupRequest request);
    void updateUserGroup(Long id, UserGroupRequest request);
    void deleteUserGroup(Long id);
    UserGroupResponse getUserGroupById(Long id);
    List<UserGroupResponse> getAllUserGroups();
}