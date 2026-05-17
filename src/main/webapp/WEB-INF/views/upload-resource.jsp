<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ScholarShare | Upload Resource</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/headerFooter.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Register.css"/>
</head>
<body>

<div id="navbar-wrapper">
    <nav id="navbar">
        <div class="nav-row">
            <a href="${pageContext.request.contextPath}/home" class="nav-logo">
                <img src="${pageContext.request.contextPath}/images/logo.png" alt="ScholarShare" class="nav-logo-icon">
                <span>ScholarShare</span>
            </a>
            <ul class="nav-links">
                <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/student/upload-resource">Upload</a></li>
            </ul>
            <a href="${pageContext.request.contextPath}/login" class="nav-cta">Get Started</a>
        </div>
    </nav>
</div>

<div class="register-wrapper">
    <div class="container">
        <div class="left">
            <div class="login-card">
                <h2>Upload Resource</h2>
                <p class="welcome-text">Submit your study material for review</p>

                <c:if test="${not empty error}">
                    <p class="invalid-error" style="
                    color: #842029;
                    background-color: #f8d7da;
                    border: 1px solid #f5c2c7;
                    padding: 10px 12px;
                    border-radius: 10px;
                    font-size: 14px;
                    margin-bottom: 1rem;
                    text-align: center;">${error}</p>
                </c:if>

                <form action="${pageContext.request.contextPath}/student/upload-resource" method="post" enctype="multipart/form-data">
                    <div class="input-group">
                        <input type="text" name="title" placeholder="Title" required>
                    </div>

                    <div class="input-group">
                        <textarea name="description" placeholder="Description" required style="width:100%;padding:12px;border-radius:12px;border:1px solid #d0d7de;"></textarea>
                    </div>

                    <div class="input-group">
                        <select name="resourceType" required style="width:100%;padding:12px;border-radius:12px;border:1px solid #d0d7de;">
                            <option value="">Resource Type</option>
                            <option value="notes">Notes</option>
                            <option value="past_paper">Past Paper</option>
                            <option value="summary">Summary</option>
                            <option value="revision_guide">Revision Guide</option>
                            <option value="other">Other</option>
                        </select>
                    </div>

                    <div class="input-group">
                        <select name="topicCategory" required style="width:100%;padding:12px;border-radius:12px;border:1px solid #d0d7de;">
                            <option value="">Topic Category</option>
                            <c:forEach var="topic" items="${topics}">
                                <option value="${topic.topicId}">
                                        ${topic.facultyName} &gt; ${topic.subjectName} &gt; ${topic.topicName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="input-group">
                        <input type="file" name="resourceFile" accept=".pdf,.docx" required>
                    </div>

                    <label class="terms-check">
                        <input type="checkbox" name="pledgeAgreed" value="yes" required />
                        <span class="checkmark"></span>
                        <span>
                        I confirm that this uploaded resource is my original academic work or properly cited material, and I understand that submitting plagiarized content violates academic integrity policies.
                    </span>
                    </label>

                    <button type="submit" class="register-submit-btn">Upload Resource</button>
                </form>
            </div>
        </div>

        <div class="right">
            <img src="${pageContext.request.contextPath}/images/login_illustration.png" alt="Upload" class="right-image">
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/home.js"></script>
</body>
</html>