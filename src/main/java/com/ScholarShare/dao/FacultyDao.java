package com.ScholarShare.dao;

import com.ScholarShare.entity.Faculty;
import java.util.List;

public interface FacultyDao {
    List<Faculty> getAllFaculties();
    Faculty getById(int facultyId);

}