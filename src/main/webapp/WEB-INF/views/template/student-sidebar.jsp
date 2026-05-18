<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<aside class="sidebar student-sidebar">

    <div class="sidebar-brand">
        <div class="brand-icon">
            <img src="${pageContext.request.contextPath}/images/logo.png" alt="ScholarShare">
        </div>
        <div class="brand-text">
            <span class="brand-name">ScholarShare</span>
            <span class="brand-sub">University Portal</span>
        </div>
    </div>

    <div class="sidebar-user-card">
        <div class="sidebar-user-avatar">
            <c:choose>
                <c:when test="${not empty profile.avatarUrl}">
                    <img src="${profile.avatarUrl}" alt="${profile.name}">
                </c:when>
                <c:otherwise>
                    <span><c:out value="${profile.initials}"/></span>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="sidebar-user-meta">
            <strong><c:out value="${profile.name}"/></strong>
            <span class="sidebar-score"><c:out value="${profile.reputationScore}"/> SCORE</span>
        </div>
    </div>

    <nav class="sidebar-nav">
        <a class="nav-item ${activePage eq 'dashboard' ? 'active' : ''}"
           href="${pageContext.request.contextPath}/student/dashboard">
            <img class="nav-icon" src="${pageContext.request.contextPath}/images/icons-admin/dashboard.png" alt="">
            <span>Dashboard</span>
        </a>
        <a class="nav-item" href="${pageContext.request.contextPath}/home#categories">
            <img class="nav-icon" src="${pageContext.request.contextPath}/images/home-icons/discover.png" alt="">
            <span>Resources</span>
        </a>
        <a class="nav-item" href="${pageContext.request.contextPath}/student/dashboard#submissions">
            <img class="nav-icon" src="${pageContext.request.contextPath}/images/home-icons/upload.png" alt="">
            <span>My Uploads</span>
        </a>
        <a class="nav-item" href="${pageContext.request.contextPath}/student/dashboard#reputation">
            <img class="nav-icon" src="${pageContext.request.contextPath}/images/home-icons/reputation-score.png" alt="">
            <span>Reputation</span>
        </a>
        <a class="nav-item" href="${pageContext.request.contextPath}/student/dashboard#recent-resources">
            <img class="nav-icon" src="${pageContext.request.contextPath}/images/icons-admin/analytics.png" alt="">
            <span>Analytics</span>
        </a>
    </nav>

    <div class="sidebar-actions">
        <a href="${pageContext.request.contextPath}/student/upload-resource"
           class="sidebar-upload-btn ${activePage eq 'upload' ? 'active' : ''}">
            <img src="${pageContext.request.contextPath}/images/home-icons/upload.png" alt="">
            Upload Resource
        </a>
    </div>

    <div class="sidebar-footer-links">
        <a class="nav-item" href="${pageContext.request.contextPath}/home#footer">
            <img class="nav-icon" src="${pageContext.request.contextPath}/images/home-icons/mail.png" alt="">
            <span>Support</span>
        </a>
        <form action="${pageContext.request.contextPath}/logout" method="post" class="sidebar-logout-form">
            <button type="submit" class="logout-btn">
                <img class="nav-icon" src="${pageContext.request.contextPath}/images/icons-admin/log-out.png" alt="">
                <span>Sign Out</span>
            </button>
        </form>
    </div>
</aside>
