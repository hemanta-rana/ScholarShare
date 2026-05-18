<%@ page contentType="text/html;charset=UTF-8" %>

<aside class="sidebar">

    <div class="sidebar-brand">
        <div class="brand-icon">
            <img
                    src="${pageContext.request.contextPath}/images/logo.png"
                    alt="ScholarShare Logo">
        </div>

        <div class="brand-text">
            <span class="brand-name">ScholarShare</span>
            <span class="brand-sub">Admin Panel</span>
        </div>
    </div>

    <nav class="sidebar-nav">

        <a class="nav-item ${param.activePage eq 'dashboard' ? 'active' : ''}"
           href="${pageContext.request.contextPath}/admin/dashboard">
            <img class="nav-icon"
                 src="${pageContext.request.contextPath}/images/icons-admin/dashboard.png"
                 alt="">
            <span>Dashboard</span>
        </a>

        <a class="nav-item ${param.activePage eq 'approvals' ? 'active' : ''}"
           href="${pageContext.request.contextPath}/admin/user-approvals">
            <img class="nav-icon"
                 src="${pageContext.request.contextPath}/images/icons-admin/approvals.png"
                 alt="">
            <span>User Approvals</span>
        </a>

        <a class="nav-item ${param.activePage eq 'pipeline' ? 'active' : ''}"
           href="${pageContext.request.contextPath}/admin/pipeline">
            <img class="nav-icon"
                 src="${pageContext.request.contextPath}/images/icons-admin/pipeline.png"
                 alt="">
            <span>Submission Pipeline</span>
        </a>

        <a class="nav-item ${param.activePage eq 'flags' ? 'active' : ''}"
           href="${pageContext.request.contextPath}/admin/flags">
            <img class="nav-icon"
                 src="${pageContext.request.contextPath}/images/icons-admin/flag.png"
                 alt="">
            <span>Flags Management</span>
        </a>

        <a class="nav-item ${param.activePage eq 'categories' ? 'active' : ''}"
           href="${pageContext.request.contextPath}/admin/categories">
            <img class="nav-icon"
                 src="${pageContext.request.contextPath}/images/icons-admin/category.png"
                 alt="">
            <span>Category Management</span>
        </a>

        <a class="nav-item ${param.activePage eq 'analytics' ? 'active' : ''}"
           href="${pageContext.request.contextPath}/admin/analytics">
            <img class="nav-icon"
                 src="${pageContext.request.contextPath}/images/icons-admin/analytics.png"
                 alt="">
            <span>Analytics</span>
        </a>

        <a class="nav-item ${param.activePage eq 'audit' ? 'active' : ''}"
           href="${pageContext.request.contextPath}/admin/audit">
            <img class="nav-icon"
                 src="${pageContext.request.contextPath}/images/icons-admin/audit.png"
                 alt="">
            <span>Audit Trail</span>
        </a>

        <a class="nav-item ${param.activePage eq 'profile' ? 'active' : ''}"
           href="${pageContext.request.contextPath}/admin/profile">
            <img class="nav-icon"
                 src="${pageContext.request.contextPath}/images/home-icons/user-check.png"
                 alt="">
            <span>My Profile</span>
        </a>

    </nav>

    <div class="sidebar-logout">
        <form action="${pageContext.request.contextPath}/logout" method="post">
            <button class="logout-btn" type="submit">
                <img class="nav-icon"
                     src="${pageContext.request.contextPath}/images/icons-admin/log-out.png"
                     alt="">
                <span>Logout</span>
            </button>
        </form>
    </div>

</aside>

<%-- Mobile overlay — closes sidebar when tapped outside --%>
<div class="sidebar-overlay" id="sidebarOverlay"></div>