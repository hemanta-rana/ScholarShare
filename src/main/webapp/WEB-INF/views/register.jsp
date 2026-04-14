<!--<%@ page contentType="text/html;charset=UTF-8" %>-->
<!--<%@ taglib prefix="c" uri="jakarta.tags.core" %>-->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=yes">
    <title>ScholarPass | Create Account</title>

    <link rel="stylesheet" href="../../css/home.css">
    <link rel="stylesheet" href="../../css/Register.css">
    <script src="../../js/Register.js" defer></script>
</head>
<body>

    <!-- TopNavBar -->
    <header class="app-header">
        <nav class="nav-container max-w-7xl">
            <div class="nav-logo">ScholarShare</div>
            <div class="nav-links">
                <a class="nav-link" href="Home.html">Home</a>
                <a class="nav-link" href="#">How It Works</a>
                <a class="nav-link" href="#">About</a>
                <a class="nav-link" href="#">Contact</a>
            </div>
            <div class="nav-button">
            </div>
        </nav>
    </header>

<div class="register-wrapper">
<div class="container">

    <!-- LEFT SECTION -->
    <div class="left">
        <div class="login-card">

            <!-- Logo -->
            <div class="shield-icon">🛡️</div>

            <h2>Create Account</h2>
            <p class="welcome-text">Join us and start learning today</p>

            <form id="registerForm" action="#" method="post">

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
                    <input type="checkbox" id="terms" required />
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
                Already have an account? <a href="${pageContext.request.contextPath}/auth/login">Sign In</a>
            </div>

        </div>
    </div>

    <!-- RIGHT SECTION (UNCHANGED IMAGE SIDE) -->
    <div class="right">
        <img src="../../Images/Login_left%20image.png" alt="Learning Illustration" class="right-image">
    </div>

</div>
</div>
<script>
document.addEventListener('DOMContentLoaded', () => {
    const wrapper = document.querySelector('.login-wrapper') || document.querySelector('.register-wrapper');
    if (wrapper) wrapper.classList.add('page-flip-in');

    document.querySelectorAll('a').forEach(link => {
        if (link.href.includes('Login.html') || link.href.includes('Register.html')) {
            link.addEventListener('click', (e) => {
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
</body>
</html>