package com.ScholarShare.controller;

import com.ScholarShare.dao.daoImpl.FacultyDaoImpl;
import com.ScholarShare.dao.daoImpl.ResourceDaoImpl;
import com.ScholarShare.dao.daoImpl.SubjectDaoImpl;
import com.ScholarShare.dao.daoImpl.TopicDaoImpl;
import com.ScholarShare.entity.Faculty;
import com.ScholarShare.entity.Resource;
import com.ScholarShare.entity.Subject;
import com.ScholarShare.entity.Topic;
import com.ScholarShare.entity.User;
import com.ScholarShare.service.StudentDashboardService;
import com.ScholarShare.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/browser")
public class BrowserServlet extends HttpServlet {

    private final StudentDashboardService dashboardService = new StudentDashboardService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        int facultyId = 0, subjectId = 0, topicId = 0;
        try { facultyId = Integer.parseInt(request.getParameter("facultyId")); } catch (Exception ignored) {}
        try { subjectId = Integer.parseInt(request.getParameter("subjectId")); } catch (Exception ignored) {}
        try { topicId   = Integer.parseInt(request.getParameter("topicId"));   } catch (Exception ignored) {}
        String q = request.getParameter("q");

        FacultyDaoImpl facultyDao = new FacultyDaoImpl();
        SubjectDaoImpl subjectDao = new SubjectDaoImpl();
        TopicDaoImpl   topicDao   = new TopicDaoImpl();
        ResourceDaoImpl resourceDao = new ResourceDaoImpl();

        List<Faculty> faculties = facultyDao.getAllFaculties();

        List<Resource> resources;
        List<Subject>  subjects  = null;
        List<Topic>    topics    = null;

        if (topicId > 0) {
            resources = resourceDao.getByTopic(topicId);
            subjects  = subjectDao.getByFaculty(facultyId);
            topics    = topicDao.getBySubject(subjectId);
        } else if (subjectId > 0) {
            resources = resourceDao.getBySubject(subjectId);
            topics    = topicDao.getBySubject(subjectId);
        } else if (facultyId > 0) {
            resources = resourceDao.getByFaculty(facultyId);
            subjects  = subjectDao.getByFaculty(facultyId);
        } else if (q != null && !q.trim().isEmpty()) {
            resources = resourceDao.search(q.trim());
        } else {
            resources = resourceDao.getAllApproved();
        }

        request.setAttribute("faculties",         faculties);
        request.setAttribute("resources",         resources);
        request.setAttribute("subjects",          subjects);
        request.setAttribute("topics",            topics);
        request.setAttribute("selectedFacultyId", facultyId);
        request.setAttribute("selectedSubjectId", subjectId);
        request.setAttribute("selectedTopicId",   topicId);
        request.setAttribute("keyword",           q);

        // Inject profile for the student header
        User user = (User) SessionUtil.getAttribute(request, "user");
        if (user != null) {
            request.setAttribute("profile", dashboardService.getProfile(user.getUserId(), request.getContextPath()));
        }

        request.getRequestDispatcher("/WEB-INF/views/search-results.jsp").forward(request, response);
    }
}
