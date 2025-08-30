package com.brinta.ems.controller;


import com.brinta.ems.dto.GradeDto;
import com.brinta.ems.entity.Grade;
import com.brinta.ems.service.GradeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("1.0/grade")
@Validated
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @PostMapping("create")
    public ResponseEntity<Grade> createGrade(@Valid @RequestBody GradeDto grade) {
        try {
            Grade createdGrade = gradeService.createGrade(grade);
            return new ResponseEntity<>(createdGrade, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create grade: " + e.getMessage());
        }
    }

    @GetMapping("getAllGrades")
    public ResponseEntity<List<Grade>> getAllGrades() {
        List<Grade> grades = gradeService.getAllGrades();
        return ResponseEntity.ok(grades);
    }

    @GetMapping("getGradeById/{id}")
    public ResponseEntity<Grade> getGradeById(
            @PathVariable @Min(value = 1, message = "ID must be greater than 0") Long id) {
        try {
            Grade grade = gradeService.getGradeById(id);
            return ResponseEntity.ok(grade);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to fetch grade: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Grade> updateGrade(
            @PathVariable @Min(value = 1, message = "ID must be greater than 0") Long id,
            @Valid @RequestBody GradeDto grade) {
        try {
            Grade updatedGrade = gradeService.updateGrade(id, grade);
            return ResponseEntity.ok(updatedGrade);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to update grade: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(
            @PathVariable @Min(value = 1, message = "ID must be greater than 0") Long id) {
        try {
            boolean isDeleted = gradeService.deleteGrade(id);
            return isDeleted ? ResponseEntity.noContent().build() :
                    ResponseEntity.notFound().build();
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to delete grade: " + e.getMessage());
        }
    }

}

