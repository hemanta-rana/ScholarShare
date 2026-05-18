package com.ScholarShare.dao;

import com.ScholarShare.entity.Subject;
import java.util.List;

public interface SubjectDao {
    List<Subject> getAllSubject();
    List<Subject> getByFaculty(int facultyId);
    Subject getById(int subjectId);
}