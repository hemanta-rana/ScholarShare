<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ScholarShare - About</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/headerFooter.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/about_us.css">
</head>
<body>
<!--  NAV BAR -->
<div id="navbar-wrapper">
    <nav id="navbar">
        <div class="nav-row">
            <!-- Logo -->
            <a href="${pageContext.request.contextPath}/" class="nav-logo">📚 ScholarShare</a>

            <!-- Desktop links -->
            <ul class="nav-links">
                <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/home#categories">Browse Resources</a></li>
                <li><a href="${pageContext.request.contextPath}/home#how-it-works">How It Works</a></li>
                <li><a href="${pageContext.request.contextPath}/aboutUs">About</a></li>
                <li><a href="#footer">Contact</a></li>
            </ul>

            <!-- Desktop CTA -->
            <a href="${pageContext.request.contextPath}/login" class="nav-cta">Get Started</a>

            <!-- Hamburger (mobile) -->
            <button class="hamburger" id="hamburger" aria-label="Toggle menu">
                <span></span><span></span><span></span>
            </button>
        </div>

        <!-- Mobile Dropdown -->
        <div class="nav-mobile" id="nav-mobile">
            <a href="${pageContext.request.contextPath}/home"          onclick="closeMenu()">Home</a>
            <a href="${pageContext.request.contextPath}/home#categories"    onclick="closeMenu()">Browse Resources</a>
            <a href="${pageContext.request.contextPath}/home#how-it-works"  onclick="closeMenu()">How It Works</a>
            <a href="${pageContext.request.contextPath}/aboutUs"    onclick="closeMenu()">About</a>
            <a href="#footer"        onclick="closeMenu()">Contact</a>
            <a href="${pageContext.request.contextPath}/login" class="mob-cta" onclick="closeMenu()">Get Started</a>
        </div>
    </nav>
</div>
<main class="about-main">
    <!-- HERO -->
    <section class="hero">
        <div class="container center">
            <h2>About ScholarShare</h2>
            <p>Built to solve a real problem in academic resource sharing.</p>
        </div>
    </section>

    <!-- MISSION -->
    <section class="mission container">
        <div class="mission-text">
            <h3>Our Mission</h3>
            <p>
                At the core of ScholarShare is a commitment to academic integrity.
                We believe knowledge should be shared freely but responsibly.
            </p>
            <p>
                Our platform ensures ethical sharing through peer moderation,
                empowering students and educators with high-quality resources.
            </p>
        </div>
        <div class="mission-image">
            <img src="${pageContext.request.contextPath}/images/about_us/about_us_hero_image.png" alt="Mission Image" />
        </div>
    </section>

    <!-- PROBLEM / SOLUTION -->
    <section class="info-section">
        <div class="container info-grid">
            <div class="card">
                <h4>⚠️ The Problem</h4>
                <p>
                    Informal academic sharing often leads to misinformation and lack
                    of accountability.
                </p>
            </div>

            <div class="card">
                <h4>✔ Our Solution</h4>
                <p>
                    A structured, peer-reviewed system that ensures verified and
                    ethical academic sharing.
                </p>
            </div>
        </div>
    </section>

    <!-- TECH STACK -->
    <section class="tech container">
        <h3>Built With</h3>
        <div class="tech-list">
            <span>Java EE</span>
            <span>JSP</span>
            <span>CSS3</span>
            <span>MySQL</span>
            <span>Apache Tomcat</span>
            <span>Maven</span>
            <span>Git</span>
        </div>
    </section>

    <!-- TEAM -->
    <section class="team">
        <div class="container">
            <h3>The Creators</h3>
            <div class="team-grid">

                <div class="team-card">
                    <img src="${pageContext.request.contextPath}/images/about_us/Hemanta.png" alt="Hementa Rana" />
                    <h4>Hementa Rana</h4>
                </div>

                <div class="team-card">
                    <img src="https://via.placeholder.com/100" alt="Sajan Gurung" />
                    <h4>Sajan Gurung</h4>
                </div>

                <div class="team-card">
                    <img src="https://via.placeholder.com/100" alt="Janam Ale" />
                    <h4>Janam Ale</h4>
                </div>

                <div class="team-card">
                    <img src="https://via.placeholder.com/100" alt="Rupan Tilija Pun"/>
                    <h4>Rupan Tilija Pun</h4>
                </div>
            </div>
        </div>
    </section>
</main>
<!-- ══════════════════════════════════════════
       8. FOOTER
  ══════════════════════════════════════════ -->
<footer id="footer">
    <div class="container">

        <div class="footer-grid">
            <!-- Brand -->
            <div>
                <div class="footer-brand-name">
                    <span class="brand-emoji">📚</span>
                    <span class="brand-text">ScholarShare</span>
                </div>
                <p class="footer-tagline">Ethical. Verified. Academic.</p>
                <p class="footer-desc">
                    A Digital Academic Resource Sharing Platform built on principles
                    of integrity, transparency, and peer collaboration.
                </p>
                <div class="footer-socials">
                    <div class="social-btn">🎓</div>
                    <div class="social-btn">📧</div>
                    <div class="social-btn">🔗</div>
                </div>
            </div>

            <!-- Platform -->
            <div>
                <div class="footer-col-title">Platform</div>
                <div class="footer-links">
                    <a href="#reputation">About</a>
                    <a href="#footer">Contact</a>
                    <a href="#">Privacy</a>
                    <a href="#integrity">Academic Integrity Policy</a>
                </div>
            </div>

            <!-- Resources -->
            <div>
                <div class="footer-col-title">Resources</div>
                <div class="footer-links">
                    <a href="#categories">Computer Science</a>
                    <a href="#categories">Engineering</a>
                    <a href="#categories">Business</a>
                    <a href="#categories">Law</a>
                    <a href="#categories">Science</a>
                    <a href="#categories">Humanities</a>
                </div>
            </div>

            <!-- Newsletter -->
            <div class="footer-newsletter">
                <div class="footer-col-title">Stay Updated</div>
                <p>Get notified when new verified resources are published.</p>
                <div class="newsletter-form">
                    <input class="newsletter-input" type="email" placeholder="your@email.ac.uk" />
                    <button class="newsletter-btn" onclick="handleSubscribe(event)">Subscribe</button>
                </div>
            </div>
        </div>

        <div class="footer-divider"></div>

        <div class="footer-bottom">
            <p>© 2026 ScholarShare — London Metropolitan University. All rights reserved.</p>
            <div class="footer-bottom-links">
                <a href="#">Integrity Policy</a>
                <a href="#">Terms of Use</a>
            </div>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/js/home.js"></script>
            </div>
        </div>
    </section>
</footer>
</body>
</html>