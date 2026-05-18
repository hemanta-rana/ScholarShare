<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>My Collections — ScholarShare</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png" />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@500;600;700&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/student-dashboard.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/collections.css" />
</head>
<body>

<jsp:include page="template/student-sidebar.jsp">
    <jsp:param name="activePage" value="collections" />
</jsp:include>

<div class="main student-main">

    <jsp:include page="template/student-header.jsp">
        <jsp:param name="pageTitle" value="My Collections" />
    </jsp:include>

    <main class="content student-content">

        <header class="upload-page-header">
            <h2>My Collections</h2>
            <p class="upload-page-sub">Organise your saved resources into study folders.</p>
        </header>

        <%-- CREATE NEW COLLECTION FORM --%>
        <form class="create-form-card"
              action="${pageContext.request.contextPath}/collections"
              method="post">
            <input type="hidden" name="action" value="create" />
            <label for="collectionName">New collection:</label>
            <input id="collectionName"
                   type="text"
                   name="collectionName"
                   placeholder="e.g. Exam Revision, Java Notes..."
                   required />
            <button type="submit" class="btn-create">+ Create</button>
        </form>

        <%-- COLLECTION LIST --%>
        <c:choose>

            <%-- EMPTY STATE --%>
            <c:when test="${empty collections}">
                <div class="empty-state">
                    <img src="${pageContext.request.contextPath}/images/home-icons/folder-open.png"
                         alt="" class="empty-state__icon-img" />
                    <p class="empty-state__title">No collections yet</p>
                    <p class="empty-state__msg">
                        Use the form above to create your first study folder.
                    </p>
                </div>
            </c:when>

            <%-- COLLECTION PANELS --%>
            <c:otherwise>
                <c:forEach var="col" items="${collections}">
                    <div class="collection-panel">

                        <%-- Collection header: name + delete --%>
                        <div class="collection-panel__head">
                            <div>
                                <span class="collection-panel__name">${col.collectionName}</span>
                                <c:choose>
                                    <c:when test="${empty col.resources}">
                                        <span class="collection-panel__count">0 resources</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="collection-panel__count">${col.resources.size()} resource(s)</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <form action="${pageContext.request.contextPath}/collections" method="post">
                                <input type="hidden" name="action" value="delete" />
                                <input type="hidden" name="collectionId" value="${col.collectionId}" />
                                <button type="submit" class="btn-delete">
                                    <img src="${pageContext.request.contextPath}/images/home-icons/triangle-alert.png"
                                         alt="" width="13" height="13" style="vertical-align:middle;margin-right:4px;">
                                    Delete
                                </button>
                            </form>
                        </div>

                        <%-- Resource list inside collection --%>
                        <div class="collection-resources">
                            <c:choose>
                                <c:when test="${empty col.resources}">
                                    <p class="collection-empty-msg">No resources saved yet.</p>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="r" items="${col.resources}">
                                        <div class="resource-row">
                                            <div class="resource-row__info">
                                                <img src="${pageContext.request.contextPath}/images/home-icons/file-text.png"
                                                     alt="" width="14" height="14"
                                                     style="opacity:0.6;flex-shrink:0;margin-right:6px;">
                                                <a href="${pageContext.request.contextPath}/resource?id=${r.resourceId}"
                                                   class="resource-row__title">
                                                    ${r.title}
                                                </a>
                                                <span class="resource-row__type">${r.resourceType}</span>
                                            </div>

                                            <form action="${pageContext.request.contextPath}/collections" method="post">
                                                <input type="hidden" name="action" value="remove" />
                                                <input type="hidden" name="collectionId" value="${col.collectionId}" />
                                                <input type="hidden" name="resourceId" value="${r.resourceId}" />
                                                <button type="submit" class="btn-remove">Remove</button>
                                            </form>
                                        </div>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </div>

                    </div><%-- end .collection-panel --%>
                </c:forEach>
            </c:otherwise>

        </c:choose>

    </main>
</div>

<script src="${pageContext.request.contextPath}/js/student-dashboard.js"></script>
</body>
</html>
