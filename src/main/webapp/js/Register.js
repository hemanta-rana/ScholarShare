// ==============================
// REGISTER PAGE SCRIPT
// ==============================

document.addEventListener("DOMContentLoaded", () => {

    // ==========================
    // ELEMENT REFERENCES
    // ==========================
    const form            = document.getElementById("registerForm");
    const passwordInput   = document.getElementById("password");
    const confirmInput    = document.getElementById("confirm-password");
    const strengthBar     = document.getElementById("strengthBar");
    const strengthLabel   = document.getElementById("strengthLabel");
    const eyeBtn1         = document.getElementById("eyeBtn1");
    const eyeBtn2         = document.getElementById("eyeBtn2");
    const termsCheckbox   = document.getElementById("terms");

    // ==========================
    // PASSWORD TOGGLE FUNCTION
    // ==========================
    function togglePassword(input, button) {
        const isHidden = input.type === "password";
        input.type = isHidden ? "text" : "password";
        button.textContent = isHidden ? "🙈" : "👁️";
    }

    // Attach toggle events
    eyeBtn1.addEventListener("click", () => togglePassword(passwordInput, eyeBtn1));
    eyeBtn2.addEventListener("click", () => togglePassword(confirmInput, eyeBtn2));


    // ==========================
    // PASSWORD STRENGTH CHECKER
    // ==========================
    passwordInput.addEventListener("input", () => {

        const password = passwordInput.value;
        let score = 0;

        // Rules
        if (password.length >= 6) score++;
        if (/[A-Z]/.test(password)) score++;
        if (/[0-9]/.test(password)) score++;
        if (/[^A-Za-z0-9]/.test(password)) score++;

        // Strength levels
        const levels = [
            { width: "0%",   color: "transparent", text: "" },
            { width: "25%",  color: "#FF6B6B",     text: "Weak" },
            { width: "50%",  color: "#F5A623",     text: "Fair" },
            { width: "75%",  color: "#00C9B1",     text: "Good" },
            { width: "100%", color: "#00A896",     text: "Strong" }
        ];

        const level = levels[score];

        strengthBar.style.width      = level.width;
        strengthBar.style.background = level.color;
        strengthLabel.textContent    = level.text;
        strengthLabel.style.color    = level.color;
    });


    // ==========================
    // CONFIRM PASSWORD VALIDATION
    // ==========================
    function validatePasswords() {
        if (confirmInput.value === "") return true;

        if (passwordInput.value !== confirmInput.value) {
            confirmInput.setCustomValidity("Passwords do not match");
            return false;
        } else {
            confirmInput.setCustomValidity("");
            return true;
        }
    }

    confirmInput.addEventListener("input", validatePasswords);
    passwordInput.addEventListener("input", validatePasswords);


    // ==========================
    // FORM VALIDATION
    // ==========================
    form.addEventListener("submit", (e) => {

        let isValid = true;

        // Password match check
        if (!validatePasswords()) {
            isValid = false;
        }

        // Terms check
        if (!termsCheckbox.checked) {
            alert("You must agree to the Terms and Privacy Policy.");
            isValid = false;
        }

        // Password strength enforcement (minimum: Fair)
        const password = passwordInput.value;
        let score = 0;

        if (password.length >= 6) score++;
        if (/[A-Z]/.test(password)) score++;
        if (/[0-9]/.test(password)) score++;
        if (/[^A-Za-z0-9]/.test(password)) score++;

        if (score < 2) {
            alert("Password is too weak. Please make it stronger.");
            isValid = false;
        }

        // Final decision
        if (!isValid) {
            e.preventDefault(); // STOP FORM SUBMISSION
        }
    });

});