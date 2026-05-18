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
    public List<Topic> getBySubject(int subjectId){
        List<Topic> topics = new ArrayList<>();
        Connection conn = null;
        try{
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT t.*, s.subject_name, f.faculty_name FROM topics t " +
                    "JOIN subjects s ON t.subject_id = s.subject_id " +
                    "JOIN faculties f ON s.faculty_id = f.faculty_id " +
                    "WHERE t.subject_id = ? ORDER BY t.topic_name ASC";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, subjectId);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                Topic topic = new Topic();
                topic.setTopicId(rs.getInt("topic_id"));
                topic.setTopicName(rs.getString("topic_name"));
                topic.setSubjectId(rs.getInt("subject_id"));
                topic.setSubjectName(rs.getString("subject_name"));
                topic.setFacultyName(rs.getString("faculty_name"));
                topic.setCreatedAt(rs.getTimestamp("created_at"));
                topics.add(topic);
            }
        } catch (SQLException e){
            System.out.println("Cannot get topics by subject id " + subjectId + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return topics;
    }

    @Override
    public Topic getById(int topicId) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT t.*, s.subject_name, f.faculty_name FROM topics t " +
                    "JOIN subjects s ON t.subject_id = s.subject_id " +
                    "JOIN faculties f ON s.faculty_id = f.faculty_id " +
                    "WHERE t.topic_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, topicId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Topic topic = new Topic();
                topic.setTopicId(rs.getInt("topic_id"));
                topic.setTopicName(rs.getString("topic_name"));
                topic.setSubjectId(rs.getInt("subject_id"));
                topic.setSubjectName(rs.getString("subject_name"));
                topic.setFacultyName(rs.getString("faculty_name"));
                topic.setCreatedAt(rs.getTimestamp("created_at"));
                return topic;
            }
        } catch (SQLException e) {
            System.out.println("Cannot get topic by id " + topicId + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return null;
    }

    @Override
    public List<Topic> getAllTopics() {
        List<Topic> topics = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT t.*, s.subject_name, f.faculty_name FROM topics t " +
                    "JOIN subjects s ON t.subject_id = s.subject_id " +
                    "JOIN faculties f ON s.faculty_id = f.faculty_id " +
                    "ORDER BY f.faculty_name ASC, s.subject_name ASC, t.topic_name ASC";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Topic topic = new Topic();
                topic.setTopicId(rs.getInt("topic_id"));
                topic.setTopicName(rs.getString("topic_name"));
                topic.setSubjectId(rs.getInt("subject_id"));
                topic.setSubjectName(rs.getString("subject_name"));
                topic.setFacultyName(rs.getString("faculty_name"));
                topic.setCreatedAt(rs.getTimestamp("created_at"));
                topics.add(topic);
            }
        } catch (SQLException e) {
            System.out.println("Cannot get all topics " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return topics;
    }
}
