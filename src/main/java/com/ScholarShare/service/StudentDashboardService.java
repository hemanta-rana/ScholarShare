package com.ScholarShare.service;

import com.ScholarShare.dao.StudentDao;
import com.ScholarShare.dao.daoImpl.StudentDaoImpl;
import com.ScholarShare.entity.Resource;
import com.ScholarShare.entity.User;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Service layer for the student dashboard.
 * Maps database entities into presentation-ready maps for JSP EL.
 */
public class StudentDashboardService {

    private final StudentDao studentDao = new StudentDaoImpl();

    public Map<String, Object> getProfile(int userId, String contextPath) {
        User user = studentDao.getUserById(userId);
        Map<String, Object> profile = new HashMap<>();
        if (user == null) {
            return profile;
        }

        profile.put("id", user.getUserId());
        profile.put("name", user.getFullName());
        profile.put("firstName", firstName(user.getFullName()));
        profile.put("email", user.getEmail());
        profile.put("initials", buildInitials(user.getFullName()));
        profile.put("avatarUrl", resolveAvatarUrl(user.getProfilePic(), contextPath));
        profile.put("reputationScore", studentDao.getContributorReputationScore(userId));
        return profile;
    }

    public Map<String, Object> getSummaryCards(int userId) {
        Map<String, Integer> uploadCounts = studentDao.countUploadsByStatus(userId);
        int reputationScore = studentDao.getContributorReputationScore(userId);
        int approvedTotal = uploadCounts.getOrDefault("approved", 0);
        int approvedThisMonth = studentDao.countApprovedUploadsSince(userId, 30);
        int approvedLastMonth = studentDao.countApprovedUploadsSince(userId, 60) - approvedThisMonth;
        int pendingReview = uploadCounts.getOrDefault("pending", 0)
                + uploadCounts.getOrDefault("underReview", 0);
        int savedResources = studentDao.countSavedResources(userId);

        Map<String, Object> cards = new HashMap<>();
        cards.put("reputationScore", reputationScore);
        cards.put("reputationMax", 100);
        cards.put("approvedUploads", approvedTotal);
        cards.put("approvedDelta", Math.max(0, approvedThisMonth - Math.max(0, approvedLastMonth)));
        cards.put("pendingReview", pendingReview);
        cards.put("pendingEta", nextFridayLabel());
        cards.put("savedResources", savedResources);
        cards.put("savedLabel", savedResources == 1 ? "Active library" : "Active libraries");
        return cards;
    }

    public Map<String, Object> getReputationBreakdown(int userId) {
        int score = studentDao.getContributorReputationScore(userId);
        double accuracy = studentDao.getAccuracyRate(userId);
        double helpfulness = studentDao.getPeerHelpfulness(userId);
        int percentile = studentDao.getContributorPercentileRank(userId);

        Map<String, Object> breakdown = new HashMap<>();
        breakdown.put("score", score);
        breakdown.put("tierLabel", tierLabel(score));
        breakdown.put("percentile", percentile);
        breakdown.put("percentileMessage",
                "You are in the top " + Math.max(1, 100 - percentile)
                        + "% of student contributors this semester.");
        breakdown.put("accuracyRate", (int) accuracy);
        breakdown.put("peerHelpfulness", (int) helpfulness);
        return breakdown;
    }

    public String buildWelcomeMessage(String firstName, int pendingReview) {
        String name = (firstName == null || firstName.isBlank()) ? "there" : firstName;
        if (pendingReview <= 0) {
            return "You have no submissions awaiting review. Your contributions help maintain our institutional excellence.";
        }
        String submissionWord = pendingReview == 1 ? "submission" : "submissions";
        return "You have " + pendingReview + " pending " + submissionWord
                + " currently under peer review. Your contributions help maintain our institutional excellence.";
    }

    public List<Map<String, Object>> getRecentSubmissions(int userId, String searchQuery) {
        List<Resource> resources;
        if (searchQuery != null && !searchQuery.isBlank()) {
            resources = studentDao.searchStudentSubmissions(userId, searchQuery.trim(), 10);
        } else {
            resources = studentDao.getRecentSubmissionsByUser(userId, 10);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");

        for (Resource resource : resources) {
            result.add(mapSubmissionRow(resource, sdf));
        }
        return result;
    }

    public List<Map<String, Object>> getRecentlyAddedResources(int userId) {
        List<Resource> resources = studentDao.getRecentlyAddedApprovedResources(userId, 8);
        List<Map<String, Object>> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");

        for (Resource resource : resources) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", resource.getResourceId());
            map.put("title", resource.getTitle());
            map.put("type", formatResourceType(resource.getResourceType()));
            map.put("subject", resource.getSubjectName() != null ? resource.getSubjectName() : "General");
            map.put("topic", resource.getTopicName() != null ? resource.getTopicName() : "");
            map.put("author", resource.getSubmitterName() != null ? resource.getSubmitterName() : "Student");
            map.put("uploadDate", resource.getUploadDate() != null ? sdf.format(resource.getUploadDate()) : "");
            result.add(map);
        }
        return result;
    }

    private Map<String, Object> mapSubmissionRow(Resource resource, SimpleDateFormat sdf) {
        Map<String, Object> map = new HashMap<>();
        String rawStatus = resource.getStatus() != null ? resource.getStatus() : "pending";

        map.put("id", resource.getResourceId());
        map.put("title", resource.getTitle());
        map.put("type", formatResourceType(resource.getResourceType()));
        map.put("subject", resource.getSubjectName() != null ? resource.getSubjectName() : "");
        map.put("date", resource.getUploadDate() != null ? sdf.format(resource.getUploadDate()) : "Unknown");
        map.put("status", formatStatusLabel(rawStatus));
        map.put("statusClass", statusClass(rawStatus));
        return map;
    }

    private String formatResourceType(String resourceType) {
        if (resourceType == null) {
            return "DOCUMENT";
        }
        return switch (resourceType) {
            case "notes" -> "PDF ARCHIVE";
            case "past_paper" -> "SOLUTION SET";
            case "summary" -> "HANDOUT";
            case "revision_guide" -> "STUDY GUIDE";
            default -> resourceType.replace("_", " ").toUpperCase(Locale.ENGLISH);
        };
    }

    private String formatStatusLabel(String rawStatus) {
        return rawStatus.replace("_", " ").toUpperCase(Locale.ENGLISH);
    }

    private String statusClass(String rawStatus) {
        return switch (rawStatus) {
            case "approved" -> "approved";
            case "rejected" -> "rejected";
            case "under_review" -> "review";
            default -> "pending";
        };
    }

    private String tierLabel(int score) {
        if (score >= 85) {
            return "TOP TIER";
        }
        if (score >= 70) {
            return "GOLD TIER";
        }
        if (score >= 50) {
            return "SILVER TIER";
        }
        return "BRONZE TIER";
    }

    private String firstName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            return "";
        }
        return fullName.trim().split("\\s+")[0];
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

    private String resolveAvatarUrl(String profilePic, String contextPath) {
        if (profilePic == null || profilePic.isBlank()) {
            return "";
        }
        if (profilePic.startsWith("http://") || profilePic.startsWith("https://")) {
            return profilePic;
        }
        String path = profilePic.startsWith("/") ? profilePic : "/" + profilePic;
        return contextPath + path;
    }

    private String nextFridayLabel() {
        LocalDate date = LocalDate.now();
        while (date.getDayOfWeek() != DayOfWeek.FRIDAY) {
            date = date.plusDays(1);
        }
        return "Expected by " + date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }
}
