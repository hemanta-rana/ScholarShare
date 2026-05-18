<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>ScholarShare | Moderation Audit Trail</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png" />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@400;500;600;700&display=swap" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/css/sidebar.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/adminDashboard.css" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/css/moderation-audit-trail.css" rel="stylesheet" />
</head>
<body>

<jsp:include page="template/admin-sidebar.jsp">
    <jsp:param name="activePage" value="audit-trail" />
</jsp:include>

<div class="main">
    <jsp:include page="template/admin-header.jsp">
        <jsp:param name="searchPlaceholder" value="Search audit logs..." />
    </jsp:include>

    <main class="content">
        <div class="page-header">
            <div>
                <div class="page-title">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="28" height="28">
                        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                        <polyline points="14 2 14 8 20 8"></polyline>
                        <line x1="16" y1="13" x2="8" y2="13"></line>
                        <line x1="16" y1="17" x2="8" y2="17"></line>
                        <polyline points="10 9 9 9 8 9"></polyline>
                    </svg>
                    Moderation Audit Trail
                </div>
                <div class="page-sub">Complete history of moderation decisions, actions taken, and moderator notes.</div>
            </div>
        </div>

        <div class="audit-controls">
            <input type="text" class="audit-search" placeholder="Search by Audit ID, Moderator, or Resource..." />
            <select class="audit-filter">
                <option value="">All Actions</option>
                <option value="upheld">Flag Upheld</option>
                <option value="dismissed">Flag Dismissed</option>
                <option value="deleted">Resource Deleted</option>
            </select>
            <select class="audit-filter">
                <option value="">Any Date</option>
                <option value="today">Today</option>
                <option value="week">Past 7 Days</option>
                <option value="month">Past 30 Days</option>
            </select>
        </div>

        <div class="audit-list">
            <!-- Sample Audit Log 1 -->
            <div class="audit-card">
                <div class="audit-header">
                    <div class="audit-meta">
                        <span class="audit-id">AUDIT-8924</span>
                        <div class="audit-title">Flag Upheld: "Introduction to Quantum Physics"</div>
                        <div class="audit-date">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <circle cx="12" cy="12" r="10"></circle>
                                <polyline points="12 6 12 12 16 14"></polyline>
                            </svg>
                            Oct 24, 2023 at 14:30 PM
                        </div>
                    </div>
                    <span class="audit-action-badge action-upheld">Upheld</span>
                </div>
                <div class="audit-body">
                    <div class="audit-section">
                        <div class="section-label">Reported Issue</div>
                        <div class="section-content">
                            <strong>Reason:</strong> Plagiarism<br/>
                            <strong>User Context:</strong> This document copies extensively from Chapter 4 of Griffiths without citation.
                        </div>
                    </div>
                    <div class="audit-section">
                        <div class="section-label">Moderation Rationale</div>
                        <div class="section-content">
                            Verified that the content indeed matches the textbook verbatim without proper attribution. Upheld the flag and temporarily suspended the uploader's privileges pending review.
                        </div>
                    </div>
                </div>
                <div class="moderator-info">
                    <div class="mod-avatar">AD</div>
                    <div class="mod-details">
                        <span class="mod-name">Admin User</span>
                        <span class="mod-role">System Administrator</span>
                    </div>
                </div>
            </div>

            <!-- Sample Audit Log 2 -->
            <div class="audit-card">
                <div class="audit-header">
                    <div class="audit-meta">
                        <span class="audit-id">AUDIT-8923</span>
                        <div class="audit-title">Flag Dismissed: "Calculus 101 Notes"</div>
                        <div class="audit-date">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <circle cx="12" cy="12" r="10"></circle>
                                <polyline points="12 6 12 12 16 14"></polyline>
                            </svg>
                            Oct 23, 2023 at 09:15 AM
                        </div>
                    </div>
                    <span class="audit-action-badge action-dismissed">Dismissed</span>
                </div>
                <div class="audit-body">
                    <div class="audit-section">
                        <div class="section-label">Reported Issue</div>
                        <div class="section-content">
                            <strong>Reason:</strong> Inappropriate Content<br/>
                            <strong>User Context:</strong> Contains offensive language on page 3.
                        </div>
                    </div>
                    <div class="audit-section">
                        <div class="section-label">Moderation Rationale</div>
                        <div class="section-content">
                            Reviewed the document up to page 10. Could not find any offensive language. The notes appear to be standard calculus formulas. Dismissed the flag as invalid.
                        </div>
                    </div>
                </div>
                <div class="moderator-info">
                    <div class="mod-avatar" style="background: #8e44ad;">SM</div>
                    <div class="mod-details">
                        <span class="mod-name">Sarah Moderator</span>
                        <span class="mod-role">Content Moderator</span>
                    </div>
                </div>
            </div>

            <!-- Sample Audit Log 3 -->
            <div class="audit-card">
                <div class="audit-header">
                    <div class="audit-meta">
                        <span class="audit-id">AUDIT-8921</span>
                        <div class="audit-title">Resource Deleted: "Final Exam Answers 2023"</div>
                        <div class="audit-date">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <circle cx="12" cy="12" r="10"></circle>
                                <polyline points="12 6 12 12 16 14"></polyline>
                            </svg>
                            Oct 20, 2023 at 16:45 PM
                        </div>
                    </div>
                    <span class="audit-action-badge action-deleted">Deleted</span>
                </div>
                <div class="audit-body">
                    <div class="audit-section">
                        <div class="section-label">Action Trigger</div>
                        <div class="section-content">
                            Direct administrative action (No prior flag).<br/>
                            <strong>Resource:</strong> Final Exam Answers 2023 (ID: RES-4402)
                        </div>
                    </div>
                    <div class="audit-section">
                        <div class="section-label">Moderation Rationale</div>
                        <div class="section-content">
                            Resource violates academic integrity policies by sharing current semester's final exam solutions. Resource permanently removed and user account banned.
                        </div>
                    </div>
                </div>
                <div class="moderator-info">
                    <div class="mod-avatar">AD</div>
                    <div class="mod-details">
                        <span class="mod-name">Admin User</span>
                        <span class="mod-role">System Administrator</span>
                    </div>
                </div>
            </div>

        </div>

    </main>

    <footer class="site-footer">
        <span class="footer-copy">© 2023 ScholarShare · Institutional Repository Management System</span>
        <div class="footer-links">
            <a href="${pageContext.request.contextPath}/docs">Documentation</a>
            <a href="${pageContext.request.contextPath}/support">Support Desk</a>
            <a href="${pageContext.request.contextPath}/status">System Status</a>
        </div>
    </footer>

    <div class="sidebar-overlay" id="sidebarOverlay"></div>

    <script src="${pageContext.request.contextPath}/js/admin-dashboard-navbar.js"></script>
</div>
</body>
</html>
