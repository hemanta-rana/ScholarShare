<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>500 — Internal Server Error | ScholarShare</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png" />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=DM+Serif+Display:ital@0;1&family=DM+Sans:wght@400;500;600;700&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/headerFooter.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/error-pages.css" />
</head>
<body class="error-page">

    <%-- ── NAVBAR ── --%>
    <div id="navbar-wrapper">
        <nav id="navbar">
            <div class="nav-row">
                <a href="${pageContext.request.contextPath}/home" class="nav-logo">
                    <img src="${pageContext.request.contextPath}/images/logo.png"
                         alt="ScholarShare" class="nav-logo-icon" />
                    <span>ScholarShare</span>
                </a>
                <ul class="nav-links">
                    <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/browser">Browse Resources</a></li>
                    <li><a href="${pageContext.request.contextPath}/aboutUs">About</a></li>
                </ul>
                <a href="${pageContext.request.contextPath}/login" class="nav-cta">Get Started</a>
                <button class="hamburger" id="hamburger" aria-label="Toggle menu">
                    <span></span><span></span><span></span>
                </button>
            </div>
            <div class="nav-mobile" id="nav-mobile">
                <a href="${pageContext.request.contextPath}/home">Home</a>
                <a href="${pageContext.request.contextPath}/browser">Browse Resources</a>
                <a href="${pageContext.request.contextPath}/aboutUs">About</a>
                <a href="${pageContext.request.contextPath}/login" class="mob-cta">Get Started</a>
            </div>
        </nav>
    </div>

    <%-- ── MAIN ERROR CONTENT ── --%>
    <main class="error-main">

        <%-- Ghost background number --%>
        <div class="error-ghost-code" aria-hidden="true">500</div>

        <div class="error-card">

            <%-- Status pill --%>
            <div class="error-status-pill error-status-pill--500">
                <span class="pill-dot"></span>
                Institutional Access Interrupted
            </div>

            <%-- Heading --%>
            <h1 class="error-heading">
                Internal Server Error
            </h1>

            <%-- Divider --%>
            <div class="error-divider"></div>

            <%-- Description --%>
            <p class="error-description">
                Our digital archive is currently experiencing an internal
                synchronisation error. Our curators have been notified and
                are working to restore access as quickly as possible.
            </p>

            <%-- Action buttons --%>
            <div class="error-actions">
                <a href="javascript:location.reload()" class="error-btn-primary">
                    &#8635;&nbsp; Reload Archive
                </a>
                <a href="${pageContext.request.contextPath}/home" class="error-btn-secondary">
                    Return to Homepage
                </a>
            </div>

            <%-- Diagnostic reference --%>
            <p class="error-diagnostic">
                Diagnostic Reference: SRV_SYNC_ERR_00X500_SCHLR
            </p>

        </div>
    </main>

    <%-- ── FOOTER ── --%>
    <footer id="footer">
        <div class="container">
            <div class="footer-grid">
                <div>
                    <div class="footer-brand-name">
                        <img src="${pageContext.request.contextPath}/images/logo.png"
                             alt="ScholarShare Logo"
                             style="width:36px;height:36px;object-fit:contain;filter:brightness(0) invert(1);" />
                        <span class="brand-text">ScholarShare</span>
                    </div>
                    <p class="footer-tagline">Ethical. Verified. Academic.</p>
                    <p class="footer-desc">
                        A Digital Academic Resource Sharing Platform built on principles
                        of integrity, transparency, and peer collaboration.
                    </p>
                </div>
                <div>
                    <div class="footer-col-title">Platform</div>
                    <div class="footer-links">
                        <a href="${pageContext.request.contextPath}/home">Home</a>
                        <a href="${pageContext.request.contextPath}/browser">Browse Resources</a>
                        <a href="${pageContext.request.contextPath}/aboutUs">About</a>
                        <a href="${pageContext.request.contextPath}/login">Login</a>
                    </div>
                </div>
                <div>
                    <div class="footer-col-title">Support</div>
                    <div class="footer-links">
                        <a href="${pageContext.request.contextPath}/home">Report an Issue</a>
                        <a href="${pageContext.request.contextPath}/aboutUs">Contact Us</a>
                        <a href="${pageContext.request.contextPath}/home">Privacy Policy</a>
                        <a href="${pageContext.request.contextPath}/home">Terms of Use</a>
                    </div>
                </div>
                <div>
                    <div class="footer-col-title">Institution</div>
                    <div class="footer-links">
                        <a href="${pageContext.request.contextPath}/home">Academic Integrity Policy</a>
                        <a href="${pageContext.request.contextPath}/home">Accessibility</a>
                        <a href="${pageContext.request.contextPath}/home">Archival Standards</a>
                    </div>
                </div>
            </div>
            <div class="footer-divider"></div>
            <div class="footer-bottom">
                <p>© 2026 ScholarShare — London Metropolitan University. All rights reserved.</p>
                <div class="footer-bottom-links">
                    <a href="${pageContext.request.contextPath}/home">Integrity Policy</a>
                    <a href="${pageContext.request.contextPath}/home">Terms of Use</a>
                </div>
            </div>
        </div>
    </footer>

    <script src="${pageContext.request.contextPath}/js/home.js"></script>
</body>
</html>
