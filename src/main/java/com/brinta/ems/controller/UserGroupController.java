package com.brinta.ems.controller;

import com.brinta.ems.request.registerRequest.UserGroupRequest;
import com.brinta.ems.response.GenericResponse;
import com.brinta.ems.response.UserGroupResponse;
import com.brinta.ems.service.UserGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-groups")
@RequiredArgsConstructor
public class UserGroupController {

    private final UserGroupService userGroupService;

    @PostMapping
    public ResponseEntity<GenericResponse> createUserGroup(@RequestBody UserGroupRequest request) {
        userGroupService.createUserGroup(request);
        return ResponseEntity.ok(new GenericResponse(true, "User group created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGroupResponse> getUserGroup(@PathVariable Long id) {
        return ResponseEntity.ok(userGroupService.getUserGroupById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse> updateUserGroup(@PathVariable Long id, @RequestBody UserGroupRequest request) {
        userGroupService.updateUserGroup(id, request);
        return ResponseEntity.ok(new GenericResponse(true, "User group updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> deleteUserGroup(@PathVariable Long id) {
        userGroupService.deleteUserGroup(id);
        return ResponseEntity.ok(new GenericResponse(true, "User group deleted successfully"));
    }

    @GetMapping
    public ResponseEntity<List<UserGroupResponse>> getAllUserGroups() {
        return ResponseEntity.ok(userGroupService.getAllUserGroups());
    }
}