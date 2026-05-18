<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<header class="topbar student-topbar">
    <button class="hamburger" id="menuBtn" type="button" aria-label="Toggle navigation">
        <span></span><span></span><span></span>
    </button>

    <h1 class="page-title">
        <c:choose>
            <c:when test="${not empty param.pageTitle}">${param.pageTitle}</c:when>
            <c:otherwise>Dashboard</c:otherwise>
        </c:choose>
    </h1>

    <form class="search-wrap" method="get" action="${pageContext.request.contextPath}/browser">
        <img class="search-icon" src="${pageContext.request.contextPath}/images/home-icons/search.png" alt="">
        <input class="search-input" type="search" name="q" placeholder="Search resources…"
               value="<c:out value='${searchQuery}'/>">
    </form>

    <div class="topbar-right">
        <button class="icon-btn" type="button" aria-label="Notifications">
            <img src="${pageContext.request.contextPath}/images/home-icons/mail.png" alt="">
        </button>
        <a class="icon-btn" href="${pageContext.request.contextPath}/home" aria-label="Help">
            <img src="${pageContext.request.contextPath}/images/home-icons/book-open.png" alt="">
        </a>

        <%-- Profile chip — click opens dropdown --%>
        <div class="user-chip" id="profileChip" role="button" tabindex="0"
             aria-haspopup="true" aria-expanded="false">
            <div class="user-info">
                <div class="user-name"><c:out value="${profile.name}"/></div>
                <div class="user-role">Student</div>
            </div>
            <div class="user-avatar">
                <c:choose>
                    <c:when test="${not empty profile.avatarUrl}">
                        <img src="${profile.avatarUrl}" alt="Profile photo">
                    </c:when>
                    <c:otherwise>
                        <c:out value="${profile.initials}"/>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <%-- Profile dropdown --%>
        <div class="profile-dropdown" id="profileDropdown" hidden>
            <div class="profile-dropdown__header">
                <div class="profile-dropdown__avatar">
                    <c:choose>
                        <c:when test="${not empty profile.avatarUrl}">
                            <img src="${profile.avatarUrl}" alt="">
                        </c:when>
                        <c:otherwise>
                            <c:out value="${profile.initials}"/>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div>
                    <div class="profile-dropdown__name"><c:out value="${profile.name}"/></div>
                    <div class="profile-dropdown__email"><c:out value="${profile.email}"/></div>
                </div>
            </div>
            <div class="profile-dropdown__divider"></div>
            <a class="profile-dropdown__item"
               href="${pageContext.request.contextPath}/student/profile">
                <img src="${pageContext.request.contextPath}/images/home-icons/user-check.png"
                     alt="" width="15" height="15">
                My Profile
            </a>
            <a class="profile-dropdown__item"
               href="${pageContext.request.contextPath}/student/dashboard">
                <img src="${pageContext.request.contextPath}/images/icons-admin/dashboard.png"
                     alt="" width="15" height="15">
                My Dashboard
            </a>
            <div class="profile-dropdown__divider"></div>
            <form action="${pageContext.request.contextPath}/logout" method="post" style="margin:0;">
                <button class="profile-dropdown__item profile-dropdown__item--danger" type="submit">
                    <img src="${pageContext.request.contextPath}/images/icons-admin/log-out.png"
                         alt="" width="15" height="15">
                    Sign Out
                </button>
            </form>
        </div>
    </div>
</header>
