<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>ScholarShare - Home page</title>
  <link rel="preconnect" href="https://fonts.googleapis.com" />
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
  <link href="https://fonts.googleapis.com/css2?family=DM+Serif+Display:ital@0;1&family=DM+Sans:wght@400;500;600;700&display=swap" rel="stylesheet" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/headerFooter.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
</head>
<body>
<%--  NAV BAR --%>
  <div id="navbar-wrapper">
    <nav id="navbar">
      <div class="nav-row">
        <!-- Logo -->
        <a href="${pageContext.request.contextPath}/" class="nav-logo">📚 ScholarShare</a>

        <!-- Desktop links -->
        <ul class="nav-links">
          <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
          <li><a href="#categories">Browse Resources</a></li>
          <li><a href="#how-it-works">How It Works</a></li>
          <li><a href="#reputation">About</a></li>
          <li><a href="#footer">Contact</a></li>
        </ul>

        <!-- Desktop CTA -->
        <a href="${pageContext.request.contextPath}/auth/login" class="nav-cta">Get Started</a>

        <!-- Hamburger (mobile) -->
        <button class="hamburger" id="hamburger" aria-label="Toggle menu">
          <span></span><span></span><span></span>
        </button>
      </div>

      <!-- Mobile Dropdown -->
      <div class="nav-mobile" id="nav-mobile">
        <a href="${pageContext.request.contextPath}/home"          onclick="closeMenu()">Home</a>
        <a href="#categories"    onclick="closeMenu()">Browse Resources</a>
        <a href="#how-it-works"  onclick="closeMenu()">How It Works</a>
        <a href="#reputation"    onclick="closeMenu()">About</a>
        <a href="#footer"        onclick="closeMenu()">Contact</a>
        <a href="${pageContext.request.contextPath}/auth/login" class="mob-cta" onclick="closeMenu()">Get Started</a>
      </div>
    </nav>
  </div>

