package com.ScholarShare.service;

import com.ScholarShare.dao.AdminDao;
import com.ScholarShare.dao.daoImpl.AdminDaoImp;
import com.ScholarShare.entity.Flag;
import com.ScholarShare.entity.Resource;
import com.ScholarShare.entity.User;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Service class for the Admin Dashboard.
 * This class acts as the bridge between the data access object (DAO) and the Servlet controller.
 * It fetches raw data from the database and maps it into clean, presentation-ready Data Transfer Objects (DTOs)
 * in the form of Maps, which the JSP can easily render using Expression Language (EL).
 */
public class AdminService {
    private final AdminDao adminDao = new AdminDaoImp();

    /**
     * Fetches top-level statistics for the dashboard header cards.
     * 
     * @return A map containing aggregated numeric statistics.
     */
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Count of users with 'student' role and 'pending' status
        stats.put("pendingRegistrations", adminDao.getNumberOfPendingRegistration());
        
        // Count of resources with 'pending' status waiting for moderation
        stats.put("pendingSubmissions", adminDao.getPendingSubmissions());
        
        // Count of community flags that haven't been resolved yet
        stats.put("openFlags", adminDao.getNumberOfOpenFlags());
        
        // Total number of resources in the system
        stats.put("totalResources", adminDao.getTotalResources());
        
        // Percentage of total resources that are 'approved'
        stats.put("approvalRate", adminDao.getApprovalRate());
        
