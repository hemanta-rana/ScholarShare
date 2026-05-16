package com.ScholarShare.dao.daoImpl;

import com.ScholarShare.dao.SubjectDao;
import com.ScholarShare.entity.Subject;
import com.ScholarShare.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubjectDaoImpl implements SubjectDao {
    @Override
    public List<Subject> getAllSubject(){
        List<Subject> subjects = new ArrayList<>();
        Connection conn = null;
        try{
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT s.*, f.faculty_name FROM subjects s " +
                    "JOIN faculties f ON s.faculty_id = f.faculty_id " +
                    "ORDER BY s.subject_name ASC";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                Subject subject = new Subject();
                subject.setSubjectId(rs.getInt("subject_id"));
                subject.setFacultyId(rs.getInt("faculty_id"));
                subject.setSubjectName(rs.getString("subject_name"));
                subject.setFacultyName(rs.getString("faculty_name"));
                subject.setCreatedAt(rs.getTimestamp("created_at"));
                subjects.add(subject);
            }
        } catch (SQLException e){
            System.out.println("Cannot get all subjects " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

}
