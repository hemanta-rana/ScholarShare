<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<header class="topbar student-topbar">
    <button class="hamburger" id="menuBtn" type="button" aria-label="Toggle navigation">
        <span></span><span></span><span></span>
    </button>

    <h1 class="page-title">Dashboard</h1>

    <form class="search-wrap" method="get" action="${pageContext.request.contextPath}/student/dashboard">
        <img class="search-icon" src="${pageContext.request.contextPath}/images/home-icons/search.png" alt="">
        <input class="search-input" type="search" name="q" placeholder="Search resources..."
               value="<c:out value='${searchQuery}'/>">
    </form>

    <div class="topbar-right">
        <button class="icon-btn" type="button" aria-label="Notifications">
            <img src="${pageContext.request.contextPath}/images/home-icons/mail.png" alt="">
        </button>
        <a class="icon-btn" href="${pageContext.request.contextPath}/home#how-it-works" aria-label="Help">
            <img src="${pageContext.request.contextPath}/images/home-icons/book-open.png" alt="">
        </a>
        <button class="icon-btn" type="button" aria-label="Settings">
            <img src="${pageContext.request.contextPath}/images/home-icons/cog.png" alt="">
        </button>

        <div class="user-chip">
            <div class="user-info">
                <div class="user-name"><c:out value="${profile.name}"/></div>
                <div class="user-role">Student</div>
            </div>
            <div class="user-avatar">
                <c:choose>
                    <c:when test="${not empty profile.avatarUrl}">
                        <img src="${profile.avatarUrl}" alt="Profile">
                    </c:when>
                    <c:otherwise>
                        <c:out value="${profile.initials}"/>
                    </c:otherwise>
                </c:choose>
            </div>
            <img class="user-chevron" src="${pageContext.request.contextPath}/images/home-icons/link.png" alt="">
        </div>
    </div>
</header>
