package com.ScholarShare.controller;

import com.ScholarShare.dao.AdminDao;
import com.ScholarShare.dao.CategoryDao;
import com.ScholarShare.dao.FlagDao;
import com.ScholarShare.dao.ModerationLogDao;
import com.ScholarShare.dao.daoImpl.AdminDaoImp;
import com.ScholarShare.dao.daoImpl.CategoryDaoImpl;
import com.ScholarShare.dao.daoImpl.FlagDaoImpl;
import com.ScholarShare.dao.daoImpl.ModerationLogDaoImpl;
import com.ScholarShare.entity.Faculty;
import com.ScholarShare.entity.Flag;
import com.ScholarShare.entity.ModerationLog;
import com.ScholarShare.entity.Subject;
import com.ScholarShare.entity.Topic;
import com.ScholarShare.entity.User;
import com.ScholarShare.service.AdminService;
import com.ScholarShare.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {

    private final AdminService adminService = new AdminService();
    private final AdminDao adminDao = new AdminDaoImp();
    private final FlagDao flagDao = new FlagDaoImpl();
    private final ModerationLogDao moderationLogDao = new ModerationLogDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) SessionUtil.getAttribute(request, "user");
        if (user == null || !user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        loadSharedDashboardAttributes(request);

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.isEmpty() || "/".equals(pathInfo)) {
            pathInfo = "/dashboard";
        }

        switch (pathInfo) {
            case "/dashboard" -> forward(request, response, "/WEB-INF/views/adminDashboard.jsp");
            case "/flags" -> {
                request.setAttribute("flags", adminService.getFlagsForManagement());
                forward(request, response, "/WEB-INF/views/flags-management.jsp");
            }
            case "/categories" -> {
                loadCategoryAttributes(request);
                forward(request, response, "/WEB-INF/views/category-management.jsp");
            }
            case "/user-approvals" -> {
                request.setAttribute("pendingApprovals", adminService.getPendingApprovals());
                forward(request, response, "/WEB-INF/views/admin-user-approvals.jsp");
            }
            case "/pipeline" -> {
                request.setAttribute("pipeline", adminService.getPipelineSubmissions());
                forward(request, response, "/WEB-INF/views/admin-pipeline.jsp");
            }
            case "/analytics" -> {
                request.setAttribute("analytics", adminService.getAnalyticsData());
                forward(request, response, "/WEB-INF/views/admin-analytics.jsp");
            }
            case "/audit" -> {
                request.setAttribute("auditLogs", adminService.getAuditLogs());
                forward(request, response, "/WEB-INF/views/moderation-audit-trail.jsp");
            }
            default -> response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) SessionUtil.getAttribute(request, "user");
        if (user == null || !user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            pathInfo = "";
        }

        switch (pathInfo) {
            case "/categories" -> handleCategoryPost(request, response);
            case "/moderate" -> handleModerationPost(request, response, user);
            case "/flags" -> handleFlagPost(request, response, user);
            case "/reviewRegistration" -> handleRegistrationPost(request, response);
            default -> response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        }
    }

    private void loadSharedDashboardAttributes(HttpServletRequest request) {
        request.setAttribute("stats", adminService.getDashboardStats());
        request.setAttribute("recentSubmissions", adminService.getRecentSubmissions());
        request.setAttribute("weeklyData", adminService.getWeeklyChartData());
        request.setAttribute("weeklyPeakDay", adminService.getWeeklyPeakDay());
        request.setAttribute("pendingRegs", adminService.getPendingRegistrations());
        request.setAttribute("recentFlags", adminService.getRecentFlags());
    }

    private void loadCategoryAttributes(HttpServletRequest request) {
        CategoryDao categoryDao = new CategoryDaoImpl();
        request.setAttribute("facultyCount", categoryDao.getFacultyCount());
        request.setAttribute("subjectCount", categoryDao.getSubjectCount());
        request.setAttribute("topicCount", categoryDao.getTopicCount());

        List<Faculty> faculties = categoryDao.getAllFaculties();
        request.setAttribute("faculties", faculties);

        String facultyIdStr = request.getParameter("facultyId");
        if (facultyIdStr != null && !facultyIdStr.isEmpty()) {
            int facultyId = Integer.parseInt(facultyIdStr);
            request.setAttribute("selectedFacultyId", facultyId);
            Faculty selectedFaculty = faculties.stream()
                    .filter(f -> f.getFacultyId() == facultyId).findFirst().orElse(null);
            request.setAttribute("selectedFaculty", selectedFaculty);

            List<Subject> subjects = categoryDao.getSubjectsByFacultyId(facultyId);
            request.setAttribute("subjects", subjects);

            String subjectIdStr = request.getParameter("subjectId");
            if (subjectIdStr != null && !subjectIdStr.isEmpty()) {
                int subjectId = Integer.parseInt(subjectIdStr);
                request.setAttribute("selectedSubjectId", subjectId);
                Subject selectedSubject = subjects.stream()
                        .filter(s -> s.getSubjectId() == subjectId).findFirst().orElse(null);
                request.setAttribute("selectedSubject", selectedSubject);
                request.setAttribute("topics", categoryDao.getTopicsBySubjectId(subjectId));
            }
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

    private void handleModerationPost(HttpServletRequest request, HttpServletResponse response, User admin)
            throws IOException {
        String action = request.getParameter("action");
        String resourceIdStr = request.getParameter("resourceId");
        String note = request.getParameter("note");

        if (action != null && resourceIdStr != null && !resourceIdStr.trim().isEmpty()) {
            try {
                int resourceId = Integer.parseInt(resourceIdStr);
                if ("approved".equals(action) || "rejected".equals(action)) {
                    adminDao.updateResourceStatus(resourceId, action);
                }
                ModerationLog log = new ModerationLog();
                log.setAdminId(admin.getUserId());
                log.setResourceId(resourceId);
                log.setAction(action);
                log.setNote(note != null ? note.trim() : "");
                moderationLogDao.addLog(log);
            } catch (NumberFormatException e) {
                System.out.println("Invalid resource id for moderation");
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/pipeline");
    }

    private void handleFlagPost(HttpServletRequest request, HttpServletResponse response, User admin)
            throws IOException {
        String flagIdStr = request.getParameter("flagId");
        String decision = request.getParameter("decision");
        String note = request.getParameter("note");

        if (flagIdStr != null && decision != null) {
            try {
                int flagId = Integer.parseInt(flagIdStr);
                Flag flag = flagDao.getById(flagId);
                if (flag != null) {
                    String status = "upheld".equals(decision) ? "upheld" : "dismissed";
                    flagDao.updateFlagStatus(flagId, status);

                    String logAction = "upheld".equals(decision) ? "flag_upheld" : "flag_dismissed";
                    if ("upheld".equals(decision)) {
                        adminDao.updateResourceStatus(flag.getResourceId(), "rejected");
                    }

                    ModerationLog log = new ModerationLog();
                    log.setAdminId(admin.getUserId());
                    log.setResourceId(flag.getResourceId());
                    log.setAction(logAction);
                    log.setNote(note != null ? note.trim() : "");
                    moderationLogDao.addLog(log);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid flag id");
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/flags");
    }

    private void handleRegistrationPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userIdStr = request.getParameter("userId");
        String decision = request.getParameter("decision");

        if (userIdStr != null && decision != null) {
            try {
                int userId = Integer.parseInt(userIdStr);
                if ("approve".equals(decision)) {
                    adminDao.updateUserStatus(userId, "active");
                } else if ("reject".equals(decision)) {
                    adminDao.updateUserStatus(userId, "suspended");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid user id for registration review");
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/user-approvals");
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String jsp)
            throws ServletException, IOException {
        request.getRequestDispatcher(jsp).forward(request, response);
    }
}
