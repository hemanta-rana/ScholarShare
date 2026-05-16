<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"  %>
<%@ taglib prefix="fn"  uri="jakarta.tags.functions" %>

<!Doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width",initial-scale="1.0, user-scalable=yes">
    <title>ScholarShare-StudentDashboard</title>
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=DM+Serif+Display:ital@0;1&family=DM+Sans:wght@400;500;600;700&display=swap" rel="stylesheet" />
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/headerFooter.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/studentDashboard.css"/>
    <script src="${pageContext.request.contextPath}/js/studentDashboard.js" defer></script>
</head>
<body>
<!-- ══════════════════════════════════════════
        1. NAVBAR
        ══════════════════════════════════════════ -->
<div id="navbar-wrapper">
    <nav id="navbar">
        <div class="nav-row">
            <a href="${pageContext.request.contextPath}/" class="nav-logo">
                <img scr="${pageContext.request.contextPath}/images/logo.png"
                     alt="ScholarShare" class="nav-logo-icon">
                <span>ScholarShare</span>
            </a>
            <ul class="nav-links">
                <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/home#categories">Browse Resources</a></li>
                <li><a href="${pageContext.request.contextPath}/home#how-it-works">How It Works</a></li>
                <li><a href="${pageContext.request.contextPath}/home#reputation">About</a></li>
                <li><a href="#footer">Contact</a></li>

            </ul>
            <a href="${pageContext.request.contextPath}/upload" class="nav-cta">Upload Resource</a>

            <button class="hamburger" id="hamburger" aria-label="Toggle menu">
            <span></span><span></span><span></span>
            </button>
        </div>

        <div class="nav-mobile" id="nav-mobile">
            <a href="${pageContext.request.contextPath}/home"              onclick="closeMenu()">Home</a>
            <a href="${pageContext.request.contextPath}/home#categories"   onclick="closeMenu()">Browse Resources</a>
            <a href="${pageContext.request.contextPath}/home#how-it-works" onclick="closeMenu()">How It Works</a>
            <a href="${pageContext.request.contextPath}/home#reputation"   onclick="closeMenu()">About</a>
            <a href="#footer"                                              onclick="closeMenu()">Contact</a>
            <a href="${pageContext.request.contextPath}/upload" class="mob-cta" onclick="closeMenu()">Upload Resource</a>
        </div>
    </nav>
</div>
<!-- ══════════════════════════════════════════
        2. DASHBOARD BODY
        ══════════════════════════════════════════ -->
