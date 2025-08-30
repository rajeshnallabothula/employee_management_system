package com.brinta.ems.service.impl;


import com.brinta.ems.dto.LevelDTO;
import com.brinta.ems.entity.Level;
import com.brinta.ems.exception.exceptionHandler.InvalidLevelException;
import com.brinta.ems.exception.exceptionHandler.LevelNotFoundException;
import com.brinta.ems.repository.LevelsRepository;
import com.brinta.ems.service.LevelService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LevelsServiceImpl implements LevelService {

    @Autowired
    private LevelsRepository levelRepository;

    @Transactional
    @Override
    public Level createLevel(LevelDTO levelDTO) {
        Level level = new Level();
        level.setCreatedAt(LocalDateTime.now());
        BeanUtils.copyProperties(levelDTO, level);
        return levelRepository.save(level);
    }

    @Override
    public List<Level> getAllLevels() {
        return levelRepository.findAll();
    }

    @Transactional
    @Override
    public Level updateLevel(Long id, LevelDTO levelDTO) {
        Optional<Level> existingLevel = levelRepository.findById(id);
        if (!existingLevel.isPresent()) {
            throw new LevelNotFoundException("Level with id " + id + " not found.");
        }
        Level levelToUpdate = existingLevel.get();
        if (levelDTO.getName() != null && !levelDTO.getName().isEmpty()) {
            levelToUpdate.setName(levelDTO.getName());
        }
        if (levelDTO.getDescription() != null) {
            levelToUpdate.setDescription(levelDTO.getDescription());
        }
        levelToUpdate.setUpdatedAt(LocalDateTime.now());
        levelToUpdate.setUpdatedBy(levelDTO.getUpdatedBy());
        return levelRepository.save(levelToUpdate);
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteLevel(Long id) {
        // Validate the ID
        if (id == null || id <= 0) {
            throw new InvalidLevelException("Invalid level ID: " + id);
        }

        // Check if the level exists
        if (!existsById(id)) {
            throw new LevelNotFoundException("Level with id " + id + " not found.");
        }

        levelRepository.deleteById(id);
        return new ResponseEntity<>("Level deleted successfully", HttpStatus.NO_CONTENT);
    }

    @Override
    public boolean existsById(Long id) {
        // Use the repository method to check if a Level exists
        return levelRepository.existsById(id);
    }

}

