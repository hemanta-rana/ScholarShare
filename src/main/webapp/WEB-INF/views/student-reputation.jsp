<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ScholarShare | Reputation</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/student-dashboard.css">
</head>
<body>

<jsp:include page="template/student-sidebar.jsp">
    <jsp:param name="activePage" value="reputation" />
</jsp:include>

<div class="main student-main">
    <jsp:include page="template/student-header.jsp">
        <jsp:param name="pageTitle" value="Reputation" />
    </jsp:include>

    <main class="content student-content">

        <%-- ── Hero banner ── --%>
        <section class="welcome-banner">
            <div class="welcome-banner-text">
                <h2>Your Reputation Score</h2>
                <p>Track your academic contributions, peer reviews, and helpfulness metrics across the platform.</p>
            </div>
        </section>

        <%-- ── Summary cards row ── --%>
        <section class="summary-row reputation-summary-row">
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
        </section>

        <%-- ── Reputation Breakdown ── --%>
        <section class="dashboard-grid reputation-detail-grid">
            <article class="card reputation-card">
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

            <article class="card">
                <div class="card-header">
                    <h3>Tier Progress</h3>
                </div>
                <div class="tier-progress-list">
                    <div class="tier-item ${reputation.score >= 85 ? 'tier-item--active' : ''}">
                        <div class="tier-icon">
                            <img src="${pageContext.request.contextPath}/images/home-icons/medal.png" alt="" width="20" height="20">
                        </div>
                        <div class="tier-info">
                            <strong>Top Tier</strong>
                            <span>85 – 100 points</span>
                        </div>
                        <c:if test="${reputation.score >= 85}">
                            <span class="tier-badge">Current</span>
                        </c:if>
                    </div>
                    <div class="tier-item ${reputation.score >= 70 && reputation.score < 85 ? 'tier-item--active' : ''}">
                        <div class="tier-icon">
                            <img src="${pageContext.request.contextPath}/images/home-icons/medal.png" alt="" width="20" height="20">
                        </div>
                        <div class="tier-info">
                            <strong>Gold Tier</strong>
                            <span>70 – 84 points</span>
                        </div>
                        <c:if test="${reputation.score >= 70 && reputation.score < 85}">
                            <span class="tier-badge">Current</span>
                        </c:if>
                    </div>
                    <div class="tier-item ${reputation.score >= 50 && reputation.score < 70 ? 'tier-item--active' : ''}">
                        <div class="tier-icon">
                            <img src="${pageContext.request.contextPath}/images/home-icons/medal.png" alt="" width="20" height="20">
                        </div>
                        <div class="tier-info">
                            <strong>Silver Tier</strong>
                            <span>50 – 69 points</span>
                        </div>
                        <c:if test="${reputation.score >= 50 && reputation.score < 70}">
                            <span class="tier-badge">Current</span>
                        </c:if>
                    </div>
                    <div class="tier-item ${reputation.score < 50 ? 'tier-item--active' : ''}">
                        <div class="tier-icon">
                            <img src="${pageContext.request.contextPath}/images/home-icons/medal.png" alt="" width="20" height="20">
                        </div>
                        <div class="tier-info">
                            <strong>Bronze Tier</strong>
                            <span>0 – 49 points</span>
                        </div>
                        <c:if test="${reputation.score < 50}">
                            <span class="tier-badge">Current</span>
                        </c:if>
                    </div>
                </div>
            </article>
        </section>

    </main>
</div>

<script src="${pageContext.request.contextPath}/js/student-dashboard.js"></script>
</body>
</html>
