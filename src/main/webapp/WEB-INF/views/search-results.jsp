<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Browse Resources — ScholarShare</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/search_results.css" />
</head>
<body>

    <%-- =========================================================
         SIDEBAR NAVIGATION (same structure as student_dashboard.jsp)
         ========================================================= --%>
    <aside class="sidebar" id="sidebar-panel" aria-label="Main navigation">
        <div class="sidebar-brand">
            <div class="brand-icon">
                <span style="font-size:22px; display:flex; align-items:center; justify-content:center; height:100%;">📖</span>
            </div>
            <div class="brand-text">
                <span class="brand-name">ScholarShare</span>
                <span class="brand-sub">University Portal</span>
            </div>
        </div>

        <nav class="sidebar-nav" aria-label="Section links">
            <a href="${pageContext.request.contextPath}/student-dashboard" class="nav-item">
                <span>▣</span><span>Dashboard</span>
            </a>
            <a href="${pageContext.request.contextPath}/browse" class="nav-item active">
                <span>🌐</span><span>Browse</span>
            </a>
            <a href="${pageContext.request.contextPath}/collections" class="nav-item">
                <span>📚</span><span>Collections</span>
            </a>
            <a href="#" class="nav-item">
                <span>📤</span><span>My Uploads</span>
            </a>
        </nav>

        <div class="sidebar-logout">
            <a href="${pageContext.request.contextPath}/logout" class="logout-btn">
                <span>🚪</span><span>Sign Out</span>
            </a>
        </div>
    </aside>

    <%-- =========================================================
         MAIN CONTENT AREA
         ========================================================= --%>
    <div class="browse-main">

        <%-- TOP HEADER with page title and keyword search form --%>
        <header class="browse-header">
            <h1>Browse Resources</h1>

            <form class="search-bar" action="${pageContext.request.contextPath}/browse" method="get" role="search">
                <label class="visually-hidden" for="search-q">Search resources</label>
                <input id="search-q"
                       type="search"
                       name="q"
                       value="${keyword}"
                       placeholder="Search resources..." />
                <button type="submit">Search</button>
            </form>
        </header>

        <div class="browse-body">

            <%-- =========================================================
                 LEFT FILTER PANEL — Faculty → Subject → Topic
                 ========================================================= --%>
            <aside class="filter-panel" aria-label="Filter by category">

                <%-- FACULTIES --%>
                <p class="filter-panel__heading">Faculty</p>
                <ul class="filter-list">
                    <li>
                        <a href="${pageContext.request.contextPath}/browse"
                           class="${selectedFacultyId == 0 && empty keyword ? 'active' : ''}">
                            All Faculties
                        </a>
                    </li>
                    <c:forEach var="f" items="${faculties}">
                        <li>
                            <a href="${pageContext.request.contextPath}/browse?facultyId=${f.facultyId}"
                               class="${f.facultyId == selectedFacultyId ? 'active' : ''}">
                                ${f.facultyName}
                            </a>
                        </li>
                    </c:forEach>
                </ul>

                <%-- SUBJECTS (only shown when a faculty is selected) --%>
                <c:if test="${selectedFacultyId > 0 && not empty subjects}">
                    <div class="filter-divider"></div>
                    <p class="filter-sub-heading">Subject</p>
                    <ul class="filter-list">
                        <li>
                            <a href="${pageContext.request.contextPath}/browse?facultyId=${selectedFacultyId}"
                               class="${selectedSubjectId == 0 ? 'active' : ''}">
                                All Subjects
                            </a>
                        </li>
                        <c:forEach var="s" items="${subjects}">
                            <li>
                                <a href="${pageContext.request.contextPath}/browse?facultyId=${selectedFacultyId}&subjectId=${s.subjectId}"
                                   class="${s.subjectId == selectedSubjectId ? 'active' : ''}">
                                    ${s.subjectName}
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </c:if>

                <%-- TOPICS (only shown when a subject is selected) --%>
                <c:if test="${selectedSubjectId > 0 && not empty topics}">
                    <div class="filter-divider"></div>
                    <p class="filter-sub-heading">Topic</p>
                    <ul class="filter-list">
                        <li>
                            <a href="${pageContext.request.contextPath}/browse?facultyId=${selectedFacultyId}&subjectId=${selectedSubjectId}"
                               class="${selectedTopicId == 0 ? 'active' : ''}">
                                All Topics
                            </a>
                        </li>
                        <c:forEach var="t" items="${topics}">
                            <li>
                                <a href="${pageContext.request.contextPath}/browse?facultyId=${selectedFacultyId}&subjectId=${selectedSubjectId}&topicId=${t.topicId}"
                                   class="${t.topicId == selectedTopicId ? 'active' : ''}">
                                    ${t.topicName}
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </c:if>

            </aside>

            <%-- =========================================================
                 RIGHT RESULTS AREA
                 ========================================================= --%>
            <main class="results-area">

                <div class="results-header">
                    <h2>Resources</h2>
                    <span class="results-count">${resources.size()} found</span>
                </div>

                <%-- Show active keyword bar when searching --%>
                <c:if test="${not empty keyword}">
                    <div class="keyword-bar">
                        🔍 Showing results for: <strong>${keyword}</strong>
                        <a href="${pageContext.request.contextPath}/browse">Clear search</a>
                    </div>
                </c:if>

                <%-- RESOURCE CARDS or EMPTY STATE --%>
                <c:choose>
                    <c:when test="${empty resources}">
                        <div class="empty-state">
                            <span class="empty-state__icon">📭</span>
                            <p class="empty-state__title">No resources found</p>
                            <p class="empty-state__msg">
                                Try selecting a different category or use a different keyword.
                            </p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="resource-grid">
                            <c:forEach var="r" items="${resources}">
                                <a href="${pageContext.request.contextPath}/resource?id=${r.resourceId}"
                                   class="resource-card">
                                    <span class="resource-card__type">${r.resourceType}</span>
                                    <h3 class="resource-card__name">${r.title}</h3>
                                    <p class="resource-card__meta">${r.description}</p>
                                </a>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>

            </main>

        </div><%-- end .browse-body --%>

    </div><%-- end .browse-main --%>

</body>
</html>
