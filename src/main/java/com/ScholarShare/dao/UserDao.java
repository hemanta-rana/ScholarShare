package com.ScholarShare.dao;

import com.ScholarShare.entity.User;

import java.util.List;

public interface UserDao {
    User getUser(int userId);
    boolean addUser(User user);
    boolean updateUser(User user,  int userId);
    User getUserByEmail(String email);
    boolean deleteUser(int userId);
    List<User> getAllStudents();

    boolean save(com.ScholarShare.entity.User user);
}
