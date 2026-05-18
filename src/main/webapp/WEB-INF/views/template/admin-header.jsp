<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<header class="topbar">
    <button class="hamburger" id="menuBtn" type="button" aria-label="Toggle navigation">
        <span></span><span></span><span></span>
    </button>

    <div class="search-wrap">
        <img class="search-icon" src="${pageContext.request.contextPath}/images/home-icons/search.png" alt="">
        <input class="search-input" type="text" placeholder="${empty param.searchPlaceholder ? 'Search...' : param.searchPlaceholder}" />
    </div>

    <div class="topbar-right">
        <button class="icon-btn" type="button" aria-label="Notifications">
            <img src="${pageContext.request.contextPath}/images/home-icons/mail.png" alt="">
        </button>

        <div class="user-chip">
            <div class="user-info">
                <div class="user-name">${sessionScope.user.name}</div>
                <div class="user-role">${sessionScope.user.role}</div>
            </div>
            <div class="user-avatar">
                <c:choose>
                    <c:when test="${not empty sessionScope.user.avatarUrl}">
                        <img src="${sessionScope.user.avatarUrl}" alt="avatar" style="width:36px;height:36px;border-radius:4px;">
                    </c:when>
                    <c:otherwise>
                        ${sessionScope.user.initials}
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</header>
