<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>${resource.title} — ScholarShare</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/resource_detail.css" />
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
    <div class="detail-main">

        <%-- TOP HEADER — breadcrumb + back link --%>
        <header class="detail-header">
            <a href="${pageContext.request.contextPath}/browse" class="back-link">← Back to Browse</a>

            <nav class="breadcrumb" aria-label="Breadcrumb">
                <a href="${pageContext.request.contextPath}/browse">Browse</a>
                <span class="breadcrumb__sep">›</span>
                <span class="breadcrumb__current">${resource.title}</span>
            </nav>
        </header>

        <div class="detail-body">

            <%-- =========================================================
                 MAIN DETAIL CARD
                 ========================================================= --%>
            <div class="detail-card">

                <%-- Type + Status badges --%>
                <div class="detail-card__meta-row">
                    <span class="badge">${resource.resourceType}</span>
                    <span class="badge badge--${resource.status}">${resource.status}</span>
                </div>

                <%-- Title --%>
                <h1 class="detail-card__title">${resource.title}</h1>

                <%-- Description --%>
                <p class="detail-card__description">${resource.description}</p>

                <%-- Info rows --%>
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

                <%-- Download button --%>
                <a href="${pageContext.request.contextPath}/${resource.filePath}"
                   download
                   class="btn-download">
                    ⬇ Download Resource
                </a>

            </div><%-- end .detail-card --%>

            <%-- =========================================================
                 RATING SECTION — Hemanta will implement here
                 ========================================================= --%>
            <div class="section-panel" id="rating-section">
                <h2 class="section-panel__title">⭐ Ratings</h2>
                <!-- RATING SECTION — Hemanta will implement here -->
                <div class="section-panel__placeholder">
                    Rating UI coming soon.
                </div>
            </div>

            <%-- =========================================================
                 FLAG SECTION — Sajan will implement here
                 ========================================================= --%>
            <div class="section-panel" id="flag-section">
                <h2 class="section-panel__title">🚩 Report Resource</h2>
                <!-- FLAG SECTION — Sajan will implement here -->
                <div class="section-panel__placeholder">
                    Flagging UI coming soon.
                </div>
            </div>

            <%-- =========================================================
                 ADD TO COLLECTION — Task 7 Step 18
                 ========================================================= --%>
            <div class="section-panel" id="collection-section">
                <h2 class="section-panel__title">📚 Add to Collection</h2>

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
                        <%-- No collections yet — show placeholder button (Task 7 Step 18) --%>
                        <p style="font-size:13.5px; color:#6b7a99; margin-bottom:12px;">
                            You have no collections yet.
                        </p>
                        <button id="btn-add-collection" type="button" class="btn-add-collection">
                            + Add to Collection
                        </button>
                        <p style="font-size:12px; color:#8a95a8; margin-top:8px;">
                            <a href="${pageContext.request.contextPath}/collections">Create a collection first</a>
                        </p>
                    </c:otherwise>
                </c:choose>

            </div>

        </div><%-- end .detail-body --%>

    </div><%-- end .detail-main --%>

</body>
</html>
