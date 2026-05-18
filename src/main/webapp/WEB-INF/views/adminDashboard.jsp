<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>

        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <title>ScholarShare | Admin Dashboard</title>
            <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png" />
            <link rel="preconnect" href="https://fonts.googleapis.com" />
            <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
            <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@400;500;600;700&display=swap" rel="stylesheet" />

            <link href="${pageContext.request.contextPath}/css/sidebar.css" rel="stylesheet" />
            <link href="${pageContext.request.contextPath}/css/adminDashboard.css" rel="stylesheet" />

        </head>

        <body>

            <jsp:include page="template/admin-sidebar.jsp">
                <jsp:param name="activePage" value="dashboard" />
            </jsp:include>

            <div class="main">

                <jsp:include page="template/admin-header.jsp">
                    <jsp:param name="searchPlaceholder" value="Search resources, users, or archives…" />
                </jsp:include>

                <main class="content">

                    <div class="stats-row">
                        <div class="stat-card gold">
                            <div class="stat-left">
                                <div class="stat-label">Pending<br />Registrations</div>
                                <div class="stat-value">${stats.pendingRegistrations}</div>
                            </div>
                            <div class="stat-icon"></div>
                        </div>

                        <div class="stat-card blue">
                            <div class="stat-left">
                                <div class="stat-label">Pending<br />Submissions</div>
                                <div class="stat-value">${stats.pendingSubmissions}</div>
                            </div>
                            <img class="stat-icon-img" src="${pageContext.request.contextPath}/images/icons-admin/pipeline.png" alt="">
                        </div>

                        <div class="stat-card red">
                            <div class="stat-left">
                                <div class="stat-label">Open<br />Flags</div>
                                <div class="stat-value">${stats.openFlags}</div>
                            </div>
                            <div class="stat-icon"></div>
                        </div>

                        <div class="stat-card navy">
                            <div class="stat-left">
                                <div class="stat-label">Total<br />Resources</div>
                                <div class="stat-value">${stats.totalResources}</div>
                            </div>
                            <div class="stat-icon"></div>
                        </div>

                        <div class="stat-card green">
                            <div class="stat-left">
                                <div class="stat-label">Approval<br />Rate</div>
                                <div class="stat-value">${stats.approvalRate}%</div>
                            </div>
                            <div class="stat-icon"></div>
                        </div>
                    </div>

                    <div class="grid-row">
                        <div class="card">
                            <div class="card-header">
                                <span class="card-title">Recent Submissions</span>
                                <span class="badge-live">Live Queue</span>
                            </div>

                            <table class="submissions-table">
                                <thead>
                                    <tr>
                                        <th>Title</th>
                                        <th>Submitted By</th>
                                        <th>Date</th>
                                        <th>Status</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="sub" items="${recentSubmissions}">
                                        <tr>
                                            <td>
                                                <div class="resource-title"><c:out value="${sub.title}"/></div>
                                                <div class="resource-meta"><c:out value="${sub.category}"/> · <c:out value="${sub.type}"/></div>
                                            </td>
                                            <td><span class="submitter-name"><c:out value="${sub.submitterName}"/></span></td>
                                            <td><span class="date-text"><c:out value="${sub.submissionDate}"/></span></td>
                                            <td>
                                                <span class="status-badge ${sub.statusClass}">
                                                    <span class="status-dot"></span><c:out value="${sub.status}"/>
                                                </span>
                                            </td>
                                        </tr>
                                    </c:forEach>

                                    <c:if test="${empty recentSubmissions}">
                                        <tr>
                                            <td colspan="4">No recent submissions.</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>

                            <div class="table-footer">
                                <a href="${pageContext.request.contextPath}/admin/pipeline" class="view-link">View
                                    Pipeline →</a>
                            </div>
                        </div>

                        <div class="card chart-card">
                            <div class="card-title">Submissions This Week</div>
                            <div class="chart-area">
                                <c:forEach var="day" items="${weeklyData}">
                                    <div class="chart-day">
                                        <div class="chart-bar-wrap">
                                            <div class="chart-bar ${day.active ? 'active' : ''}"
                                                style="height:${day.height}px;"></div>
                                        </div>
                                        <span class="chart-label">${day.label}</span>
                                    </div>
                                </c:forEach>
                            </div>
                            <div class="chart-footer">
                                Peak performance reached on <strong>${weeklyPeakDay}</strong>
                            </div>
                        </div>
                    </div>

                    <div class="bottom-grid">
                        <div class="card">
                            <div class="card-header">
                                <span class="card-title">Pending User Registrations</span>
                            </div>
                            <div class="reg-list">
                                <c:forEach var="reg" items="${pendingRegs}">
                                    <div class="reg-item">
                                        <div class="reg-avatar"><c:out value="${reg.initials}"/></div>
                                        <div class="reg-info">
                                            <div class="reg-name"><c:out value="${reg.fullName}"/></div>
                                            <div class="reg-dept"><c:out value="${reg.department}"/> · <c:out value="${reg.requestedOn}"/></div>
                                        </div>
                                        <form action="${pageContext.request.contextPath}/admin/reviewRegistration"
                                            method="post" style="display:flex;gap:0.35rem;">
                                            <input type="hidden" name="userId" value="${reg.id}" />
                                            <button class="btn-review" type="submit" name="decision" value="approve">Approve</button>
                                            <button class="btn-review-red" type="submit" name="decision" value="reject">Suspend</button>
                                        </form>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>

                        <div class="card">
                            <div class="card-header flags-header">
                                <span class="card-title">Recent Flags</span>
                            </div>
                            <div class="flag-list">
                                <c:forEach var="flag" items="${recentFlags}">
                                    <div class="flag-item">
                                        <div class="flag-info">
                                            <div class="flag-title"><c:out value="${flag.resourceTitle}"/></div>
                                            <div class="flag-meta">Flagged by: <c:out value="${flag.flaggedBy}"/> · <c:out value="${flag.reason}"/></div>
                                        </div>
                                        <a href="${pageContext.request.contextPath}/admin/flags" class="btn-review-red">Review</a>
                                    </div>
                                </c:forEach>
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

                <script src="${pageContext.request.contextPath}/js/admin-dashboard-navbar.js">
                </script>
            </div>
        </body>

        </html>

