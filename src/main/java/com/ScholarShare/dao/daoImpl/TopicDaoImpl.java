package com.ScholarShare.dao.daoImpl;

import com.ScholarShare.dao.TopicDao;
import com.ScholarShare.entity.Topic;
import com.ScholarShare.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TopicDaoImpl implements TopicDao {

    @Override
    public List<Topic> getAllTopics() {
        List<Topic> topics = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT t.topic_id, t.topic_name, s.subject_name, f.faculty_name " +
                    "FROM topics t " +
                    "JOIN subjects s ON t.subject_id = s.subject_id " +
                    "JOIN faculties f ON s.faculty_id = f.faculty_id " +
                    "ORDER BY f.faculty_name, s.subject_name, t.topic_name";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Topic topic = new Topic();
                topic.setTopicId(rs.getInt("topic_id"));
                topic.setTopicName(rs.getString("topic_name"));
                topic.setSubjectName(rs.getString("subject_name"));
                topic.setFacultyName(rs.getString("faculty_name"));
                topics.add(topic);
            }
        } catch (SQLException e) {
            System.out.println("getAllTopics failed " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return topics;
    }
}
