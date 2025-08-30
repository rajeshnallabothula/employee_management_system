package com.brinta.ems.controller;


import com.brinta.ems.dto.LevelDTO;
import com.brinta.ems.entity.Level;
import com.brinta.ems.exception.exceptionHandler.InvalidLevelException;
import com.brinta.ems.service.LevelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/1.0/level")
public class LevelController {

    @Autowired
    private LevelService levelService;

    @PostMapping("/create")
    public ResponseEntity<?> createLevel(@Valid @RequestBody LevelDTO levelDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Level createdLevel = levelService.createLevel(levelDTO);
        return new ResponseEntity<>(createdLevel, HttpStatus.CREATED);
    }

    @PutMapping("updateById/{id}")
    public ResponseEntity<?> updateLevel(@PathVariable Long id, @Valid @RequestBody LevelDTO level, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Level updatedLevel = levelService.updateLevel(id, level);
        return new ResponseEntity<>(updatedLevel, HttpStatus.OK);
    }

    @DeleteMapping("deleteById/{id}")
    public ResponseEntity<?> deleteLevel(@PathVariable String id) {
        try {
            Long levelId = Long.parseLong(id);

            if (levelId <= 0) {
                throw new InvalidLevelException("Invalid level ID: " + id);
            }

            return levelService.deleteLevel(levelId);
        } catch (NumberFormatException e) {
            throw new InvalidLevelException("Invalid level ID. Please provide a numeric value.");
        }
    }

    @GetMapping("/getAllLevels")
    public ResponseEntity<List<Level>> getAllLevels() {
        List<Level> levels = levelService.getAllLevels();
        return new ResponseEntity<>(levels, HttpStatus.OK);
    }

}

