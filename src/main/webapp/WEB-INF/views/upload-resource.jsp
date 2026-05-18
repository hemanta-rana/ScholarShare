<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ScholarShare | Upload a Resource</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:ital,wght@0,500;0,600;0,700;1,500&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/student-dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/upload-resource.css">
</head>
<body>

<jsp:include page="template/student-sidebar.jsp">
    <jsp:param name="activePage" value="upload" />
</jsp:include>

<div class="main student-main">
    <jsp:include page="template/student-upload-header.jsp"/>

    <main class="content student-content upload-page">

        <header class="upload-page-header">
            <h2>Upload a Resource</h2>
            <p class="upload-page-sub">Contributing to the collective knowledge of our academic community.</p>
        </header>

        <c:if test="${not empty error}">
            <p class="flash flash-error"><c:out value="${error}"/></p>
        </c:if>

        <form id="uploadForm"
              action="${pageContext.request.contextPath}/student/upload-resource"
              method="post"
              enctype="multipart/form-data"
              class="upload-form"
              novalidate>

            <div class="upload-layout">
                <div class="upload-main">

                    <nav class="upload-stepper" aria-label="Upload progress">
                        <button type="button" class="stepper-item active" data-step="1">
                            <span class="stepper-num">1</span>
                            <span class="stepper-label">File Upload</span>
                        </button>
                        <button type="button" class="stepper-item" data-step="2">
                            <span class="stepper-num">2</span>
                            <span class="stepper-label">Resource Details</span>
                        </button>
                        <button type="button" class="stepper-item" data-step="3">
                            <span class="stepper-num">3</span>
                            <span class="stepper-label">Review &amp; Submit</span>
                        </button>
                    </nav>

                    <!-- Step 1 -->
                    <section class="upload-panel active" data-panel="1">
                        <div class="upload-card">
                            <div class="drop-zone" id="dropZone">
                                <input type="file" name="resourceFile" id="resourceFile"
                                       accept=".pdf,.docx,.zip" class="drop-zone-input" required>
                                <div class="drop-zone-content">
                                    <div class="drop-zone-icon">
                                        <img src="${pageContext.request.contextPath}/images/home-icons/upload.png" alt="">
                                    </div>
                                    <h3>Drag and drop your manuscript</h3>
                                    <p>Limit 50MB per file. Supported formats: PDF, DOCX, ZIP.</p>
                                    <button type="button" class="browse-btn" id="browseBtn">Browse Files</button>
                                </div>
                                <p class="file-selected" id="fileSelected" hidden></p>
                            </div>
                        </div>

                        <div class="metadata-card">
                            <h3>Resource Metadata</h3>
                            <div class="field-row">
                                <label class="field-label" for="title">Resource Title</label>
                                <input type="text" id="title" name="title" class="field-input"
                                       placeholder="e.g. Advanced Microeconomics Notes"
                                       value="<c:out value='${formTitle}'/>" required>
                            </div>
                            <div class="field-row">
                                <label class="field-label" for="courseCode">Course Code</label>
                                <input type="text" id="courseCode" name="courseCode" class="field-input"
                                       placeholder="e.g. ECON301"
                                       value="<c:out value='${formCourseCode}'/>">
                            </div>
                        </div>
                    </section>

                    <!-- Step 2 -->
                    <section class="upload-panel" data-panel="2">
                        <div class="metadata-card">
                            <h3>Resource Details</h3>
                            <div class="field-row">
                                <label class="field-label" for="description">Description</label>
                                <textarea id="description" name="description" class="field-textarea" rows="5"
                                          placeholder="Summarize the content, scope, and intended audience..."
                                          required><c:out value="${formDescription}"/></textarea>
                            </div>
                            <div class="field-row field-row--grid">
                                <div>
                                    <label class="field-label" for="resourceType">Resource Type</label>
                                    <select id="resourceType" name="resourceType" class="field-select" required>
                                        <option value="">Select type</option>
                                        <option value="notes" ${formResourceType eq 'notes' ? 'selected' : ''}>Notes</option>
                                        <option value="past_paper" ${formResourceType eq 'past_paper' ? 'selected' : ''}>Past Paper</option>
                                        <option value="summary" ${formResourceType eq 'summary' ? 'selected' : ''}>Summary</option>
                                        <option value="revision_guide" ${formResourceType eq 'revision_guide' ? 'selected' : ''}>Revision Guide</option>
                                        <option value="other" ${formResourceType eq 'other' ? 'selected' : ''}>Other</option>
                                    </select>
                                </div>
                                <div>
                                    <label class="field-label" for="topicCategory">Topic Category</label>
                                    <select id="topicCategory" name="topicCategory" class="field-select" required>
                                        <option value="">Faculty &gt; Subject &gt; Topic</option>
                                        <c:forEach var="topic" items="${topics}">
                                            <option value="${topic.topicId}"
                                                    ${formTopicCategory == topic.topicId ? 'selected' : ''}>
                                                <c:out value="${topic.facultyName}"/> &gt;
                                                <c:out value="${topic.subjectName}"/> &gt;
                                                <c:out value="${topic.topicName}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </section>

                    <!-- Step 3 -->
                    <section class="upload-panel" data-panel="3">
                        <div class="metadata-card review-card">
                            <h3>Review &amp; Submit</h3>
                            <dl class="review-list">
                                <div><dt>Title</dt><dd id="reviewTitle">—</dd></div>
                                <div><dt>Course Code</dt><dd id="reviewCourse">—</dd></div>
                                <div><dt>Description</dt><dd id="reviewDescription">—</dd></div>
                                <div><dt>Resource Type</dt><dd id="reviewType">—</dd></div>
                                <div><dt>Topic</dt><dd id="reviewTopic">—</dd></div>
                                <div><dt>File</dt><dd id="reviewFile">—</dd></div>
                            </dl>

                            <label class="pledge-check">
                                <input type="checkbox" name="pledgeAgreed" value="yes" required>
                                <span class="pledge-box"></span>
                                <span class="pledge-text">
                                    I confirm this resource is my original academic work or properly cited material,
                                    and I understand that plagiarized submissions violate institutional integrity policies.
                                </span>
                            </label>
                        </div>
                    </section>

                    <footer class="upload-footer">
                        <a href="${pageContext.request.contextPath}/student/dashboard" class="cancel-link">
                            <img src="${pageContext.request.contextPath}/images/home-icons/link.png" alt="" class="cancel-icon">
                            Cancel Upload
                        </a>
                        <div class="upload-footer-actions">
                            <button type="button" class="btn-secondary" id="prevBtn" hidden>Back</button>
                            <button type="button" class="btn-primary" id="nextBtn">Proceed to Details</button>
                            <button type="submit" class="btn-primary" id="submitBtn" hidden>Submit Resource</button>
                        </div>
                    </footer>
                </div>

                <aside class="upload-aside">
                    <div class="integrity-card">
                        <img class="integrity-icon" src="${pageContext.request.contextPath}/images/home-icons/shield-check.png" alt="">
                        <h4>Academic Integrity</h4>
                        <p>
                            All manuscripts undergo automated plagiarism detection. By uploading,
                            you certify that you hold the rights to share this material with the university archive.
                        </p>
                    </div>

                    <div class="impact-card">
                        <h4>Institutional Impact</h4>
                        <div class="impact-stats">
                            <div>
                                <span class="impact-label">Monthly Contributors</span>
                                <strong class="impact-value"><c:out value="${impact.monthlyContributors}"/></strong>
                            </div>
                            <div>
                                <span class="impact-label">Archive Citations</span>
                                <strong class="impact-value"><c:out value="${impact.archiveCitations}"/></strong>
                            </div>
                        </div>
                        <div class="impact-bar-wrap">
                            <div class="impact-bar">
                                <span style="width:${impact.expansionGoal}%"></span>
                            </div>
                            <p><c:out value="${impact.expansionGoal}"/>% towards archive expansion goal</p>
                        </div>
                    </div>
                </aside>
            </div>
        </form>
    </main>
</div>

<script src="${pageContext.request.contextPath}/js/student-dashboard.js"></script>
<script src="${pageContext.request.contextPath}/js/upload-resource.js"></script>
</body>
</html>
