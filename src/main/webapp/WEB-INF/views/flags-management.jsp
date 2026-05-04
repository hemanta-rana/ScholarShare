<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>ScholarShare | Flags Management</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png" />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@400;500;600;700&display=swap" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/css/sidebar.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/adminDashboard.css" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/css/flag-management.css" rel="stylesheet" />
</head>
<body>

<jsp:include page="template/admin-sidebar.jsp">
    <jsp:param name="activePage" value="flags" />
</jsp:include>

<div class="main">
    <jsp:include page="template/admin-header.jsp">
        <jsp:param name="searchPlaceholder" value="Search flags, resources, users..." />
    </jsp:include>

    <main class="content">
        <div class="page-header">
            <div>
                <div class="page-title">
                    Flags Management
                    <span class="flag-count-badge">
                        <svg viewBox="0 0 24 24" fill="currentColor" width="10" height="10"><path d="M4 15s1-1 4-1 5 2 8 2 4-1 4-1V3s-1 1-4 1-5-2-8-2-4 1-4 1z"/></svg>
                        <c:out value="${empty stats.openFlags ? '4' : stats.openFlags}" /> Open
                    </span>
                </div>
                <div class="page-sub">Review community-reported content issues. Uphold or dismiss each flag with a written rationale.</div>
            </div>
        </div>

        <!-- FILTER BAR -->
        <div class="filter-bar">
            <button class="filter-tab active" onclick="filterFlags('all', this)">All Flags</button>
            <button class="filter-tab" onclick="filterFlags('open', this)">Open</button>
            <button class="filter-tab" onclick="filterFlags('upheld', this)">Upheld</button>
            <button class="filter-tab" onclick="filterFlags('dismissed', this)">Dismissed</button>
            <select class="filter-select" onchange="filterByReason(this.value)">
                <option value="">All Reasons</option>
                <option value="Copyright Infringement">Copyright Infringement</option>
                <option value="Plagiarism">Plagiarism</option>
                <option value="Missing Citations">Missing Citations</option>
                <option value="Duplicate Entry">Duplicate Entry</option>
                <option value="Inappropriate Content">Inappropriate Content</option>
            </select>
        </div>

        <!-- FLAGS LIST -->
        <div class="flags-list" id="flagsList">
            <c:choose>
                <c:when test="${not empty flags}">
                    <c:forEach var="flag" items="${flags}">
                        <div class="flag-card ${flag.status.toLowerCase()}" data-status="${flag.status.toLowerCase()}" data-reason="${flag.reason}">
                            <div class="flag-header">
                                <div class="flag-icon-wrap" style="${flag.status eq 'UPHELD' ? 'background: var(--red-light);' : (flag.status eq 'DISMISSED' ? 'background: var(--green-light);' : '')}">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="${flag.status eq 'UPHELD' ? 'color: var(--red);' : (flag.status eq 'DISMISSED' ? 'color: var(--green);' : '')}">
                                        <path d="M4 15s1-1 4-1 5 2 8 2 4-1 4-1V3s-1 1-4 1-5-2-8-2-4 1-4 1z"/><line x1="4" y1="22" x2="4" y2="15"/>
                                    </svg>
                                </div>
                                <div class="flag-meta">
                                    <div class="flag-resource-title"><a href="${pageContext.request.contextPath}/resource?id=${flag.resourceId}">${flag.resourceTitle}</a></div>
                                    <div class="flag-tags">
                                        <span class="tag tag-type">${flag.resourceType}</span>
                                        <span class="tag tag-cat">${flag.resourceCategory}</span>
                                        <span class="tag tag-reason">${flag.reason}</span>
                                    </div>
                                    <div class="flag-info-row">
                                      <span>
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="12" height="12"><rect x="3" y="4" width="18" height="18" rx="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
                                        Flagged ${flag.flaggedDate}
                                      </span>
                                        <span>
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="12" height="12"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/></svg>
                                        Submitted by ${flag.submitterName}
                                      </span>
                                    </div>
                                </div>
                                <span class="flag-status-badge status-${flag.status.toLowerCase()}">${flag.status}</span>
                            </div>
                            <div class="flag-body">
                                <div class="flag-reporter-row">
                                    <div class="reporter-avatar">${flag.reporterInitials}</div>
                                    <div class="reporter-info">
                                        <strong>Reported by: ${flag.reporterName}</strong>
                                        <span> · ${flag.reportContext}</span>
                                    </div>
                                </div>
                                <div class="flag-reason-box">
                                    <strong>Reported Reason</strong>
                                        ${flag.description}
                                </div>
                            </div>
                            <c:choose>
                                <c:when test="${flag.status eq 'OPEN'}">
                                    <div class="flag-action-area">
                                        <div class="action-label">Moderation Decision</div>
                                        <textarea class="action-textarea" placeholder="Write your reasoning for upholding or dismissing this flag..."></textarea>
                                        <div class="action-buttons">
                                            <button class="btn btn-uphold" onclick="resolveFlag(this, 'upheld', '${flag.id}')">
                                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" width="14" height="14"><path d="M4 15s1-1 4-1 5 2 8 2 4-1 4-1V3s-1 1-4 1-5-2-8-2-4 1-4 1z"/></svg>
                                                Uphold Flag
                                            </button>
                                            <button class="btn btn-dismiss" onclick="resolveFlag(this, 'dismissed', '${flag.id}')">
                                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" width="14" height="14"><polyline points="20 6 9 17 4 12"/></svg>
                                                Dismiss Flag
                                            </button>
                                            <button class="btn btn-secondary">View Resource</button>
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="resolved-note">
                                        <div class="resolved-note-inner ${flag.status.toLowerCase()}">
                                            <strong>Flag ${flag.status eq 'UPHELD' ? 'Upheld' : 'Dismissed'} — Decision by ${flag.resolvedBy}</strong>
                                                ${flag.resolutionNotes}
                                            <div class="resolved-meta">Resolved ${flag.resolvedDate} · Audit ID: ${flag.auditId}</div>
                                        </div>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 15s1-1 4-1 5 2 8 2 4-1 4-1V3s-1 1-4 1-5-2-8-2-4 1-4 1z"/><line x1="4" y1="22" x2="4" y2="15"/></svg>
                        <p>No flags are currently available.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

    </main>

    <footer class="site-footer">
        <span class="footer-copy">© 2023 ScholarShare · Institutional Repository Management System</span>
        <div class="footer-links">
            <a href="${pageContext.request.contextPath}/docs">Documentation</a>
            <a href="${pageContext.request.contextPath}/support">Support Desk</a>
            <a href="${pageContext.request.contextPath}/status">System Status</a>
        </div>
    </footer>

    <div class="sidebar-overlay" id="sidebarOverlay"></div>

    <script src="${pageContext.request.contextPath}/js/admin-dashboard-navbar.js"></script>

    <!-- TOAST CONTAINER -->
    <div class="toast-container" id="toastContainer"></div>


</div>
</body>
</html>
