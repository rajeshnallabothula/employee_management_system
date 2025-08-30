package com.brinta.ems.service;

import com.brinta.ems.dto.LevelDTO;
import com.brinta.ems.entity.Level;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LevelService {

    public Level createLevel(LevelDTO levelDTO);

    public List<Level> getAllLevels();

    public Level updateLevel(Long id, LevelDTO levelDTO);

    public ResponseEntity<?> deleteLevel(Long id);

    boolean existsById(Long id);

}

