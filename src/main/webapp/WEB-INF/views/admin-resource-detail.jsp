<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>${resource.title} — ScholarShare Admin</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png" />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@400;500;600;700&display=swap" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/css/sidebar.css" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/css/adminDashboard.css" rel="stylesheet" />
</head>
<body>

<jsp:include page="template/admin-sidebar.jsp">
    <jsp:param name="activePage" value="" />
</jsp:include>

<div class="main">
    <jsp:include page="template/admin-header.jsp">
        <jsp:param name="searchPlaceholder" value="Search resources, users, or archives…" />
    </jsp:include>

    <main class="content">

        <%-- Back link --%>
        <div style="margin-bottom:1.25rem;">
            <a href="javascript:history.back()"
               style="display:inline-flex;align-items:center;gap:6px;font-size:13px;font-weight:600;color:var(--accent-blue);text-decoration:none;">
                <img src="${pageContext.request.contextPath}/images/home-icons/link.png"
                     alt="" width="13" height="13" style="transform:rotate(180deg);opacity:0.7;">
                Back
            </a>
        </div>

        <div class="card" style="padding:28px 32px;max-width:860px;">

            <%-- Type + Status badges --%>
            <div style="display:flex;gap:8px;flex-wrap:wrap;margin-bottom:14px;">
                <span class="status-badge pending" style="text-transform:uppercase;">${resource.resourceType}</span>
                <span class="status-badge ${resource.status eq 'approved' ? 'reviewing' : 'pending'}"
                      style="text-transform:uppercase;">${resource.status}</span>
            </div>

            <%-- Title --%>
            <h1 style="font-family:var(--font-display);font-size:26px;font-weight:700;color:var(--text-primary);margin-bottom:12px;line-height:1.3;">
                <c:out value="${resource.title}"/>
            </h1>

            <%-- Description --%>
            <p style="font-size:14.5px;color:#4a5568;line-height:1.65;margin-bottom:22px;">
                <c:out value="${resource.description}"/>
            </p>

            <%-- Meta info --%>
            <div style="display:flex;flex-direction:column;gap:10px;padding-top:18px;border-top:1px solid var(--border);margin-bottom:22px;">
                <div style="display:flex;gap:10px;font-size:13.5px;color:#6b7a99;">
                    <span style="font-weight:600;color:#3d4f6e;min-width:120px;">Uploaded on</span>
                    <span><c:out value="${resource.uploadDate}"/></span>
                </div>
                <div style="display:flex;gap:10px;font-size:13.5px;color:#6b7a99;">
                    <span style="font-weight:600;color:#3d4f6e;min-width:120px;">Self-declared</span>
                    <span>${resource.selfDeclaration ? 'Yes' : 'No'}</span>
                </div>
                <c:if test="${not empty resource.submitterName}">
                    <div style="display:flex;gap:10px;font-size:13.5px;color:#6b7a99;">
                        <span style="font-weight:600;color:#3d4f6e;min-width:120px;">Submitted by</span>
                        <span><c:out value="${resource.submitterName}"/></span>
                    </div>
                </c:if>
            </div>

            <%-- Download --%>
            <a href="${pageContext.request.contextPath}/${resource.filePath}"
               download
               style="display:inline-flex;align-items:center;gap:8px;padding:10px 22px;background:var(--accent-blue);color:#fff;font-size:13.5px;font-weight:600;border-radius:8px;text-decoration:none;transition:background 0.2s;">
                <img src="${pageContext.request.contextPath}/images/home-icons/upload.png"
                     alt="" width="14" height="14" style="transform:rotate(180deg);filter:brightness(10);">
                Download Resource
            </a>
        </div>

        <%-- Collections this resource belongs to (admin view) --%>
        <c:if test="${not empty collections}">
            <div class="card" style="padding:20px 24px;max-width:860px;margin-top:16px;">
                <div class="card-header" style="padding:0 0 14px 0;border-bottom:1px solid var(--border);margin-bottom:14px;">
                    <span class="card-title">Saved in Collections</span>
                </div>
                <c:forEach var="col" items="${collections}">
                    <div style="font-size:13.5px;padding:6px 0;border-bottom:1px solid #f5f6fa;color:var(--text-primary);">
                        <c:out value="${col.collectionName}"/>
                    </div>
                </c:forEach>
            </div>
        </c:if>

    </main>

    <footer class="site-footer">
        <span class="footer-copy">© 2023 ScholarShare · Institutional Repository Management System</span>
    </footer>

    <div class="sidebar-overlay" id="sidebarOverlay"></div>
    <script src="${pageContext.request.contextPath}/js/admin-dashboard-navbar.js"></script>
</div>
</body>
</html>
