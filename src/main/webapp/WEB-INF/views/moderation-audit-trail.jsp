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
    <jsp:param name="activePage" value="audit" />
</jsp:include>

<div class="main">
    <jsp:include page="template/admin-header.jsp">
        <jsp:param name="searchPlaceholder" value="Search audit logs..." />
    </jsp:include>

    <main class="content">
        <div class="page-header">
            <div>
                <div class="page-title">
                    
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
            <c:choose>
                <c:when test="${empty auditLogs}">
                    <div class="empty-state" style="text-align:center;padding:2rem;">
                        <img src="${pageContext.request.contextPath}/images/icons-admin/audit.png" alt="" width="48" height="48">
                        <p>No moderation actions recorded yet.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="log" items="${auditLogs}">
                        <article class="audit-card">
                            <div class="audit-header">
                                <div class="audit-meta">
                                    <span class="audit-id">LOG-<c:out value="${log.id}"/></span>
                                    <div class="audit-title"><c:out value="${log.resourceTitle}"/></div>
                                    <div class="audit-date">
                                        <img src="${pageContext.request.contextPath}/images/home-icons/file-text.png" alt="" width="14" height="14">
                                        <c:out value="${log.actionedAt}"/>
                                    </div>
                                </div>
                                <span class="audit-action-badge action-${log.actionClass}"><c:out value="${log.action}"/></span>
                            </div>
                            <div class="audit-body">
                                <div class="audit-section">
                                    <div class="section-label">Moderation Note</div>
                                    <div class="section-content"><c:out value="${log.note}"/></div>
                                </div>
                            </div>
                            <div class="moderator-info">
                                <div class="mod-avatar"><c:out value="${log.adminInitials}"/></div>
                                <div class="mod-details">
                                    <span class="mod-name"><c:out value="${log.adminName}"/></span>
                                    <span class="mod-role">Administrator</span>
                                </div>
                            </div>
                        </article>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
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
