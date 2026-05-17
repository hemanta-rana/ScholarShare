<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>My Collections — ScholarShare</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/collections.css" />
</head>
<body>

    <%-- =========================================================
         SIDEBAR NAVIGATION
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
            <a href="${pageContext.request.contextPath}/browse" class="nav-item">
                <span>🌐</span><span>Browse</span>
            </a>
            <a href="${pageContext.request.contextPath}/collections" class="nav-item active">
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
    <div class="collections-main">

        <%-- PAGE HEADER --%>
        <header class="collections-header">
            <h1>My Collections</h1>
        </header>

        <div class="collections-body">

            <%-- =========================================================
                 CREATE NEW COLLECTION FORM
                 ========================================================= --%>
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

            <%-- =========================================================
                 COLLECTION LIST
                 ========================================================= --%>
            <c:choose>

                <%-- EMPTY STATE --%>
                <c:when test="${empty collections}">
                    <div class="empty-state">
                        <span class="empty-state__icon">📂</span>
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

                                <form action="${pageContext.request.contextPath}/collections"
                                      method="post">
                                    <input type="hidden" name="action" value="delete" />
                                    <input type="hidden" name="collectionId" value="${col.collectionId}" />
                                    <button type="submit" class="btn-delete">🗑 Delete</button>
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
                                                    <a href="${pageContext.request.contextPath}/resource?id=${r.resourceId}"
                                                       class="resource-row__title">
                                                        ${r.title}
                                                    </a>
                                                    <span class="resource-row__type">${r.resourceType}</span>
                                                </div>

                                                <form action="${pageContext.request.contextPath}/collections"
                                                      method="post">
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

        </div><%-- end .collections-body --%>

    </div><%-- end .collections-main --%>

</body>
</html>
