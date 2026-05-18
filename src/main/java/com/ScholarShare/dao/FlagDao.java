package com.ScholarShare.dao;

import com.ScholarShare.entity.Flag;

public interface FlagDao {
    boolean createFlag(Flag flag);
    boolean hasStudentAlreadyFlagged(int resourceId, int studentId);
    java.util.List<Flag> getAllFlags();
    boolean updateFlagStatus(int flagId, String status);
    Flag getById(int flagId);
}