<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ScholarShare | Recent Resources</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/student-dashboard.css">
</head>
<body>

<jsp:include page="template/student-sidebar.jsp">
    <jsp:param name="activePage" value="recent-resources" />
</jsp:include>

<div class="main student-main">
    <jsp:include page="template/student-header.jsp">
        <jsp:param name="pageTitle" value="Recent Resources" />
    </jsp:include>

    <main class="content student-content">

        <%-- ── Page header ── --%>
        <section class="welcome-banner">
            <div class="welcome-banner-text">
                <h2>Recently Added Resources</h2>
                <p>Browse the latest peer-reviewed academic material shared by fellow students across your faculty.</p>
            </div>
            <a href="${pageContext.request.contextPath}/browser" class="welcome-upload-btn">
                <img src="${pageContext.request.contextPath}/images/home-icons/search.png" alt="">
                Browse All
            </a>
        </section>

        <%-- ── Resource grid ── --%>
        <c:choose>
            <c:when test="${empty recentResources}">
                <section class="recent-resources-section">
                    <div class="empty-panel">
                        <img src="${pageContext.request.contextPath}/images/home-icons/folder-open.png" alt="">
                        <p>No approved resources from other students yet. Check back later!</p>
                    </div>
                </section>
            </c:when>
            <c:otherwise>
                <section class="recent-resources-grid">
                    <c:forEach var="item" items="${recentResources}">
                        <article class="resource-grid-card">
                            <div class="resource-grid-card__icon">
                                <img src="${pageContext.request.contextPath}/images/home-icons/file-text.png" alt="">
                            </div>
                            <div class="resource-grid-card__body">
                                <h4><c:out value="${item.title}"/></h4>
                                <p class="resource-grid-card__meta">
                                    <c:out value="${item.type}"/> · <c:out value="${item.subject}"/>
                                </p>
                                <p class="resource-grid-card__date"><c:out value="${item.uploadDate}"/></p>
                                <p class="resource-grid-card__author">By <c:out value="${item.author}"/></p>
                            </div>
                        </article>
                    </c:forEach>
                </section>
            </c:otherwise>
        </c:choose>

    </main>
</div>

<script src="${pageContext.request.contextPath}/js/student-dashboard.js"></script>
</body>
</html>
