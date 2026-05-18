<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>ScholarShare | Analytics</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png" />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@400;500;600;700&display=swap" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/css/sidebar.css" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/css/adminDashboard.css" rel="stylesheet" />
</head>
<body>

<jsp:include page="template/admin-sidebar.jsp">
    <jsp:param name="activePage" value="analytics" />
</jsp:include>

<div class="main">
    <jsp:include page="template/admin-header.jsp">
        <jsp:param name="searchPlaceholder" value="Search analytics…" />
    </jsp:include>

    <main class="content">
        <c:set var="stats" value="${analytics.stats}" />

        <div class="stats-row">
            <div class="stat-card gold">
                <div class="stat-left">
                    <div class="stat-label">Pending<br />Registrations</div>
                    <div class="stat-value">${stats.pendingRegistrations}</div>
                </div>
                <img class="stat-icon-img" src="${pageContext.request.contextPath}/images/icons-admin/approvals.png" alt="">
            </div>
            <div class="stat-card blue">
                <div class="stat-left">
                    <div class="stat-label">Pending<br />Submissions</div>
                    <div class="stat-value">${stats.pendingSubmissions}</div>
                </div>
                <img class="stat-icon-img" src="${pageContext.request.contextPath}/images/icons-admin/pipeline.png" alt="">
            </div>
            <div class="stat-card green">
                <div class="stat-left">
                    <div class="stat-label">Approval<br />Rate</div>
                    <div class="stat-value">${stats.approvalRate}%</div>
                </div>
                <img class="stat-icon-img" src="${pageContext.request.contextPath}/images/home-icons/circle-check.png" alt="">
            </div>
            <div class="stat-card navy">
                <div class="stat-left">
                    <div class="stat-label">Total<br />Resources</div>
                    <div class="stat-value">${stats.totalResources}</div>
                </div>
                <img class="stat-icon-img" src="${pageContext.request.contextPath}/images/home-icons/database.png" alt="">
            </div>
        </div>

        <div class="grid-row">
            <div class="card chart-card">
                <div class="card-title">Submissions This Week</div>
                <div class="chart-area">
                    <c:forEach var="day" items="${analytics.weeklyData}">
                        <div class="chart-day">
                            <div class="chart-bar-wrap">
                                <div class="chart-bar ${day.active ? 'active' : ''}" style="height:${day.height}px;"></div>
                            </div>
                            <span class="chart-label">${day.label}</span>
                        </div>
                    </c:forEach>
                </div>
                <div class="chart-footer">Peak on <strong>${analytics.weeklyPeakDay}</strong></div>
            </div>

            <div class="card">
                <div class="card-title">Top Contributors</div>
                <ul class="analytics-list">
                    <c:forEach var="c" items="${analytics.topContributors}">
                        <li><c:out value="${c.name}"/> — <strong><c:out value="${c.count}"/></strong> approved</li>
                    </c:forEach>
                    <c:if test="${empty analytics.topContributors}"><li>No data yet.</li></c:if>
                </ul>
            </div>
        </div>

        <div class="card" style="margin-top:1rem;">
            <div class="card-title">Most Flagged Resources</div>
            <ul class="analytics-list">
                <c:forEach var="f" items="${analytics.mostFlagged}">
                    <li><c:out value="${f.title}"/> — <strong><c:out value="${f.count}"/></strong> flags</li>
                </c:forEach>
                <c:if test="${empty analytics.mostFlagged}"><li>No flags recorded.</li></c:if>
            </ul>
        </div>
    </main>

    <footer class="site-footer">
        <span class="footer-copy">© 2023 ScholarShare · Institutional Repository Management System</span>
    </footer>

    <div class="sidebar-overlay" id="sidebarOverlay"></div>
</div>

<style>
    .stat-icon-img { width: 40px; height: 40px; object-fit: contain; opacity: 0.9; }
    .analytics-list { list-style: none; padding: 1rem 1.25rem; margin: 0; }
    .analytics-list li { padding: 0.5rem 0; border-bottom: 1px solid #e8ecf0; }
</style>
<script src="${pageContext.request.contextPath}/js/admin-dashboard-navbar.js"></script>
</body>
</html>