<div class="dashboard-wrapper">
    <!-- ── Profile Hero ─────────────────────────── -->
    <div class="profile-hero">
        <div class="profile-hero_avatar">
            <c:choose>
                <c:when test="${not empty student.avatarUrl}">
                    <img src="${student.avatarUrl}" alt="Avatar"/>
                </c:when>
                <c:otherwise>
                    <span class="avatar-initials">
                        <c:out value="${fb:substring(student.fullname,0,1)}"/>
                    </span>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="profle-hero_info">
            <h1 class="profile-hero_name">
                Welcome back,<span><c:out value="${student.fullname}"/></span>
            </h1>
            <p class="profile-hero_email"><c:out value="${student.email}"/></p>
        </div>
        <a href="${pagecontext.request.contextpath}/upload" class="upload-btn">
            +upload Nre Resouce
        </a>
    </div>
    <!-- ── Server error banner ─────────────────── -->
    <c:if test="${not empty error}">
        <p class="alert-error"><c:out value="${error}"/></p>
    </c:if>
    <!-- ── Server error banner ─────────────────── -->
    <div class="stats-row">
        <div class="stat-card stat-card--total">
            <div class="stat-card_number">${stats.total}</div>
            <div class="stat-card_label">Total Uploads</div>
        </div>
        <div class="stat-card stat-card--pending">
            <div class="stat-card_number">${stats.pending}</div>
            <div class="stat-card_label">Pending</div>
        </div>
        <div class="stat-card stat-card--review">
            <div class="stat-card_number">${stats.underReview}</div>
            <div class="stat-card_label">Under Review</div>
        </div>
        <div class="stat-card stat-card--approved">
            <div class="stat-card_number">${stats.approved}</div>
            <div class="stat-card_label">Approved</div>
        </div>
        <div class="stat-card stat-card--rejected">
            <div class="stat-card_number">${stats.rejected}</div>
            <div class="stat-card_label">Rejected</div>
        </div>
    </div>

    <!-- ── Section Header + Filter Tabs ────────── -->
    <div class="section-header">
        <h2 class="section-title">My Sumitted Resources</h2>

        <div class="filter-tabs" id="filterTabs">
            <button class="filter-tab active" data-filter="All">All</button>
            <button class="filter-tab" data-filter="PENDING">Pending</button>
            <button class="filter-tab" data-filter="UNDER_REVIEW">Under Review</button>
            <button class="filter-tab" data-filter="APPROVED">Approved</button>
            <button class="filter-tab" data-filter="REJECTED">Rejected</button>
        </div>
    </div>

    <!-- ── Resource Cards (JSTL loop) ─────────── -->
    <c:choose>
        <c:when test="${empty resources}">
            <div class="empty-state">
                <div class="empty-state_icon">folder</div>
                <h3 class="empty-state_title">No Resouces Uploaded yet</h3>
                <p class="empty-state_sub">Start sharing your study materials with the community!</p>
                <a href="${pageContext.request.contextPath}/upload" class="upload-btn">
                    Upload Your First Resource
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="resource-list" id="resourceList">
                <c:forEach var="r" items="${resources}" varStatus="loop">
                    <div class="resource-card resource-card--${fn:toLowerCase(r.status)}"
                         data-status="${r.status}"
                         style="animation-delay:${loop.index * 0.07}s">
                        <!-- ① File-type icon -->
                        <div class="resource-card_icon file-icon--${fn:toLowerCase(r.fileType)}">
                            <c:choose>
                                <c:when test="${r.fileType eq 'PDF'}">📄</c:when>
                                <C:when test="${r.fileType eq'DOC' or r.fileType eq 'DOCX'}">📝</C:when>
                                <c:when test="${r.fileType eq 'PPT'  or r.fileType eq 'PPTX'}">📊</c:when>
                                <c:when test="${r.fileType eq 'MP4'  or r.fileType eq 'VIDEO'}">🎬</c:when>
                                <c:when test="${r.fileType eq 'ZIP'}">🗜️</c:when>
                                <c:when test="${r.fileType eq 'PNG'  or r.fileType eq 'JPG' or r.fileType eq 'IMG'}">🖼️</c:when>
                                <c:otherwise>📁</c:otherwise>
                            </c:choose>
                        </div>

                        <!-- ② Main body -->
                        <div class="resource-card__body">
                            <h3 class="resource-card__title">
                                <c:out value="${r.title}"/>
                            </h3>

                            <div class="resource-card__tags">
                                    <span class="tag tag--type">
                                        <c:out value="${r.fileType}"/>
                                    </span>
                                <span class="tag tag--subject">
                                        <c:out value="${r.subject}"/>
                                    </span>
                                <c:if test="${not empty r.fileSize}">
                                        <span class="tag tag--size">
                                            <c:out value="${r.fileSize}"/>
                                        </span>
                                </c:if>
                            </div>

                            <div class="resource-card_meta">
                                <span>Uploaded:&nbsp;
                                    <strong>
                                        <fmt:formatDate value="${r.uploadDate}" pattern="MMM dd, YYYY"/>
                                    </strong>
                                </span>
                                <C:if test="${not empty r.lastUpdated}">
                                    <span>Last Updated:&nbsp;
                                        <fmt:formatDate value="${r.lastUpdated}" pattern="MMM dd,YYYY"/>
                                    </span>
                                </C:if>
                            </div>

                            <!-- Reviewer note -->
                            <c:if test="${not empty r.reviewNote}">
                                <div class="reviewer-note reviewer-note--${fn:toLowerCase(r.status)}">
                                    <c:choose>
                                        <c:when test="${r.status eq 'Approved'}">✅</c:when>
                                        <c:when test="${r.status eq 'REJECTED'}">❌</c:when>
                                        <c:otherwise>💬</c:otherwise>
                                    </c:choose>
                                    <span>
                                        <strong>Reviewer:</strong>
                                        <c:out value="${r.reviewerNote}"/>
                                    </span>
                                </div>
                            </c:if>

                            <!-- Status pipeline -->
                            <div class="pipeline">
                                <div class="pipeline_dot"></div>
                                <span class="pipeline_label">Uploaded</span>
                            </div>

                            <div class="pipeline_line ${(r.status eq 'UNDER_REVIEW' or r.status eq 'APPROVED' or r.status eq 'REJECTED') ? 'pipeline_line--filled': ''}"></div>

                            <div class="pipeline__step
                                        ${r.status eq 'UNDER_REVIEW' ? 'pipeline__step--active' : ''}
                                        ${(r.status eq 'APPROVED' or r.status eq 'REJECTED') ? 'pipeline__step--done' : ''}">
                                <div class="pipeline__dot"></div>
                                <span class="pipeline__label">Under Review</span>
                            </div>

                            <div class="pipeline__line
                                        ${r.status eq 'APPROVED' ? 'pipeline__line--approved' : ''}
                                        ${r.status eq 'REJECTED' ? 'pipeline__line--rejected' : ''}"></div>

                            <div class="pipeline__step
                                        ${r.status eq 'APPROVED' ? 'pipeline__step--approved' : ''}
                                        ${r.status eq 'REJECTED' ? 'pipeline__step--rejected' : ''}">
                                <div class="pipeline__dot"></div>
                                <span class="pipeline__label">
                                            <c:choose>
                                                <c:when test="${r.status eq 'APPROVED'}">Approved</c:when>
                                                <c:when test="${r.status eq 'REJECTED'}">Rejected</c:when>
                                                <c:otherwise>Decision</c:otherwise>
                                            </c:choose>
                                        </span>
                            </div>
                        </div><%-- end pipeline --%>
                    </div><%-- end resource-card__body --%>

                    <!-- ③ Right: badge + resubmit -->
                    <div class="resource-card_aside">
                        <span class="status-badge status-badge--${fn:toLowerCase(r.status)}">
                            <span class="status-badge_dot"></span>
                            <c:choose>
                                <c:when test="${r.status eq 'UNDER_REVIEW'}">Under Review</c:when>
                                <c:when test="${r.status eq 'PENDING'}">Pending</c:when>
                                <c:when test="${r.status eq 'APPROVED'}">Approved</c:when>
                                <c:when test="${r.status eq 'REJECTED'}">Rejected</c:when>
                                <c:otherwise><c:out value="${r.status}"/></c:otherwise>
                            </c:choose>
                        </span>

                        <c:if test="${r.status eq 'REJECTED'}">
                            <a href="${pageContext.request.contextPath}/upload?resubmit=${r.id}"
                               class="resubmit-btn">Resubmit</a>
                        </c:if>
                    </div>

            </div><%-- end resource-card --%>
                </c:forEach>
            </div><%-- end resource-list --%>
        </c:otherwise>
    </c:choose>

<!-- JS-driven empty state (filter results in zero) -->
<div class="empty-state" id="jsEmptyState" style="display:none;">
    <div class="empty-state__icon">🔍</div>
    <h3 class="empty-state__title">No resources match this filter</h3>
    <p class="empty-state__sub">Try selecting a different status tab.</p>
</div>
</div><%-- end dashboard-wrapper --%>

<!-- ══════════════════════════════════════════
         3. FOOTER
         ══════════════════════════════════════════ -->
<footer id="footer">
    <p>© 2025 ScholarShare. All rights reserved.</p>
</footer>

<script src="${pageContext.request.contextPath}/js/home.js"></script>
</body>
</html>