        return stats;
    }

    /**
     * Fetches the 5 most recent resource submissions and maps them for the JSP.
     * Extracts joined fields (submitter name and topic category) and formats dates.
     * Also maps the database ENUM status to a CSS class for styling.
     * 
     * @return A list of mapped resources.
     */
    public List<Map<String, Object>> getRecentSubmissions() {
        List<Resource> resources = adminDao.getRecentSubmission();
        List<Map<String, Object>> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        
        for (Resource r : resources) {
            Map<String, Object> map = new HashMap<>();
            
            // Basic resource details
            map.put("title", r.getTitle());
            map.put("type", r.getResourceType() != null ? r.getResourceType() : "Document");
            map.put("submissionDate", r.getUploadDate() != null ? sdf.format(r.getUploadDate()) : "Unknown");
            
            // These fields come from the JOIN query in AdminDaoImp
            map.put("submitterName", r.getSubmitterName() != null ? r.getSubmitterName() : "Unknown Submitter");
            map.put("category", r.getTopicName() != null ? r.getTopicName() : "Uncategorized");
            
            // Format status for display (e.g., 'under_review' -> 'Under review')
            String rawStatus = r.getStatus() != null ? r.getStatus() : "pending";
            String displayStatus = rawStatus.replace("_", " ");
            displayStatus = displayStatus.substring(0, 1).toUpperCase() + displayStatus.substring(1);
            map.put("status", displayStatus);
            
            // Determine the CSS class based on the database status
            String statusClass = "pending"; // Default
            if (rawStatus.equals("approved")) {
                statusClass = "approved";
            } else if (rawStatus.equals("rejected")) {
                statusClass = "rejected";
            } else if (rawStatus.equals("under_review")) {
                statusClass = "review";
            }
            map.put("statusClass", statusClass);
            
            result.add(map);
        }
        return result;
    }

    /**
     * Fetches the latest pending user registrations.
     * Dynamically calculates user initials based on their full name since initials aren't stored in the DB.
     * 
     * @return A list of mapped user objects.
     */
    public List<Map<String, Object>> getPendingRegistrations() {
        List<User> users = adminDao.getPendingUserRegistration();
        List<Map<String, Object>> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        
        for (User u : users) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", u.getUserId());
            map.put("fullName", u.getFullName());
            
            // Calculate initials (e.g., "Hemanta Rana" -> "HR")
            String initials = "U";
            if (u.getFullName() != null && !u.getFullName().trim().isEmpty()) {
                String[] parts = u.getFullName().trim().split("\\s+");
                initials = String.valueOf(parts[0].charAt(0));
                if (parts.length > 1) {
                    initials += parts[parts.length - 1].charAt(0);
                }
            }
            map.put("initials", initials.toUpperCase());
            
            // Using Role instead of Department since users table has no department column
            String role = u.getRole() != null ? u.getRole().substring(0, 1).toUpperCase() + u.getRole().substring(1) : "Student";
            map.put("department", role); 
            
            map.put("requestedOn", u.getCreatedAt() != null ? sdf.format(u.getCreatedAt()) : "Unknown");
            result.add(map);
        }
        return result;
    }

    /**
     * Fetches the latest open flags reported by the community.
     * Relies on the JOIN query to fetch the real resource title and the name of the person who flagged it.
     * 
     * @return A list of mapped flags.
     */
    public List<Map<String, Object>> getRecentFlags() {
        List<Flag> flags = adminDao.getRecentFlags();
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Flag f : flags) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", f.getFlagId());
            map.put("reason", f.getReason());
            
            // Joined fields
            map.put("resourceTitle", f.getResourceTitle() != null ? f.getResourceTitle() : "Unknown Resource");
            map.put("flaggedBy", f.getFlaggedByName() != null ? f.getFlaggedByName() : "Unknown User");
            
            result.add(map);
        }
        return result;
    }

    /**
     * Constructs the data needed to render the "Submissions This Week" bar chart.
     * It maps the raw database grouped counts into a 7-day array structure expected by the frontend CSS.
     * 
     * @return A list of chart data points (label, height, active flag).
     */
    public List<Map<String, Object>> getWeeklyChartData() {
        // Get the grouped counts from the database for the last 7 days
        Map<String, Integer> rawData = adminDao.getWeeklySubmissionCounts();
        List<Map<String, Object>> chartData = new ArrayList<>();
        
        // Find the maximum submissions in a single day to normalize the chart height (max 100px)
        int maxCount = 1;
        for (Integer count : rawData.values()) {
            if (count > maxCount) maxCount = count;
        }

        // Build the last 7 days list to ensure we show days even if they have 0 submissions
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            String dbDateStr = date.toString(); // format: YYYY-MM-DD
            
            int count = rawData.getOrDefault(dbDateStr, 0);
            
            // Calculate height relative to max, assume max height is 100 pixels
            int height = (int) (((double) count / maxCount) * 100);
            if (height < 5 && count > 0) height = 5; // minimum visible height if > 0
            
            // Get day label e.g., "Mon"
            String dayLabel = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            
            Map<String, Object> point = new HashMap<>();
            point.put("label", dayLabel);
            point.put("height", height);
            point.put("active", i == 0); // Mark 'today' as active
            
            chartData.add(point);
        }
        
        return chartData;
    }

    /**
     * Determines which day of the week had the peak submissions.
     * Used for the chart footer text.
     */
    public String getWeeklyPeakDay() {
        Map<String, Integer> rawData = adminDao.getWeeklySubmissionCounts();
        int maxCount = -1;
        String peakDate = null;
        
        for (Map.Entry<String, Integer> entry : rawData.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                peakDate = entry.getKey();
            }
        }
        
        if (peakDate == null) return "None";
        
        // Parse "YYYY-MM-DD" to Day of Week
        LocalDate date = LocalDate.parse(peakDate);
        return date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

    public List<Map<String, Object>> getPendingApprovals() {
        List<User> users = adminDao.getAllPendingRegistrations();
        List<Map<String, Object>> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        for (User u : users) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", u.getUserId());
            map.put("fullName", u.getFullName());
            map.put("email", u.getEmail());
            map.put("phone", u.getPhone());
            map.put("initials", buildInitials(u.getFullName()));
            map.put("requestedOn", u.getCreatedAt() != null ? sdf.format(u.getCreatedAt()) : "Unknown");
            result.add(map);
        }
        return result;
    }

    public List<Map<String, Object>> getPipelineSubmissions() {
        List<Resource> resources = adminDao.getPipelineResources();
        List<Map<String, Object>> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        for (Resource r : resources) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", r.getResourceId());
            map.put("title", r.getTitle());
            map.put("type", r.getResourceType());
            map.put("submitterName", r.getSubmitterName());
            map.put("category", r.getTopicName());
            map.put("subject", r.getSubjectName());
            map.put("submissionDate", r.getUploadDate() != null ? sdf.format(r.getUploadDate()) : "Unknown");
            map.put("status", r.getStatus());
            map.put("statusClass", statusClassFor(r.getStatus()));
            result.add(map);
        }
        return result;
    }

    public List<Map<String, Object>> getFlagsForManagement() {
        List<Flag> flags = adminDao.getAllFlags();
        List<Map<String, Object>> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        for (Flag f : flags) {
            Map<String, Object> map = new HashMap<>();
            String status = f.getStatus() != null ? f.getStatus() : "open";
            map.put("id", f.getFlagId());
            map.put("resourceId", f.getResourceId());
            map.put("resourceTitle", f.getResourceTitle() != null ? f.getResourceTitle() : "Unknown");
            map.put("resourceType", "Resource");
            map.put("resourceCategory", "General");
            map.put("reason", f.getReason());
            map.put("description", f.getReason());
            map.put("status", status.toUpperCase(Locale.ENGLISH));
            map.put("flaggedDate", f.getCreatedAt() != null ? sdf.format(f.getCreatedAt()) : "");
            map.put("reporterName", f.getFlaggedByName() != null ? f.getFlaggedByName() : "Student");
            map.put("reporterInitials", buildInitials(f.getFlaggedByName()));
            map.put("submitterName", "Uploader");
            map.put("reportContext", "Community report");
            result.add(map);
        }
        return result;
    }

    public List<Map<String, Object>> getAuditLogs() {
        com.ScholarShare.dao.ModerationLogDao logDao = new com.ScholarShare.dao.daoImpl.ModerationLogDaoImpl();
        List<com.ScholarShare.entity.ModerationLog> logs = logDao.getAllLogs();
        List<Map<String, Object>> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' HH:mm");
        for (com.ScholarShare.entity.ModerationLog log : logs) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", log.getLogId());
            map.put("action", log.getAction());
            map.put("actionClass", actionClassFor(log.getAction()));
            map.put("resourceTitle", log.getResourceTitle() != null ? log.getResourceTitle() : "Resource #" + log.getResourceId());
            map.put("adminName", log.getAdminName() != null ? log.getAdminName() : "Admin");
            map.put("adminInitials", buildInitials(log.getAdminName()));
            map.put("note", log.getNote() != null ? log.getNote() : "");
            map.put("actionedAt", log.getActionAt() != null ? sdf.format(log.getActionAt()) : "");
            result.add(map);
        }
        return result;
    }

    public Map<String, Object> getAnalyticsData() {
        Map<String, Object> data = new HashMap<>();
        data.put("stats", getDashboardStats());
        data.put("weeklyData", getWeeklyChartData());
        data.put("weeklyPeakDay", getWeeklyPeakDay());
        data.put("topContributors", adminDao.getTopContributors(5));
        data.put("mostFlagged", adminDao.getMostFlaggedResources(5));
        return data;
    }

    private String buildInitials(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "U";
        }
        String[] parts = fullName.trim().split("\\s+");
        String initials = String.valueOf(parts[0].charAt(0));
        if (parts.length > 1) {
            initials += parts[parts.length - 1].charAt(0);
        }
        return initials.toUpperCase(Locale.ENGLISH);
    }

    private String statusClassFor(String rawStatus) {
        if (rawStatus == null) {
            return "pending";
        }
        return switch (rawStatus) {
            case "approved" -> "approved";
            case "rejected" -> "rejected";
            case "under_review" -> "review";
            default -> "pending";
        };
    }

    private String actionClassFor(String action) {
        if (action == null) {
            return "pending";
        }
        return switch (action) {
            case "approved", "flag_dismissed" -> "approved";
            case "rejected", "flag_upheld" -> "rejected";
            default -> "review";
        };
    }
}
