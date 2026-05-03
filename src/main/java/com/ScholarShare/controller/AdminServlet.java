package com.ScholarShare.controller;

import com.ScholarShare.entity.User;
import com.ScholarShare.service.AdminService;
import com.ScholarShare.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/adminDashboard")
public class AdminServlet extends HomeServlet{
    private final AdminService adminService = new AdminService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        // Set all the aggregated and mapped data from the Service layer into the Request object
        // This makes the data available to the JSP via Expression Language (EL), e.g., ${stats.pendingRegistrations}
        request.setAttribute("stats", adminService.getDashboardStats());
        request.setAttribute("recentSubmissions", adminService.getRecentSubmissions());
        request.setAttribute("weeklyData", adminService.getWeeklyChartData());
        request.setAttribute("weeklyPeakDay", adminService.getWeeklyPeakDay());
        request.setAttribute("pendingRegs", adminService.getPendingRegistrations());
        request.setAttribute("recentFlags", adminService.getRecentFlags());


        request.getRequestDispatcher("WEB-INF/views/adminDashboard.jsp").forward(request, response);

    }


}
