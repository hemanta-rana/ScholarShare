<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<header class="topbar">
    <button class="hamburger" id="menuBtn" type="button" aria-label="Toggle navigation">
        <span></span><span></span><span></span>
    </button>

    <form class="search-wrap" method="get" action="${pageContext.request.contextPath}/browser">
        <img class="search-icon" src="${pageContext.request.contextPath}/images/home-icons/search.png" alt="">
        <input class="search-input" type="search" name="q"
               placeholder="${empty param.searchPlaceholder ? 'Search...' : param.searchPlaceholder}" />
    </form>

    <div class="topbar-right">
        <button class="icon-btn" type="button" aria-label="Notifications">
            <img src="${pageContext.request.contextPath}/images/home-icons/mail.png" alt="">
        </button>

        <%-- Admin user chip — click opens dropdown --%>
        <div class="user-chip admin-user-chip" id="adminProfileChip"
             role="button" tabindex="0" aria-haspopup="true" aria-expanded="false">
            <div class="user-info">
                <div class="user-name"><c:out value="${sessionScope.user.name}"/></div>
                <div class="user-role"><c:out value="${sessionScope.user.role}"/></div>
            </div>
            <div class="user-avatar">
                <c:choose>
                    <c:when test="${not empty sessionScope.user.profilePic}">
                        <img src="${pageContext.request.contextPath}/${sessionScope.user.profilePic}" alt="avatar">
                    </c:when>
                    <c:otherwise>
                        <c:out value="${sessionScope.user.initials}"/>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <%-- Admin profile dropdown --%>
        <div class="admin-profile-dropdown" id="adminProfileDropdown" hidden>
            <div class="admin-dropdown__header">
                <div class="admin-dropdown__avatar">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user.profilePic}">
                            <img src="${pageContext.request.contextPath}/${sessionScope.user.profilePic}" alt="">
                        </c:when>
                        <c:otherwise>
                            <c:out value="${sessionScope.user.initials}"/>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div>
                    <div class="admin-dropdown__name"><c:out value="${sessionScope.user.name}"/></div>
                    <div class="admin-dropdown__role"><c:out value="${sessionScope.user.role}"/></div>
                </div>
            </div>
            <div class="admin-dropdown__divider"></div>
            <a class="admin-dropdown__item"
               href="${pageContext.request.contextPath}/admin/profile">
                <img src="${pageContext.request.contextPath}/images/home-icons/user-check.png"
                     alt="" width="15" height="15">
                My Profile
            </a>
            <a class="admin-dropdown__item"
               href="${pageContext.request.contextPath}/admin/dashboard">
                <img src="${pageContext.request.contextPath}/images/icons-admin/dashboard.png"
                     alt="" width="15" height="15">
                Dashboard
            </a>
            <div class="admin-dropdown__divider"></div>
            <form action="${pageContext.request.contextPath}/logout" method="post" style="margin:0;">
                <button class="admin-dropdown__item admin-dropdown__item--danger" type="submit">
                    <img src="${pageContext.request.contextPath}/images/icons-admin/log-out.png"
                         alt="" width="15" height="15">
                    Sign Out
                </button>
            </form>
        </div>
    </div>
</header>
