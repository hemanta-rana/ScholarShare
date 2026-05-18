<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>${resource.title} — ScholarShare</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png" />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@500;600;700&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/student-dashboard.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/resource_detail.css" />
</head>
<body>

<jsp:include page="template/student-sidebar.jsp">
    <jsp:param name="activePage" value="browse" />
</jsp:include>

<div class="main student-main">

    <jsp:include page="template/student-header.jsp">
        <jsp:param name="pageTitle" value="Resource Detail" />
    </jsp:include>

    <main class="content student-content">

        <%-- BREADCRUMB / BACK LINK --%>
        <header class="detail-header">
            <a href="${pageContext.request.contextPath}/browser" class="back-link">
                <img src="${pageContext.request.contextPath}/images/home-icons/link.png"
                     alt="" width="14" height="14"
                     style="transform:rotate(180deg);vertical-align:middle;margin-right:4px;">
                Back to Browse
            </a>

            <nav class="breadcrumb" aria-label="Breadcrumb">
                <a href="${pageContext.request.contextPath}/browser">Browse</a>
                <span class="breadcrumb__sep">›</span>
                <span class="breadcrumb__current">${resource.title}</span>
            </nav>
        </header>

        <div class="detail-body">

            <%-- MAIN DETAIL CARD --%>
            <div class="detail-card">

                <div class="detail-card__meta-row">
                    <span class="badge">${resource.resourceType}</span>
                    <span class="badge badge--${resource.status}">${resource.status}</span>
                </div>

                <h1 class="detail-card__title">${resource.title}</h1>

                <p class="detail-card__description">${resource.description}</p>

                <div class="detail-card__info">
                    <div class="detail-card__info-item">
                        <span class="detail-card__info-label">Uploaded on</span>
                        <span>${resource.uploadDate}</span>
                    </div>
                    <div class="detail-card__info-item">
                        <span class="detail-card__info-label">Self-declared</span>
                        <span>${resource.selfDeclaration ? 'Yes' : 'No'}</span>
                    </div>
                </div>

                <a href="${pageContext.request.contextPath}/${resource.filePath}"
                   download
                   class="btn-download">
                    <img src="${pageContext.request.contextPath}/images/home-icons/upload.png"
                         alt="" width="15" height="15"
                         style="transform:rotate(180deg);vertical-align:middle;margin-right:6px;">
                    Download Resource
                </a>

            </div><%-- end .detail-card --%>

            <%-- RATING SECTION --%>
            <div class="section-panel" id="rating-section">
                <h2 class="section-panel__title">
                    <img src="${pageContext.request.contextPath}/images/home-icons/trophy.png"
                         alt="" width="18" height="18"
                         style="vertical-align:middle;margin-right:6px;opacity:0.8;">
                    Ratings
                </h2>
                <div class="section-panel__placeholder">
                    Rating UI coming soon.
                </div>
            </div>

            <%-- FLAG SECTION --%>
            <div class="section-panel" id="flag-section">
                <h2 class="section-panel__title">
                    <img src="${pageContext.request.contextPath}/images/home-icons/flag-content.png"
                         alt="" width="18" height="18"
                         style="vertical-align:middle;margin-right:6px;opacity:0.8;">
                    Report Resource
                </h2>
                <div class="section-panel__placeholder">
                    Flagging UI coming soon.
                </div>
            </div>

            <%-- ADD TO COLLECTION --%>
            <div class="section-panel" id="collection-section">
                <h2 class="section-panel__title">
                    <img src="${pageContext.request.contextPath}/images/home-icons/save-to-collection.png"
                         alt="" width="18" height="18"
                         style="vertical-align:middle;margin-right:6px;opacity:0.8;">
                    Add to Collection
                </h2>

                <c:choose>
                    <c:when test="${not empty collections}">
                        <form class="collection-form"
                              action="${pageContext.request.contextPath}/collections"
                              method="post">
                            <input type="hidden" name="action" value="add" />
                            <input type="hidden" name="resourceId" value="${resource.resourceId}" />

                            <label for="collectionSelect">Save to:</label>
                            <select id="collectionSelect" name="collectionId">
                                <c:forEach var="col" items="${collections}">
                                    <option value="${col.collectionId}">${col.collectionName}</option>
                                </c:forEach>
                            </select>
                            <button type="submit" class="btn-save-collection">Save</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <p style="font-size:13.5px; color:#6b7a99; margin-bottom:12px;">
                            You have no collections yet.
                        </p>
                        <a href="${pageContext.request.contextPath}/collections" class="btn-save-collection">
                            + Create a Collection
                        </a>
                    </c:otherwise>
                </c:choose>

            </div>

        </div><%-- end .detail-body --%>

    </main>
</div>

<script src="${pageContext.request.contextPath}/js/student-dashboard.js"></script>
</body>
</html>
