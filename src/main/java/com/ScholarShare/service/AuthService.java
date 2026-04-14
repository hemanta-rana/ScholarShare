package com.ScholarShare.service;
import com.ScholarShare.dao.daoImpl.UserDaoImpl;
import com.ScholarShare.entity.User;
import com.ScholarShare.util.ValidationUtil;
import com.ScholarShare.util.PasswordUtil;


public class AuthService {
    private static final UserDaoImpl userDao = new UserDaoImpl();

    //Register
    public String register(String fullName, String email, String phone, String password, String confirmPassword, Boolean pledgeAgreed) {

        if (ValidationUtil.isNullOrEmpty(email) ||
                ValidationUtil.isNullOrEmpty(password) ||
                ValidationUtil.isNullOrEmpty(fullName) ||
                ValidationUtil.isNullOrEmpty(phone) ||
                ValidationUtil.isNullOrEmpty(confirmPassword)) {
            return "All fields are required";
        }

        if (!ValidationUtil.isValidEmail(email))
            return "Please! enter a valid email address.";

        if (!ValidationUtil.isValidPhone(phone))
            return "Phone must be 10-15 digits and valid.";

        if (!ValidationUtil.isValidPassword(password))
            return "Password must be at least 8 characters and include uppercase,lowercase,number and special character.";

        if (!password.equals(confirmPassword))
            return "Passwords do not match.";

        if (!pledgeAgreed) {
            System.out.println("pledge agreed"+pledgeAgreed);
            return "You must agree to the Academic Integrity Pledge to register.";

        }

        if (userDao.getUserByEmail(email) != null)
            return "Email already exists.";


        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email.toLowerCase().trim());
        user.setPassword(PasswordUtil.getHashPassword(password));
        user.setPhone(phone.trim());

        user.setRole("student");
        user.setStatus("pending");
        user.setProfilePic(null);
        user.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

        boolean saved = userDao.addUser(user);

        if (saved) return null;
        else return "registration failed.";
    }

    // login
    public User login(String email, String password) {
        if (ValidationUtil.isNullOrEmpty(email) || ValidationUtil.isNullOrEmpty(password)) {
            return null;
        }
        User user = userDao.getUserByEmail(email.toLowerCase().trim());

        if (user == null) {
            return null;
        }
        if (!PasswordUtil.checkPassword(password, user.getPassword())) {
            return null;
        }

        return user;
    }

    // takes email and password as parameter. returns String used in the UI to show message
    public String getLoginErrorMessage(String email, String password) {
        if (email == null || email.isEmpty()) {
            return "Email is required";
        }
        if (password == null || password.isEmpty()) {
            return "Password is required";
        }
        return "Invalid email or password";
    }

}




