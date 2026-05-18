<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Browse Resources — ScholarShare</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png" />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@500;600;700&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/student-dashboard.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/search_results.css" />
</head>
<body>

<jsp:include page="template/student-sidebar.jsp">
    <jsp:param name="activePage" value="browse" />
</jsp:include>

<div class="main student-main">

    <jsp:include page="template/student-header.jsp">
        <jsp:param name="pageTitle" value="Browse Resources" />
    </jsp:include>

    <main class="content student-content">


        <div class="browse-body">

            <%-- LEFT FILTER PANEL — Faculty → Subject → Topic --%>
            <aside class="filter-panel" aria-label="Filter by category">

                <p class="filter-panel__heading">Faculty</p>
                <ul class="filter-list">
                    <li>
                        <a href="${pageContext.request.contextPath}/browser"
                           class="${selectedFacultyId == 0 && empty keyword ? 'active' : ''}">
                            All Faculties
                        </a>
                    </li>
                    <c:forEach var="f" items="${faculties}">
                        <li>
                            <a href="${pageContext.request.contextPath}/browser?facultyId=${f.facultyId}"
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
                            <a href="${pageContext.request.contextPath}/browser?facultyId=${selectedFacultyId}"
                               class="${selectedSubjectId == 0 ? 'active' : ''}">
                                All Subjects
                            </a>
                        </li>
                        <c:forEach var="s" items="${subjects}">
                            <li>
                                <a href="${pageContext.request.contextPath}/browser?facultyId=${selectedFacultyId}&subjectId=${s.subjectId}"
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
                            <a href="${pageContext.request.contextPath}/browser?facultyId=${selectedFacultyId}&subjectId=${selectedSubjectId}"
                               class="${selectedTopicId == 0 ? 'active' : ''}">
                                All Topics
                            </a>
                        </li>
                        <c:forEach var="t" items="${topics}">
                            <li>
                                <a href="${pageContext.request.contextPath}/browser?facultyId=${selectedFacultyId}&subjectId=${selectedSubjectId}&topicId=${t.topicId}"
                                   class="${t.topicId == selectedTopicId ? 'active' : ''}">
                                    ${t.topicName}
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </c:if>

            </aside>

            <%-- RIGHT RESULTS AREA --%>
            <main class="results-area">

                <div class="results-header">
                    <h2>Resources</h2>
                    <span class="results-count">${resources.size()} found</span>
                </div>

                <%-- Active keyword bar --%>
                <c:if test="${not empty keyword}">
                    <div class="keyword-bar">
                        <img src="${pageContext.request.contextPath}/images/home-icons/search.png"
                             alt="" width="14" height="14"
                             style="vertical-align:middle;margin-right:5px;opacity:0.7;">
                        Showing results for: <strong>${keyword}</strong>
                        <a href="${pageContext.request.contextPath}/browser">Clear search</a>
                    </div>
                </c:if>

                <%-- RESOURCE CARDS or EMPTY STATE --%>
                <c:choose>
                    <c:when test="${empty resources}">
                        <div class="empty-state">
                            <img src="${pageContext.request.contextPath}/images/home-icons/folder-open.png"
                                 alt="" class="empty-state__icon-img" />
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

    </main>
</div>

<script src="${pageContext.request.contextPath}/js/student-dashboard.js"></script>
</body>
</html>
