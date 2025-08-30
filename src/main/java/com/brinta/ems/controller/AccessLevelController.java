package com.brinta.ems.controller;

import com.brinta.ems.request.registerRequest.AccessLevelRequest;
import com.brinta.ems.response.AccessLevelResponse;
import com.brinta.ems.service.AccessLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/access-levels")
@RequiredArgsConstructor
public class AccessLevelController {
    private final AccessLevelService accessLevelService;

    @PostMapping
    public ResponseEntity<AccessLevelResponse> create(@RequestBody AccessLevelRequest request) {
        return ResponseEntity.ok(accessLevelService.create(request));
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<AccessLevelResponse>> getAll(@PathVariable Long tenantId) {
        return ResponseEntity.ok(accessLevelService.getAll(tenantId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccessLevelResponse> update(@PathVariable Long id, @RequestBody AccessLevelRequest request) {
        return ResponseEntity.ok(accessLevelService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accessLevelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}