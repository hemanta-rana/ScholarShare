package com.ScholarShare.dao.daoImpl;

import com.ScholarShare.dao.FacultyDao;
import com.ScholarShare.entity.Faculty;
import com.ScholarShare.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FacultyDaoImpl implements FacultyDao {

    @Override
    public List<Faculty> getAllFaculties() {
        List<Faculty> faculties = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM faculties ORDER BY faculty_name ASC";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Faculty faculty = new Faculty();
                faculty.setFacultyId(rs.getInt("faculty_id"));
                faculty.setFacultyName(rs.getString("faculty_name"));
                faculty.setCreatedAt(rs.getTimestamp("created_at"));
                faculties.add(faculty);
            }
        } catch (SQLException e){
            System.out.println("Cannot get all faculties "+e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return faculties;
    }

}