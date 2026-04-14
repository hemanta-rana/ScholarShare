<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=yes">
    <title>ScholarPass | Secure Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/headerFooter.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Login.css">
</head>

<body>

<%--       1. NAVBAR--%>

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
          <li><a href="${pageContext.request.contextPath}/home#reputation">About</a></li>
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
        <a href="${pageContext.request.contextPath}/home#categories"    onclick="closeMenu()">Browse Resources</a>
        <a href="${pageContext.request.contextPath}/home#how-it-works"  onclick="closeMenu()">How It Works</a>
        <a href="${pageContext.request.contextPath}/home#reputation"    onclick="closeMenu()">About</a>
        <a href="#footer"        onclick="closeMenu()">Contact</a>
        <a href="${pageContext.request.contextPath}/auth/login" class="mob-cta" onclick="closeMenu()">Get Started</a>
      </div>
    </nav>
  </div>

    <div class="login-wrapper">
        <div class="container">
            <!-- LEFT SECTION: PRIMARY COLOR #36454F with brand messaging & features -->
            <div class="left">

                <img src="${pageContext.request.contextPath}/Images/login_illustration.png" alt="Learning Illustration" class="left-image" onerror="this.src='../../images/hero.png'">

            </div>

            <!-- RIGHT SECTION: SECONDARY COLOR #F5DEB3 as background, login card white -->
            <div class="right">
                <div class="login-card">
                    <h2>Welcome Back</h2>
                    <p class="welcome-text">Sign in to access your dashboard</p>
                    <c:if test="${not empty error}">
                        <p style="color:red;">${error}</p>
                    </c:if>
                    <form action="${pageContext.request.contextPath}auth/login" method="post">
                        <!-- Email field -->
                        <div class="input-group">
                            <input name="email" value="<c:out value='${email}'/> "  type="email" placeholder="Email address" required>
                        </div>

                        <!-- Password field -->
                        <div class="input-group">
                            <input name="password" type="password" placeholder="Password" required>
                        </div>

                        <a href="#" class="forgot-link">Forgot Password?</a>


                        <button type="submit" class="login-submit-btn">
                            <svg class="material-symbols-outlined" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="width:1rem;height:1rem;"><path d="M15 3h4a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-4"></path><polyline points="10 17 15 12 10 7"></polyline><line x1="15" y1="12" x2="3" y2="12"></line></svg> 
                            Sign In
                        </button>
                    </form>

                    <div class="register-link">
                        Don't have an account? <a href="${pageContext.request.contextPath}/register">Register</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

<script>
document.addEventListener('DOMContentLoaded', () => {
    const wrapper = document.querySelector('.login-wrapper') || document.querySelector('.register-wrapper');
    if (wrapper) wrapper.classList.add('page-flip-in');

    document.querySelectorAll('a').forEach(link => {
        if (link.href.includes('auth/login') || link.href.includes('register')) {
            link.addEventListener('click', (e) => {
                if(link.closest('#navbar') || link.closest('#nav-mobile') || link.closest('.nav-cta') || link.closest('.mob-cta')) return;
                e.preventDefault();
                const target = e.currentTarget.href;
                if (wrapper) {
                    wrapper.classList.remove('page-flip-in');
                    wrapper.classList.add('page-flip-out');
                }
                setTimeout(() => window.location.href = target, 400);
            });
        }
    });
});
</script>


  <script src="${pageContext.request.contextPath}/js/home.js"></script>
</body>
</html>