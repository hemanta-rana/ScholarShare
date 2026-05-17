package com.ScholarShare.dao;

import com.ScholarShare.entity.Flag;

public interface FlagDao {
    boolean createFlag(Flag flag);
    boolean hasStudentAlreadyFlagged(int resourceId, int studentId);
}