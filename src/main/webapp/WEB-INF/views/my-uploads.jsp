<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ScholarShare | My Uploads</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/student-dashboard.css">
</head>
<body>

<jsp:include page="template/student-sidebar.jsp">
    <jsp:param name="activePage" value="myuploads" />
</jsp:include>

<div class="main student-main">

    <jsp:include page="template/student-header.jsp">
        <jsp:param name="pageTitle" value="My Uploads" />
    </jsp:include>

    <main class="content student-content">

        <%-- Flash messages --%>
        <c:if test="${not empty flashMessage}">
            <p class="flash flash-success"><c:out value="${flashMessage}"/></p>
        </c:if>
        <c:if test="${not empty flashError}">
            <p class="flash flash-error"><c:out value="${flashError}"/></p>
        </c:if>

        <%-- ── PAGE HEADER ── --%>
        <div class="uploads-page-header">
            <div>
                <h2 class="uploads-page-title">My Uploaded Resources</h2>
                <p class="uploads-page-sub">All resources you have submitted to the institutional archive.</p>
            </div>
            <a href="${pageContext.request.contextPath}/student/upload-resource"
               class="welcome-upload-btn">
                <img src="${pageContext.request.contextPath}/images/home-icons/upload.png" alt="">
                Upload New Resource
            </a>
        </div>

        <%-- ── FILTER + SEARCH BAR ── --%>
        <div class="uploads-toolbar">

            <%-- Status filter tabs --%>
            <div class="uploads-tabs">
                <a href="${pageContext.request.contextPath}/student/uploads?status=all"
                   class="uploads-tab ${statusFilter eq 'all' ? 'active' : ''}">
                    All
                    <span class="uploads-tab-count">${totalCount}</span>
                </a>
                <a href="${pageContext.request.contextPath}/student/uploads?status=approved"
                   class="uploads-tab ${statusFilter eq 'approved' ? 'active' : ''}">
                    Approved
                    <span class="uploads-tab-count uploads-tab-count--approved">${approvedCount}</span>
                </a>
                <a href="${pageContext.request.contextPath}/student/uploads?status=pending"
                   class="uploads-tab ${statusFilter eq 'pending' ? 'active' : ''}">
                    Pending
                    <span class="uploads-tab-count uploads-tab-count--pending">${pendingCount}</span>
                </a>
                <a href="${pageContext.request.contextPath}/student/uploads?status=rejected"
                   class="uploads-tab ${statusFilter eq 'rejected' ? 'active' : ''}">
                    Rejected
                    <span class="uploads-tab-count uploads-tab-count--rejected">${rejectedCount}</span>
                </a>
            </div>

            <%-- Keyword search --%>
            <form class="uploads-search" method="get"
                  action="${pageContext.request.contextPath}/student/uploads">
                <input type="hidden" name="status" value="${statusFilter}">
                <div class="uploads-search-wrap">
                    <img src="${pageContext.request.contextPath}/images/home-icons/search.png"
                         alt="" class="uploads-search-icon">
                    <input class="uploads-search-input" type="search" name="q"
                           placeholder="Search your uploads…"
                           value="<c:out value='${searchQuery}'/>">
                </div>
            </form>
        </div>

        <%-- ── UPLOADS TABLE ── --%>
        <div class="card" style="padding:0; overflow:hidden;">

            <c:choose>
                <c:when test="${empty uploads}">
                    <div class="empty-panel" style="padding:56px 24px;">
                        <img src="${pageContext.request.contextPath}/images/home-icons/folder-open.png" alt="">
                        <p style="font-size:15px; font-weight:600; color:var(--student-navy); margin-bottom:6px;">
                            <c:choose>
                                <c:when test="${not empty searchQuery}">No results for "<c:out value='${searchQuery}'/>"</c:when>
                                <c:when test="${statusFilter ne 'all'}">No ${statusFilter} uploads yet.</c:when>
                                <c:otherwise>You haven't uploaded any resources yet.</c:otherwise>
                            </c:choose>
                        </p>
                        <p style="font-size:13px; color:var(--student-muted); margin-bottom:20px;">
                            <c:choose>
                                <c:when test="${not empty searchQuery}">Try a different search term.</c:when>
                                <c:otherwise>Share your academic work with the community.</c:otherwise>
                            </c:choose>
                        </p>
                        <c:if test="${empty searchQuery}">
                            <a href="${pageContext.request.contextPath}/student/upload-resource"
                               class="welcome-upload-btn small">
                                <img src="${pageContext.request.contextPath}/images/home-icons/upload.png" alt="">
                                Upload a Resource
                            </a>
                        </c:if>
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="table-wrap">
                        <table class="submissions-table uploads-table">
                            <thead>
                                <tr>
                                    <th>Title</th>
                                    <th>Type</th>
                                    <th>Subject</th>
                                    <th>Uploaded</th>
                                    <th>Status</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="row" items="${uploads}">
                                    <tr>
                                        <td>
                                            <span class="upload-title">
                                                <c:out value="${row.title}"/>
                                            </span>
                                        </td>
                                        <td>
                                            <span class="upload-type-badge">
                                                <c:out value="${row.type}"/>
                                            </span>
                                        </td>
                                        <td>
                                            <span style="font-size:13px; color:var(--student-muted);">
                                                <c:out value="${row.subject}"/>
                                            </span>
                                        </td>
                                        <td>
                                            <span style="font-size:13px; color:var(--student-muted);">
                                                <c:out value="${row.date}"/>
                                            </span>
                                        </td>
                                        <td>
                                            <span class="status-badge status-${row.statusClass}">
                                                <c:out value="${row.status}"/>
                                            </span>
                                        </td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/resource?id=${row.id}"
                                               class="upload-view-link">
                                                View
                                                <img src="${pageContext.request.contextPath}/images/home-icons/link.png"
                                                     alt="" width="11" height="11">
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <%-- Row count footer --%>
                    <div style="padding:12px 20px; border-top:1px solid var(--student-border);
                                font-size:12.5px; color:var(--student-muted); text-align:right;">
                        Showing <strong>${uploads.size()}</strong>
                        <c:choose>
                            <c:when test="${statusFilter ne 'all'}"> ${statusFilter}</c:when>
                        </c:choose>
                        upload<c:if test="${uploads.size() != 1}">s</c:if>
                    </div>
                </c:otherwise>
            </c:choose>

        </div><%-- end .card --%>

    </main>
</div>

<script src="${pageContext.request.contextPath}/js/student-dashboard.js"></script>
</body>
</html>
