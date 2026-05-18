<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>ScholarShare | User Approvals</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png" />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@400;500;600;700&display=swap" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/css/sidebar.css" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/css/adminDashboard.css" rel="stylesheet" />
</head>
<body>

<jsp:include page="template/admin-sidebar.jsp">
    <jsp:param name="activePage" value="approvals" />
</jsp:include>

<div class="main">
    <jsp:include page="template/admin-header.jsp">
        <jsp:param name="searchPlaceholder" value="Search pending registrations…" />
    </jsp:include>

    <main class="content">
        <div style="margin-bottom:1.5rem;">
            <h1 class="card-title" style="font-size:1.5rem;">Pending User Registrations</h1>
            <p style="color:#64748b;margin-top:0.35rem;">Approve or suspend new student accounts before they can access the platform.</p>
        </div>

        <div class="card">
            <c:choose>
                <c:when test="${empty pendingApprovals}">
                    <p style="padding:1.5rem;">No pending registrations at this time.</p>
                </c:when>
                <c:otherwise>
                    <div class="reg-list">
                        <c:forEach var="reg" items="${pendingApprovals}">
                            <div class="reg-item">
                                <div class="reg-avatar"><c:out value="${reg.initials}"/></div>
                                <div class="reg-info">
                                    <div class="reg-name"><c:out value="${reg.fullName}"/></div>
                                    <div class="reg-dept"><c:out value="${reg.email}"/> · <c:out value="${reg.phone}"/> · <c:out value="${reg.requestedOn}"/></div>
                                </div>
                                <form action="${pageContext.request.contextPath}/admin/reviewRegistration" method="post" style="display:flex;gap:0.5rem;">
                                    <input type="hidden" name="userId" value="${reg.id}" />
                                    <button type="submit" name="decision" value="approve" class="btn-review">Approve</button>
                                    <button type="submit" name="decision" value="reject" class="btn-review-red">Suspend</button>
                                </form>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </main>

    <footer class="site-footer">
        <span class="footer-copy">© 2023 ScholarShare · Institutional Repository Management System</span>
    </footer>

    <div class="sidebar-overlay" id="sidebarOverlay"></div>
</div>

<script src="${pageContext.request.contextPath}/js/admin-dashboard-navbar.js"></script>
</body>
</html>
