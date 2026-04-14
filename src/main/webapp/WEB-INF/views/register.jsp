<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=yes">
    <title>ScholarPass | Create Account</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/headerFooter.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Register.css"/>
    <script src="${pageContext.request.contextPath}/js/Register.js" defer></script>
</head>
<body>

  <!-- ══════════════════════════════════════════
       1. NAVBAR
  ══════════════════════════════════════════ -->
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
        <a href="${pageContext.request.contextPath}/home#reputation"    onclick="closeMenu()">About</a>
        <a href="#footer"        onclick="closeMenu()">Contact</a>
        <a href="${pageContext.request.contextPath}/login" class="mob-cta" onclick="closeMenu()">Get Started</a>
      </div>
    </nav>
  </div>

<div class="register-wrapper">
<div class="container">

    <!-- LEFT SECTION -->
    <div class="left">
        <div class="login-card">

            <!-- Logo -->
<%--            <div class="shield-icon">🛡️</div>--%>

            <h2>Create Account</h2>
            <p class="welcome-text">Join us and start learning today</p>

            <c:if test="${not empty error}">
                <p class="invalid-error" style="
                    color: #842029;
                    background-color: #f8d7da;
                    border: 1px solid #f5c2c7;
                    padding: 10px 12px;
                    border-radius: 10px;
                    font-size: 14px;
                    margin-bottom: 1rem;
                    text-align: center; "
                >${error}</p>
            </c:if>
            <form id="registerForm" action="${pageContext.request.contextPath}/register" method="post">

<!--                full name -->
                <div class="input-group">
                    <input type="text" name="fullName" placeholder="Full Name"
                           value="<c:out value='${fullName}' />"
                           required>
                </div>
                <div class="input-group">
                    <input type="text" name="phone" placeholder="Phone Number"
                           value="<c:out value='${phone}' />"
                           required>
                </div>


                <!-- Email -->
                <div class="input-group">
                    <input type="email" name="email" placeholder="Email address"
                           value="<c:out value='${email}'/> "
                           required>
                </div>

                <!-- Password -->
                <div class="input-group password-wrap">
                    <input type="password" id="password" name="password" placeholder="Password" required>
                    <button type="button" class="eye-btn" id="eyeBtn1">
                        👁️
                    </button>
                </div>

                <!-- Confirm Password -->
                <div class="input-group password-wrap">
                    <input type="password" id="confirm-password" name="confirmPassword" placeholder="Confirm Password" required>
                    <button type="button" class="eye-btn" id="eyeBtn2">
                        👁️
                    </button>
                </div>

                <!-- Strength Bar -->
                <div class="strength-bar-wrap">
                    <div class="strength-bar" id="strengthBar"></div>
                </div>
                <p class="strength-label" id="strengthLabel"></p>

                <!-- Terms -->
                <label class="terms-check">
                    <input type="checkbox" id="terms" name="pledgeAgreed" value="yes" required />
                    <span class="checkmark"></span>
                    <span>
                        I agree to the
                        <a href="#">Terms of Service</a> &
                        <a href="#">Privacy Policy</a>
                    </span>
                </label>


                <!-- Submit -->
                <button type="submit" class="register-submit-btn">
                    Create Account
                </button>

            </form>

            <!-- Login Redirect -->
            <div class="register-link">
                Already have an account? <a href="${pageContext.request.contextPath}/login">Sign In</a>
            </div>

        </div>
    </div>

    <!-- RIGHT SECTION -->
    <div class="right">
        <img src="${pageContext.request.contextPath}/Images/login_illustration.png" alt="Learning Illustration" class="right-image">
    </div>

</div>
</div>
  <script src="${pageContext.request.contextPath}/js/home.js"></script>
</body>
</html>