<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>ScholarShare | Submission Pipeline</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png" />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@400;500;600;700&display=swap" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/css/sidebar.css" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/css/adminDashboard.css" rel="stylesheet" />
</head>
<body>

<jsp:include page="template/admin-sidebar.jsp">
    <jsp:param name="activePage" value="pipeline" />
</jsp:include>

<div class="main">
    <jsp:include page="template/admin-header.jsp">
        <jsp:param name="searchPlaceholder" value="Search pending submissions…" />
    </jsp:include>

    <main class="content">
        <div style="margin-bottom:1.5rem;">
            <h1 class="card-title" style="font-size:1.5rem;">Submission Pipeline</h1>
            <p style="color:#64748b;margin-top:0.35rem;">Review pending uploads and approve or reject with moderation notes.</p>
        </div>

        <c:choose>
            <c:when test="${empty pipeline}">
                <div class="card"><p style="padding:1.5rem;">No submissions awaiting review.</p></div>
            </c:when>
            <c:otherwise>
                <c:forEach var="item" items="${pipeline}">
                    <article class="card" style="margin-bottom:1rem;padding:1.25rem;">
                        <div class="card-header" style="margin-bottom:0.75rem;">
                            <span class="card-title"><c:out value="${item.title}"/></span>
                            <span class="status-badge ${item.statusClass}"><c:out value="${item.status}"/></span>
                        </div>
                        <p class="resource-meta" style="margin-bottom:0.5rem;">
                            <c:out value="${item.submitterName}"/> · <c:out value="${item.subject}"/> / <c:out value="${item.category}"/> · <c:out value="${item.submissionDate}"/>
                        </p>
                        <form action="${pageContext.request.contextPath}/admin/moderate" method="post" class="pipeline-form">
                            <input type="hidden" name="resourceId" value="${item.id}" />
                            <textarea name="note" rows="2" placeholder="Moderation note (required for rejection)…" style="width:100%;margin-bottom:0.75rem;padding:0.5rem;"></textarea>
                            <div style="display:flex;gap:0.5rem;">
                                <button type="submit" name="action" value="approved" class="btn-review">Approve</button>
                                <button type="submit" name="action" value="rejected" class="btn-review-red">Reject</button>
                            </div>
                        </form>
                    </article>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </main>

    <footer class="site-footer">
        <span class="footer-copy">© 2023 ScholarShare · Institutional Repository Management System</span>
    </footer>

    <div class="sidebar-overlay" id="sidebarOverlay"></div>
</div>

<script src="${pageContext.request.contextPath}/js/admin-dashboard-navbar.js"></script>
</body>
</html>
