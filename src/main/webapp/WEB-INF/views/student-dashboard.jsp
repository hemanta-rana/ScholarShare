<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ScholarShare | Student Dashboard</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/student-dashboard.css">
</head>
<body>

<jsp:include page="template/student-sidebar.jsp"/>

<div class="main student-main">
    <jsp:include page="template/student-header.jsp"/>

    <main class="content student-content">

        <c:if test="${not empty flashMessage}">
            <p class="flash flash-success"><c:out value="${flashMessage}"/></p>
        </c:if>
        <c:if test="${not empty flashError}">
            <p class="flash flash-error"><c:out value="${flashError}"/></p>
        </c:if>

        <section class="welcome-banner">
            <div class="welcome-banner-text">
                <h2>Welcome back, <c:out value="${profile.firstName}"/></h2>
                <p><c:out value="${welcomeMessage}"/></p>
            </div>
            <a href="${pageContext.request.contextPath}/student/upload-resource" class="welcome-upload-btn">
                <img src="${pageContext.request.contextPath}/images/home-icons/upload.png" alt="">
                Upload a Resource
            </a>
        </section>

        <section class="summary-row">
            <article class="summary-card">
                <div class="summary-card-top">
                    <span class="summary-label">Reputation Score</span>
                    <img class="summary-icon" src="${pageContext.request.contextPath}/images/home-icons/medal.png" alt="">
                </div>
                <div class="summary-value">
                    <c:out value="${summary.reputationScore}"/> / <c:out value="${summary.reputationMax}"/>
                </div>
                <div class="summary-progress">
                    <span style="width:${summary.reputationScore}%"></span>
                </div>
            </article>

            <article class="summary-card">
                <div class="summary-card-top">
                    <span class="summary-label">Approved Uploads</span>
                    <img class="summary-icon" src="${pageContext.request.contextPath}/images/home-icons/circle-check.png" alt="">
                </div>
                <div class="summary-value"><c:out value="${summary.approvedUploads}"/></div>
                <p class="summary-sub">
                    <img class="summary-sub-icon" src="${pageContext.request.contextPath}/images/home-icons/chart-line.png" alt="">
                    <c:out value="${summary.approvedDelta}"/> from last month
                </p>
            </article>

            <article class="summary-card">
                <div class="summary-card-top">
                    <span class="summary-label">Pending Review</span>
                    <img class="summary-icon" src="${pageContext.request.contextPath}/images/home-icons/file-text.png" alt="">
                </div>
                <div class="summary-value"><c:out value="${summary.pendingReview}"/></div>
                <p class="summary-sub"><c:out value="${summary.pendingEta}"/></p>
            </article>

            <article class="summary-card">
                <div class="summary-card-top">
                    <span class="summary-label">Saved Resources</span>
                    <img class="summary-icon" src="${pageContext.request.contextPath}/images/home-icons/save-to-collection.png" alt="">
                </div>
                <div class="summary-value"><c:out value="${summary.savedResources}"/></div>
                <p class="summary-sub"><c:out value="${summary.savedLabel}"/></p>
            </article>
        </section>

        <section class="dashboard-grid">
            <article class="card submissions-card" id="submissions">
                <div class="card-header">
                    <h3>Recent Submissions</h3>
                    <a href="${pageContext.request.contextPath}/student/dashboard">View All</a>
                </div>

                <c:choose>
                    <c:when test="${empty submissions}">
                        <div class="empty-panel">
                            <img src="${pageContext.request.contextPath}/images/home-icons/folder-open.png" alt="">
                            <p>No submissions yet. Upload your first resource to get started.</p>
                            <a href="${pageContext.request.contextPath}/student/upload-resource" class="welcome-upload-btn small">Upload Resource</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-wrap">
                            <table class="submissions-table">
                                <thead>
                                <tr>
                                    <th>Title</th>
                                    <th>Type</th>
                                    <th>Date</th>
                                    <th>Status</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="row" items="${submissions}">
                                    <tr>
                                        <td><c:out value="${row.title}"/></td>
                                        <td><c:out value="${row.type}"/></td>
                                        <td><c:out value="${row.date}"/></td>
                                        <td>
                                            <span class="status-badge status-${row.statusClass}">
                                                <c:out value="${row.status}"/>
                                            </span>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:otherwise>
                </c:choose>
            </article>

            <article class="card reputation-card" id="reputation">
                <div class="card-header">
                    <h3>Reputation Breakdown</h3>
                </div>
                <div class="reputation-ring-wrap">
                    <div class="reputation-ring" style="--score:${reputation.score}">
                        <div class="reputation-ring-inner">
                            <strong><c:out value="${reputation.score}"/>%</strong>
                            <span><c:out value="${reputation.tierLabel}"/></span>
                        </div>
                    </div>
                </div>
                <p class="reputation-message"><c:out value="${reputation.percentileMessage}"/></p>

                <div class="metric-row">
                    <div class="metric-label">
                        <span>Accuracy Rate</span>
                        <strong><c:out value="${reputation.accuracyRate}"/>%</strong>
                    </div>
                    <div class="metric-bar"><span style="width:${reputation.accuracyRate}%"></span></div>
                </div>
                <div class="metric-row">
                    <div class="metric-label">
                        <span>Peer Helpfulness</span>
                        <strong><c:out value="${reputation.peerHelpfulness}"/>%</strong>
                    </div>
                    <div class="metric-bar"><span style="width:${reputation.peerHelpfulness}%"></span></div>
                </div>
            </article>
        </section>

        <section class="recent-resources-section" id="recent-resources">
            <div class="recent-resources-header">
                <div>
                    <h3>Recently Added Resources</h3>
                    <p>Fresh peer-reviewed material from your faculty.</p>
                </div>
                <div class="carousel-controls">
                    <button type="button" class="carousel-btn" id="carouselPrev" aria-label="Previous">
                        <img src="${pageContext.request.contextPath}/images/home-icons/link.png" alt="" class="carousel-prev-icon">
                    </button>
                    <button type="button" class="carousel-btn" id="carouselNext" aria-label="Next">
                        <img src="${pageContext.request.contextPath}/images/home-icons/link.png" alt="" class="carousel-next-icon">
                    </button>
                </div>
            </div>

            <c:choose>
                <c:when test="${empty recentResources}">
                    <div class="empty-panel compact">
                        <p>No approved resources from other students yet.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="resource-carousel" id="resourceCarousel">
                        <c:forEach var="item" items="${recentResources}">
                            <article class="resource-tile">
                                <img class="resource-tile-icon" src="${pageContext.request.contextPath}/images/home-icons/file-text.png" alt="">
                                <h4><c:out value="${item.title}"/></h4>
                                <p class="resource-tile-meta"><c:out value="${item.type}"/> · <c:out value="${item.subject}"/></p>
                                <p class="resource-tile-date"><c:out value="${item.uploadDate}"/></p>
                                <p class="resource-tile-author">By <c:out value="${item.author}"/></p>
                            </article>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>

    </main>
</div>

<script src="${pageContext.request.contextPath}/js/student-dashboard.js"></script>
</body>
</html>
