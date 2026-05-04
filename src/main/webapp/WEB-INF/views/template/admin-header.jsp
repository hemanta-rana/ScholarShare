<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<header class="topbar">
    <button class="hamburger" id="menuBtn" aria-label="Toggle navigation">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="3" y1="12" x2="21" y2="12"/><line x1="3" y1="6" x2="21" y2="6"/><line x1="3" y1="18" x2="21" y2="18"/></svg>
    </button>

    <div class="search-wrap">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
        <input class="search-input" type="text" placeholder="${empty param.searchPlaceholder ? 'Search...' : param.searchPlaceholder}" />
    </div>

    <div class="topbar-right">
        <button class="notif-btn">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/><path d="M13.73 21a2 2 0 0 1-3.46 0"/></svg>
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
