package com.ScholarShare.dao.daoImpl;

import com.ScholarShare.dao.CategoryDao;
import com.ScholarShare.entity.Faculty;
import com.ScholarShare.entity.Subject;
import com.ScholarShare.entity.Topic;
import com.ScholarShare.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the CategoryDao interface.
 * Handles database operations for Faculties, Subjects, and Topics.
 */
public class CategoryDaoImpl implements CategoryDao {

    // ==========================================
    // Faculty Operations
    // ==========================================

    @Override
    public List<Faculty> getAllFaculties() {
        List<Faculty> faculties = new ArrayList<>();
        String sql = "SELECT * FROM faculties ORDER BY faculty_name ASC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Faculty faculty = new Faculty();
                faculty.setFacultyId(rs.getInt("faculty_id"));
                faculty.setFacultyName(rs.getString("faculty_name"));
                faculty.setCreatedAt(rs.getTimestamp("created_at"));
                faculties.add(faculty);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return faculties;
    }

    @Override
    public int getFacultyCount() {
        String sql = "SELECT COUNT(*) FROM faculties";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean addFaculty(Faculty faculty) {
        String sql = "INSERT INTO faculties (faculty_name) VALUES (?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, faculty.getFacultyName());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateFaculty(Faculty faculty) {
        String sql = "UPDATE faculties SET faculty_name = ? WHERE faculty_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, faculty.getFacultyName());
            stmt.setInt(2, faculty.getFacultyId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteFaculty(int facultyId) {
        String sql = "DELETE FROM faculties WHERE faculty_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, facultyId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==========================================
    // Subject Operations
    // ==========================================

    @Override
    public List<Subject> getSubjectsByFacultyId(int facultyId) {
        List<Subject> subjects = new ArrayList<>();
        // Joining with faculties to optionally populate the faculty name, though we already have facultyId
        String sql = "SELECT s.*, f.faculty_name FROM subjects s " +
                     "JOIN faculties f ON s.faculty_id = f.faculty_id " +
                     "WHERE s.faculty_id = ? ORDER BY s.subject_name ASC";
                     
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, facultyId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setSubjectId(rs.getInt("subject_id"));
                    subject.setFacultyId(rs.getInt("faculty_id"));
                    subject.setSubjectName(rs.getString("subject_name"));
                    subject.setFacultyName(rs.getString("faculty_name")); // Populating joined field
                    subject.setCreatedAt(rs.getTimestamp("created_at"));
                    subjects.add(subject);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    @Override
    public int getSubjectCount() {
        String sql = "SELECT COUNT(*) FROM subjects";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean addSubject(Subject subject) {
        String sql = "INSERT INTO subjects (faculty_id, subject_name) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, subject.getFacultyId());
            stmt.setString(2, subject.getSubjectName());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateSubject(Subject subject) {
        String sql = "UPDATE subjects SET subject_name = ?, faculty_id = ? WHERE subject_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, subject.getSubjectName());
            stmt.setInt(2, subject.getFacultyId());
            stmt.setInt(3, subject.getSubjectId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteSubject(int subjectId) {
        String sql = "DELETE FROM subjects WHERE subject_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, subjectId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==========================================
    // Topic Operations
    // ==========================================

    @Override
    public List<Topic> getTopicsBySubjectId(int subjectId) {
        List<Topic> topics = new ArrayList<>();
        // Join with subjects and faculties to populate full hierarchy names
        String sql = "SELECT t.*, s.subject_name, f.faculty_name " +
                     "FROM topics t " +
                     "JOIN subjects s ON t.subject_id = s.subject_id " +
                     "JOIN faculties f ON s.faculty_id = f.faculty_id " +
                     "WHERE t.subject_id = ? ORDER BY t.topic_name ASC";
                     
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, subjectId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Topic topic = new Topic();
                    topic.setTopicId(rs.getInt("topic_id"));
                    topic.setSubjectId(rs.getInt("subject_id"));
                    topic.setTopicName(rs.getString("topic_name"));
                    topic.setSubjectName(rs.getString("subject_name")); // Populating joined field
                    topic.setFacultyName(rs.getString("faculty_name")); // Populating joined field
                    topic.setCreatedAt(rs.getTimestamp("created_at"));
                    topics.add(topic);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topics;
    }

    @Override
    public int getTopicCount() {
        String sql = "SELECT COUNT(*) FROM topics";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean addTopic(Topic topic) {
        String sql = "INSERT INTO topics (subject_id, topic_name) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, topic.getSubjectId());
            stmt.setString(2, topic.getTopicName());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateTopic(Topic topic) {
        String sql = "UPDATE topics SET topic_name = ?, subject_id = ? WHERE topic_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, topic.getTopicName());
            stmt.setInt(2, topic.getSubjectId());
            stmt.setInt(3, topic.getTopicId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteTopic(int topicId) {
        String sql = "DELETE FROM topics WHERE topic_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, topicId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
