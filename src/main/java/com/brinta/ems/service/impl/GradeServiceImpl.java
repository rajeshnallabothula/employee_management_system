package com.brinta.ems.service.impl;


import com.brinta.ems.dto.GradeDto;
import com.brinta.ems.entity.Grade;
import com.brinta.ems.exception.exceptionHandler.ResourceNotFoundException;
import com.brinta.ems.repository.GradeRepository;
import com.brinta.ems.service.GradeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Override
    @Transactional
    public Grade createGrade(GradeDto gradeDto) {

        if (gradeDto.getId() != null) {
            throw new IllegalStateException("ID should not be set for new grade creation");
        }
        Grade grade=new Grade();
        // Bellow code also working, but it's boilerplate code so i replaced with BeanUtils.copyProperties same as mappers

        /*grade.setGradename(gradeDto.getGradename());
        grade.setGradeCode(gradeDto.getGradeCode());
        grade.setCreatedBy(gradeDto.getCreatedBy());*/

        // instead of using boilerplate code just use this line to inject the data in one object to another object
        grade.setCreatedAt(LocalDateTime.now());
        BeanUtils.copyProperties(gradeDto, grade);
        return gradeRepository.save(grade);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Grade> getAllGrades() {

        List<Grade> grades = gradeRepository.findAll();

        if (grades.isEmpty()) {
            throw new ResourceNotFoundException("No grades found");
        }

        return grades;
    }

    @Override
    @Transactional(readOnly = true)
    public Grade getGradeById(Long id) {

        return gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found with id: " + id));

    }

    @Override
    @Transactional
    public Grade updateGrade(Long id, GradeDto grade) {

        Grade existingGrade = getGradeById(id);
        if (grade.getGradeCode() != null) {
            existingGrade.setGradeCode(grade.getGradeCode());
        }

        if (grade.getGradename() != null) {
            existingGrade.setGradename(grade.getGradename());
        }

        if (grade.getUpdatedBy() != null) {
            existingGrade.setUpdatedBy(grade.getUpdatedBy());
        }

        existingGrade.setUpdatedAt(LocalDateTime.now());
        return gradeRepository.save(existingGrade);

    }

    @Override
    @Transactional
    public boolean deleteGrade(Long id) {

        Grade grade = getGradeById(id);
        gradeRepository.delete(grade);
        return true;

    }

}

