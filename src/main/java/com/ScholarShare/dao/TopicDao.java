package com.ScholarShare.dao;

import com.ScholarShare.entity.Topic;
import java.util.List;

public interface TopicDao {
    List<Topic> getBySubject(int subjectId);
    Topic getById(int id);
}
