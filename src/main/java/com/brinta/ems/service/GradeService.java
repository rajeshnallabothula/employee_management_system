package com.brinta.ems.service;

import com.brinta.ems.dto.GradeDto;
import com.brinta.ems.entity.Grade;

import java.util.List;

public interface GradeService {

   public Grade createGrade(GradeDto grade);

    public List<Grade> getAllGrades();

    public Grade getGradeById(Long id);

    public Grade updateGrade(Long id, GradeDto grade);

    public boolean deleteGrade(Long id);

}

