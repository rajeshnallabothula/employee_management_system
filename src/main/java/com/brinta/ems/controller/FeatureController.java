package com.brinta.ems.controller;


import com.brinta.ems.entity.Feature;
import com.brinta.ems.request.registerRequest.FeatureRequest;
import com.brinta.ems.response.FeatureResponse;
import com.brinta.ems.response.GenericResponse;
import com.brinta.ems.service.FeatureService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/1.0/feature")
public class FeatureController {

    @Autowired
    private FeatureService featureService;

    /*@GetMapping("getAllFeatures")
    public ResponseEntity<?> getAllFeatures() {
        return ResponseEntity.ok(featureService.getAllFeatures());
    }

    @PostMapping("createFeature")
    public ResponseEntity<GenericResponse> createFeature(@Valid @RequestBody FeatureRequest featureRequest) {
        featureService.createFeature(featureRequest);
        return new ResponseEntity<>(new GenericResponse(true, "Feature created successfully"), HttpStatus.CREATED);
    }

    @PutMapping("updateFeature/{id}")
    public Feature updateFeature(@PathVariable Long id, @Valid @RequestBody FeatureRequest featureRequest) {

        return featureService.updateFeature(id, featureRequest);
    }

    @DeleteMapping("/features/{id}")
    public ResponseEntity<GenericResponse> deleteFeature(@PathVariable Long id) {
        featureService.delete(id);
        return ResponseEntity.ok(new GenericResponse(true, "Feature deleted successfully"));
    }
*/
    @PostMapping
    public ResponseEntity<FeatureResponse> create(@RequestBody FeatureRequest request) {
        return ResponseEntity.ok(featureService.create(request));
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<FeatureResponse>> getAll(@PathVariable Long tenantId) {
        return ResponseEntity.ok(featureService.getAll(tenantId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeatureResponse> update(@PathVariable Long id, @RequestBody FeatureRequest request) {
        return ResponseEntity.ok(featureService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        featureService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

