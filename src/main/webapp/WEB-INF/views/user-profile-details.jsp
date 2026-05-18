<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ScholarShare | My Profile</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/student-dashboard.css">
</head>
<body>

<jsp:include page="template/student-sidebar.jsp">
    <jsp:param name="activePage" value="profile" />
</jsp:include>

<div class="main student-main">

    <jsp:include page="template/student-header.jsp">
        <jsp:param name="pageTitle" value="My Profile" />
    </jsp:include>

    <main class="content student-content">

        <%-- Flash messages --%>
        <c:if test="${not empty flashMessage}">
            <p class="flash flash-success"><c:out value="${flashMessage}"/></p>
        </c:if>
        <c:if test="${not empty flashError}">
            <p class="flash flash-error"><c:out value="${flashError}"/></p>
        </c:if>

        <%-- ── PROFILE HERO CARD ── --%>
        <div class="profile-hero">
            <div class="profile-hero__avatar-wrap">
                <div class="profile-hero__avatar" id="heroAvatar">
                    <c:choose>
                        <c:when test="${not empty profile.avatarUrl}">
                            <img src="${profile.avatarUrl}" alt="Profile photo" id="heroAvatarImg">
                        </c:when>
                        <c:otherwise>
                            <span id="heroAvatarInitials"><c:out value="${profile.initials}"/></span>
                        </c:otherwise>
                    </c:choose>
                </div>
                <label class="profile-hero__photo-btn" for="photoInput" title="Change photo">
                    <img src="${pageContext.request.contextPath}/images/home-icons/upload.png" alt="" width="14" height="14">
                </label>
            </div>

            <div class="profile-hero__info">
                <h2 class="profile-hero__name"><c:out value="${profile.name}"/></h2>
                <p class="profile-hero__email"><c:out value="${profile.email}"/></p>
                <div class="profile-hero__badges">
                    <span class="profile-badge profile-badge--role">Student</span>
                    <span class="profile-badge profile-badge--tier">
                        <img src="${pageContext.request.contextPath}/images/home-icons/medal.png" alt="" width="12" height="12">
                        <c:out value="${reputation.tierLabel}"/>
                    </span>
                    <span class="profile-badge profile-badge--score">
                        <c:out value="${reputation.score}"/> / 100
                    </span>
                </div>
            </div>

            <%-- Quick stats --%>
            <div class="profile-hero__stats">
                <div class="profile-stat">
                    <span class="profile-stat__val"><c:out value="${summary.approvedUploads}"/></span>
                    <span class="profile-stat__lbl">Approved</span>
                </div>
                <div class="profile-stat-divider"></div>
                <div class="profile-stat">
                    <span class="profile-stat__val"><c:out value="${summary.pendingReview}"/></span>
                    <span class="profile-stat__lbl">Pending</span>
                </div>
                <div class="profile-stat-divider"></div>
                <div class="profile-stat">
                    <span class="profile-stat__val"><c:out value="${summary.savedResources}"/></span>
                    <span class="profile-stat__lbl">Saved</span>
                </div>
            </div>
        </div>

        <%-- ── TWO-COLUMN LAYOUT ── --%>
        <div class="profile-grid">

            <%-- LEFT: Edit personal info --%>
            <section class="profile-card">
                <div class="profile-card__header">
                    <img src="${pageContext.request.contextPath}/images/home-icons/user-check.png" alt="" width="18" height="18">
                    <h3>Personal Information</h3>
                </div>

                <form class="profile-form"
                      action="${pageContext.request.contextPath}/student/profile"
                      method="post">
                    <input type="hidden" name="action" value="updateInfo">

                    <div class="profile-field">
                        <label class="profile-field__label" for="fullName">Full Name</label>
                        <input class="profile-field__input" type="text" id="fullName" name="fullName"
                               value="<c:out value='${profile.name}'/>"
                               placeholder="Your full name" required>
                    </div>

                    <div class="profile-field">
                        <label class="profile-field__label" for="emailDisplay">Email Address</label>
                        <input class="profile-field__input profile-field__input--readonly"
                               type="email" id="emailDisplay"
                               value="<c:out value='${profile.email}'/>"
                               readonly disabled
                               title="Email cannot be changed">
                        <p class="profile-field__hint">Email address cannot be changed.</p>
                    </div>

                    <div class="profile-field">
                        <label class="profile-field__label" for="phone">Phone Number</label>
                        <input class="profile-field__input" type="tel" id="phone" name="phone"
                               value="<c:out value='${profile.phone}'/>"
                               placeholder="e.g. +44 7700 900000">
                    </div>

                    <div class="profile-form__actions">
                        <button type="submit" class="profile-btn profile-btn--primary">
                            <img src="${pageContext.request.contextPath}/images/home-icons/circle-check.png" alt="" width="15" height="15">
                            Save Changes
                        </button>
                        <a href="${pageContext.request.contextPath}/student/dashboard" class="profile-btn profile-btn--ghost">
                            Cancel
                        </a>
                    </div>
                </form>
            </section>

            <%-- RIGHT: Photo upload + reputation --%>
            <div class="profile-right-col">

                <%-- Photo upload card --%>
                <section class="profile-card">
                    <div class="profile-card__header">
                        <img src="${pageContext.request.contextPath}/images/home-icons/upload.png" alt="" width="18" height="18">
                        <h3>Profile Photo</h3>
                    </div>

                    <form class="profile-photo-form"
                          action="${pageContext.request.contextPath}/student/profile"
                          method="post"
                          enctype="multipart/form-data"
                          id="photoForm">
                        <input type="hidden" name="action" value="uploadPhoto">

                        <%-- Preview --%>
                        <div class="photo-preview" id="photoPreview">
                            <c:choose>
                                <c:when test="${not empty profile.avatarUrl}">
                                    <img src="${profile.avatarUrl}" alt="Current photo" id="photoPreviewImg">
                                </c:when>
                                <c:otherwise>
                                    <span id="photoPreviewInitials"><c:out value="${profile.initials}"/></span>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <label class="photo-drop-zone" for="photoInput">
                            <img src="${pageContext.request.contextPath}/images/home-icons/upload.png" alt="" width="20" height="20">
                            <span id="photoDropLabel">Click to choose a photo</span>
                            <input type="file" id="photoInput" name="profilePic"
                                   accept="image/jpeg,image/png,image/gif,image/webp"
                                   class="photo-input">
                        </label>
                        <p class="photo-hint">JPG, PNG, GIF or WebP · Max 5 MB</p>

                        <button type="submit" class="profile-btn profile-btn--primary profile-btn--full"
                                id="photoSaveBtn" disabled>
                            <img src="${pageContext.request.contextPath}/images/home-icons/circle-check.png" alt="" width="15" height="15">
                            Upload Photo
                        </button>
                    </form>
                </section>

                <%-- Reputation card --%>
                <section class="profile-card">
                    <div class="profile-card__header">
                        <img src="${pageContext.request.contextPath}/images/home-icons/reputation-score.png" alt="" width="18" height="18">
                        <h3>Reputation</h3>
                    </div>

                    <div class="rep-ring-wrap">
                        <div class="rep-ring" style="--score:${reputation.score}">
                            <div class="rep-ring-inner">
                                <strong><c:out value="${reputation.score}"/>%</strong>
                                <span><c:out value="${reputation.tierLabel}"/></span>
                            </div>
                        </div>
                    </div>

                    <p class="rep-message"><c:out value="${reputation.percentileMessage}"/></p>

                    <div class="rep-metric">
                        <div class="rep-metric__label">
                            <span>Accuracy Rate</span>
                            <strong><c:out value="${reputation.accuracyRate}"/>%</strong>
                        </div>
                        <div class="rep-metric__bar">
                            <span style="width:${reputation.accuracyRate}%"></span>
                        </div>
                    </div>

                    <div class="rep-metric" style="margin-top:12px;">
                        <div class="rep-metric__label">
                            <span>Peer Helpfulness</span>
                            <strong><c:out value="${reputation.peerHelpfulness}"/>%</strong>
                        </div>
                        <div class="rep-metric__bar">
                            <span style="width:${reputation.peerHelpfulness}%"></span>
                        </div>
                    </div>
                </section>

            </div><%-- end .profile-right-col --%>
        </div><%-- end .profile-grid --%>

    </main>
