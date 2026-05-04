package com.ScholarShare.controller;

import com.ScholarShare.entity.User;
import com.ScholarShare.service.AdminService;
import com.ScholarShare.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.ScholarShare.dao.CategoryDao;
import com.ScholarShare.dao.daoImpl.CategoryDaoImpl;
import com.ScholarShare.entity.Faculty;
import com.ScholarShare.entity.Subject;
import com.ScholarShare.entity.Topic;

import java.io.IOException;
import java.util.List;
@WebServlet("/admin/*")
public class AdminServlet extends HomeServlet{
    private final AdminService adminService = new AdminService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        // Set all the aggregated and mapped data from the Service layer into the Request object
        request.setAttribute("stats", adminService.getDashboardStats());
        request.setAttribute("recentSubmissions", adminService.getRecentSubmissions());
        request.setAttribute("weeklyData", adminService.getWeeklyChartData());
        request.setAttribute("weeklyPeakDay", adminService.getWeeklyPeakDay());
        request.setAttribute("pendingRegs", adminService.getPendingRegistrations());
        request.setAttribute("recentFlags", adminService.getRecentFlags());

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            pathInfo = "/dashboard";
        }

        switch (pathInfo) {
            case "/dashboard":
                request.getRequestDispatcher("/WEB-INF/views/adminDashboard.jsp").forward(request, response);
                break;
            case "/flags":
                request.getRequestDispatcher("/WEB-INF/views/flags-management.jsp").forward(request, response);
                break;
            case "/categories":
                CategoryDao categoryDao = new CategoryDaoImpl();
                
                // Add stats
                request.setAttribute("facultyCount", categoryDao.getFacultyCount());
                request.setAttribute("subjectCount", categoryDao.getSubjectCount());
                request.setAttribute("topicCount", categoryDao.getTopicCount());
                
                // Fetch Faculties
                List<Faculty> faculties = categoryDao.getAllFaculties();
                request.setAttribute("faculties", faculties);
                
                // Selected faculty and Subjects
                String facultyIdStr = request.getParameter("facultyId");
                if (facultyIdStr != null && !facultyIdStr.isEmpty()) {
                    int facultyId = Integer.parseInt(facultyIdStr);
                    request.setAttribute("selectedFacultyId", facultyId);
                    
                    Faculty selectedFaculty = faculties.stream().filter(f -> f.getFacultyId() == facultyId).findFirst().orElse(null);
                    request.setAttribute("selectedFaculty", selectedFaculty);
                    
                    List<Subject> subjects = categoryDao.getSubjectsByFacultyId(facultyId);
                    request.setAttribute("subjects", subjects);
                    
                    // Selected subject and Topics
                    String subjectIdStr = request.getParameter("subjectId");
                    if (subjectIdStr != null && !subjectIdStr.isEmpty()) {
                        int subjectId = Integer.parseInt(subjectIdStr);
                        request.setAttribute("selectedSubjectId", subjectId);
                        
                        Subject selectedSubject = subjects.stream().filter(s -> s.getSubjectId() == subjectId).findFirst().orElse(null);
                        request.setAttribute("selectedSubject", selectedSubject);
                        
                        List<Topic> topics = categoryDao.getTopicsBySubjectId(subjectId);
                        request.setAttribute("topics", topics);
                    }
                }
                
                request.getRequestDispatcher("/WEB-INF/views/category-management.jsp").forward(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                break;
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if ("/categories".equals(pathInfo)) {
            handleCategoryPost(request, response);
        } else {
            super.doPost(request, response);
        }
    }

    private void handleCategoryPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        CategoryDao categoryDao = new CategoryDaoImpl();
        
        String redirectUrl = request.getContextPath() + "/admin/categories";
        
        if ("addFaculty".equals(action)) {
            String name = request.getParameter("facultyName");
            if (name != null && !name.trim().isEmpty()) {
                Faculty f = new Faculty();
                f.setFacultyName(name.trim());
                categoryDao.addFaculty(f);
            }
        } else if ("addSubject".equals(action)) {
            String name = request.getParameter("subjectName");
            String facultyIdStr = request.getParameter("facultyId");
            if (name != null && !name.trim().isEmpty() && facultyIdStr != null) {
                Subject s = new Subject();
                s.setSubjectName(name.trim());
                s.setFacultyId(Integer.parseInt(facultyIdStr));
                categoryDao.addSubject(s);
                redirectUrl += "?facultyId=" + facultyIdStr;
            }
        } else if ("addTopic".equals(action)) {
            String name = request.getParameter("topicName");
            String subjectIdStr = request.getParameter("subjectId");
            String facultyIdStr = request.getParameter("facultyId");
            if (name != null && !name.trim().isEmpty() && subjectIdStr != null) {
                Topic t = new Topic();
                t.setTopicName(name.trim());
                t.setSubjectId(Integer.parseInt(subjectIdStr));
                categoryDao.addTopic(t);
                redirectUrl += "?facultyId=" + facultyIdStr + "&subjectId=" + subjectIdStr;
            }
        } else if ("deleteFaculty".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                categoryDao.deleteFaculty(Integer.parseInt(idStr));
            }
        } else if ("deleteSubject".equals(action)) {
            String idStr = request.getParameter("id");
            String facultyIdStr = request.getParameter("facultyId");
            if (idStr != null) {
                categoryDao.deleteSubject(Integer.parseInt(idStr));
                redirectUrl += "?facultyId=" + facultyIdStr;
            }
        } else if ("deleteTopic".equals(action)) {
            String idStr = request.getParameter("id");
            String facultyIdStr = request.getParameter("facultyId");
            String subjectIdStr = request.getParameter("subjectId");
            if (idStr != null) {
                categoryDao.deleteTopic(Integer.parseInt(idStr));
                redirectUrl += "?facultyId=" + facultyIdStr + "&subjectId=" + subjectIdStr;
            }
        }
        
        response.sendRedirect(redirectUrl);
    }
}