<%--HERO section --%>
  <section id="hero">
    <div class="container">
      <div class="hero-grid">

        <!-- Left content -->
        <div class="reveal">
          <div class="hero-badge">
            <span class="hero-badge-dot"></span>
            <span>Integrity-First Academic Platform</span>
          </div>

          <h1 class="hero-h1">
            Academic Resources.<br>
            <em>Verified.</em> Trusted. Shared.
          </h1>

          <p class="hero-sub">
            A moderated platform where students share study materials
            with academic integrity at every step.
          </p>

          <div class="hero-btns">
            <a href="#categories" class="btn-sky">Browse Resources</a>
            <a href="#how-it-works" class="btn-gold">Upload a Resource</a>
          </div>

        </div>

        <!-- Right image -->
        <div class="hero-img-col reveal" style="transition-delay:.15s;">
          <div class="hero-img-wrap">
            <img
              src="${pageContext.request.contextPath}/Images/hero.png"
              alt="Students studying in a university library"
            />
          </div>
        </div>

      </div>
    </div>
  </section>


  <!-- ══════════════════════════════════════════
       3. HOW IT WORKS
  ══════════════════════════════════════════ -->
  <section id="how-it-works">
    <div class="container">

      <div class="section-header reveal">
        <div class="badge">Simple Process</div>
        <h2>How ScholarShare Works</h2>
        <p>A transparent, three-step pipeline that ensures every shared resource meets our academic integrity standards.</p>
      </div>

      <div class="steps-grid">
        <div class="step-card reveal">
          <div class="step-number">01</div>
          <div class="step-icon">📤</div>
          <h3>Upload</h3>
          <p>Submit your study material with metadata and an integrity pledge. Tag it by faculty, subject, and topic for easy discovery.</p>
          <div class="step-bar"></div>
        </div>
        <div class="step-card reveal" style="transition-delay:.12s;">
          <div class="step-number">02</div>
          <div class="step-icon">🔍</div>
          <h3>Moderation</h3>
          <p>Admins review every submission before it goes live. Each resource is checked for accuracy, relevance, and academic integrity.</p>
          <div class="step-bar"></div>
        </div>
        <div class="step-card reveal" style="transition-delay:.24s;">
          <div class="step-number">03</div>
          <div class="step-icon">🗂️</div>
          <h3>Discover</h3>
          <p>Browse verified resources by Faculty, Subject, and Topic. Save materials to your collection and track your usage.</p>
          <div class="step-bar"></div>
        </div>
      </div>

      <div class="process-img-wrap reveal">
        <img
          src="https://images.unsplash.com/photo-1552664730-d307ca884978?w=1100&q=80"
          alt="Collaborative academic workflow"
          onerror="this.style.height='340px';this.style.background='#dde3ec'"
        />
      </div>
    </div>
  </section>


  <!-- ══════════════════════════════════════════
       4. FEATURES
  ══════════════════════════════════════════ -->
  <section id="features">
    <div class="container">

      <div class="section-header reveal">
        <div class="badge">Platform Features</div>
        <h2>Built for Students. Managed by Admins.</h2>
        <p>ScholarShare provides dedicated toolsets for both contributors and administrators.</p>
      </div>

      <div class="features-grid reveal">
        <!-- Student Panel -->
        <div class="feat-panel light">
          <div class="feat-panel-header">
            <div class="feat-icon-box">🎓</div>
            <div>
              <h3>Student Features</h3>
              <small>Tools for contributors</small>
            </div>
          </div>
          <ul>
            <li class="feat-item">
              <div class="feat-item-icon">📤</div>
              <span class="feat-item-label">Upload Materials</span>
              <span class="feat-dot"></span>
            </li>
            <li class="feat-item">
              <div class="feat-item-icon">📊</div>
              <span class="feat-item-label">Track Submission Status</span>
              <span class="feat-dot"></span>
            </li>
            <li class="feat-item">
              <div class="feat-item-icon">🗂️</div>
              <span class="feat-item-label">Browse by Category</span>
              <span class="feat-dot"></span>
            </li>
            <li class="feat-item">
              <div class="feat-item-icon">🔖</div>
              <span class="feat-item-label">Save to Collections</span>
              <span class="feat-dot"></span>
            </li>
            <li class="feat-item">
              <div class="feat-item-icon">🚩</div>
              <span class="feat-item-label">Flag Content</span>
              <span class="feat-dot"></span>
            </li>
            <li class="feat-item">
              <div class="feat-item-icon">⭐</div>
              <span class="feat-item-label">View Reputation Score</span>
              <span class="feat-dot"></span>
            </li>
          </ul>
        </div>

        <!-- Admin Panel -->
        <div class="feat-panel dark">
          <div class="feat-panel-header">
            <div class="feat-icon-box">🛡️</div>
            <div>
              <h3>Admin Features</h3>
              <small>Moderation toolkit</small>
            </div>
          </div>
          <ul>
            <li class="feat-item">
              <div class="feat-item-icon">✅</div>
              <span class="feat-item-label">Approve Registrations</span>
              <span class="feat-dot"></span>
            </li>
            <li class="feat-item">
              <div class="feat-item-icon">📋</div>
              <span class="feat-item-label">Review Submissions</span>
              <span class="feat-dot"></span>
            </li>
            <li class="feat-item">
              <div class="feat-item-icon">🛡️</div>
              <span class="feat-item-label">Manage Flags</span>
              <span class="feat-dot"></span>
            </li>
            <li class="feat-item">
              <div class="feat-item-icon">🗃️</div>
              <span class="feat-item-label">Category CRUD</span>
              <span class="feat-dot"></span>
            </li>
            <li class="feat-item">
              <div class="feat-item-icon">📈</div>
              <span class="feat-item-label">Analytics Dashboard</span>
              <span class="feat-dot"></span>
            </li>
            <li class="feat-item">
              <div class="feat-item-icon">🔎</div>
              <span class="feat-item-label">Moderation Audit Trail</span>
              <span class="feat-dot"></span>
            </li>
          </ul>
        </div>
      </div>

      <div class="dashboard-img-wrap reveal">
        <img
          src="https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=1100&q=80"
          alt="Student dashboard analytics"
          onerror="this.style.height='380px';this.style.background='#dde3ec'"
        />
      </div>

    </div>
  </section>


  <!-- ══════════════════════════════════════════
       5. ACADEMIC INTEGRITY BANNER
  ══════════════════════════════════════════ -->
  <section id="integrity">
    <div class="deco-circle-1"></div>
    <div class="deco-circle-2"></div>
    <div class="deco-quote-bg">"</div>

    <div class="integrity-inner reveal">
      <div class="integrity-badge">
        <span>🏛️</span>
        <span>Academic Integrity Pledge</span>
      </div>

      <div class="quote-mark">"</div>

      <p class="integrity-quote">
        Every contributor takes an Academic Integrity Pledge. No shortcuts.
        No plagiarism. Just honest knowledge sharing.
      </p>

      <div class="quote-mark" style="transform:scaleX(-1);display:inline-block;">"</div>

      <div class="integrity-divider">
        <div class="line"></div>
        <span class="stars">✦ ✦ ✦</span>
        <div class="line r"></div>
      </div>

      <p class="integrity-sub">
        ScholarShare enforces an integrity-first culture. Every submission
        undergoes admin review before being made available to the academic community.
      </p>
    </div>
  </section>


  <!-- ══════════════════════════════════════════
       6. RESOURCE CATEGORIES
  ══════════════════════════════════════════ -->
  <section id="categories">
    <div class="container">

      <div class="section-header reveal">
        <div class="badge">Browse by Faculty</div>
        <h2>Explore by Faculty</h2>
        <p>Find peer-reviewed study materials organised by your academic faculty and subject area.</p>
      </div>

      <div class="cat-grid reveal">
        <div class="cat-card">
          <div class="cat-icon">💻</div>
          <div class="cat-name">Computer Science</div>
          <div class="cat-count">320 resources</div>
          <div class="cat-link">Browse <span class="cat-link-arrow">→</span></div>
        </div>
        <div class="cat-card">
          <div class="cat-icon">⚙️</div>
          <div class="cat-name">Engineering</div>
          <div class="cat-count">215 resources</div>
          <div class="cat-link">Browse <span class="cat-link-arrow">→</span></div>
        </div>
        <div class="cat-card">
          <div class="cat-icon">📊</div>
          <div class="cat-name">Business</div>
          <div class="cat-count">180 resources</div>
          <div class="cat-link">Browse <span class="cat-link-arrow">→</span></div>
        </div>
        <div class="cat-card">
          <div class="cat-icon">⚖️</div>
          <div class="cat-name">Law</div>
          <div class="cat-count">140 resources</div>
          <div class="cat-link">Browse <span class="cat-link-arrow">→</span></div>
        </div>
        <div class="cat-card">
          <div class="cat-icon">🔬</div>
          <div class="cat-name">Science</div>
          <div class="cat-count">195 resources</div>
          <div class="cat-link">Browse <span class="cat-link-arrow">→</span></div>
        </div>
        <div class="cat-card">
          <div class="cat-icon">📜</div>
          <div class="cat-name">Humanities</div>
          <div class="cat-count">150 resources</div>
          <div class="cat-link">Browse <span class="cat-link-arrow">→</span></div>
        </div>
      </div>

      <div class="categories-cta reveal">
        <a href="#" class="btn-sky-outline" onclick="return false;">View All Resources →</a>
      </div>

    </div>
  </section>


  <!-- ══════════════════════════════════════════
       7. CONTRIBUTOR REPUTATION
  ══════════════════════════════════════════ -->
  <section id="reputation">
    <div class="container">
      <div class="rep-grid">

        <!-- Left -->
        <div class="reveal">
          <div class="badge">Contributor Reputation</div>

          <h2 class="serif" style="font-size:clamp(28px,3.5vw,42px);color:var(--navy);margin-bottom:20px;line-height:1.2;">
            Your Reputation Grows With You
          </h2>

          <p style="color:#5a6a8a;font-size:16px;line-height:1.75;margin-bottom:32px;max-width:440px;">
            ScholarShare tracks your contributions and builds your academic
            profile. The more quality materials you share, the higher your
            standing in the community.
          </p>

          <!-- Score Widget -->
          <div class="rep-score-widget">
            <div class="score-circle">
              <strong>847</strong>
              <small>SCORE</small>
            </div>
            <div class="score-info">
              <div class="score-tier">
                <span>🥇</span>
                <strong>Gold Contributor</strong>
              </div>
              <p class="score-desc">Top 5% of all contributors this semester. 84 approved uploads.</p>
              <div class="progress-track">
                <div class="progress-bar"></div>
              </div>
              <p class="progress-label">153 points to Platinum</p>
            </div>
          </div>

          <!-- Reputation Rules -->
          <div class="rep-rules">
            <div class="rep-rule pos">
              <span class="rep-rule-icon">✅</span>
              <span class="rep-rule-text">Approved uploads earn +10 reputation points</span>
            </div>
            <div class="rep-rule pos">
              <span class="rep-rule-icon">📝</span>
              <span class="rep-rule-text">Detailed submissions with metadata earn bonus points</span>
            </div>
            <div class="rep-rule neg">
              <span class="rep-rule-icon">⚠️</span>
              <span class="rep-rule-text">Rejected submissions reduce your score by −5 points</span>
            </div>
            <div class="rep-rule pos">
              <span class="rep-rule-icon">🏆</span>
              <span class="rep-rule-text">Top contributors receive exclusive recognition badges</span>
            </div>
          </div>

          <a href="#" class="btn-gold" onclick="return false;" style="margin-top:4px;">
            Start Contributing →
          </a>
        </div>

        <!-- Right image -->
        <div class="rep-img-wrap reveal" style="transition-delay:.15s;">
          <img
            src="https://images.unsplash.com/photo-1523240795612-9a054b0db644?w=800&q=80"
            alt="Students earning academic recognition"
            onerror="this.style.height='520px';this.style.background='#dde3ec'"
          />
        </div>

      </div>
    </div>
  </section>


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
  </footer>

  <!-- ══════════════════════════════════════════
       JAVASCRIPT
  ══════════════════════════════════════════ -->
  <script src="${pageContext.request.contextPath}/js/home.js"></script>
</body>
</html>