</div>

<script src="${pageContext.request.contextPath}/js/student-dashboard.js"></script>
<script>
(function () {
    'use strict';

    const photoInput    = document.getElementById('photoInput');
    const photoSaveBtn  = document.getElementById('photoSaveBtn');
    const photoDropLabel = document.getElementById('photoDropLabel');
    const photoPreview  = document.getElementById('photoPreview');
    const heroAvatarImg = document.getElementById('heroAvatarImg');
    const heroAvatarInitials = document.getElementById('heroAvatarInitials');

    if (photoInput) {
        photoInput.addEventListener('change', function () {
            const file = this.files[0];
            if (!file) {
                photoSaveBtn.disabled = true;
                photoDropLabel.textContent = 'Click to choose a photo';
                return;
            }

            if (file.size > 5 * 1024 * 1024) {
                alert('File is too large. Maximum size is 5 MB.');
                this.value = '';
                photoSaveBtn.disabled = true;
                photoDropLabel.textContent = 'Click to choose a photo';
                return;
            }

            photoDropLabel.textContent = file.name;
            photoSaveBtn.disabled = false;

            /* Live preview in both the card and the hero avatar */
            const reader = new FileReader();
            reader.onload = function (ev) {
                /* Update card preview */
                const existingImg = document.getElementById('photoPreviewImg');
                const initials    = document.getElementById('photoPreviewInitials');
                if (initials) initials.style.display = 'none';
                if (existingImg) {
                    existingImg.src = ev.target.result;
                } else {
                    const img = document.createElement('img');
                    img.id  = 'photoPreviewImg';
                    img.src = ev.target.result;
                    img.alt = 'Preview';
                    photoPreview.appendChild(img);
                }

                /* Update hero avatar */
                if (heroAvatarInitials) heroAvatarInitials.style.display = 'none';
                if (heroAvatarImg) {
                    heroAvatarImg.src = ev.target.result;
                } else {
                    const img = document.createElement('img');
                    img.id  = 'heroAvatarImg';
                    img.src = ev.target.result;
                    img.alt = 'Preview';
                    document.getElementById('heroAvatar').appendChild(img);
                }
            };
            reader.readAsDataURL(file);
        });
    }
})();
</script>
</body>
</html>
