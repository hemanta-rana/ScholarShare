<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>ScholarShare | My Profile</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png"/>
    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@400;500;600;700&display=swap" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/sidebar.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/adminDashboard.css" rel="stylesheet"/>
    <style>
        /* ── Admin Profile Page Styles ── */
        .profile-hero {
            display: flex;
            align-items: center;
            gap: 28px;
            padding: 28px 32px;
            border-radius: var(--radius);
            background: linear-gradient(135deg, #1b2332 0%, #2c3e5a 100%);
            color: #fff;
            box-shadow: var(--shadow-card);
            flex-wrap: wrap;
            margin-bottom: 24px;
        }
        .profile-hero__avatar-wrap { position: relative; flex-shrink: 0; }
        .profile-hero__avatar {
            width: 88px; height: 88px; border-radius: 50%;
            background: linear-gradient(135deg, var(--accent-blue), #1b2332);
            color: #fff; display: flex; align-items: center; justify-content: center;
            font-size: 28px; font-weight: 700; overflow: hidden;
            border: 3px solid rgba(255,255,255,0.2);
        }
        .profile-hero__avatar img { width: 100%; height: 100%; object-fit: cover; }
        .profile-hero__photo-btn {
            position: absolute; bottom: 2px; right: 2px;
            width: 26px; height: 26px; border-radius: 50%;
            background: var(--accent-gold); display: flex;
            align-items: center; justify-content: center;
            cursor: pointer; border: 2px solid #fff; transition: background 0.15s;
        }
        .profile-hero__photo-btn:hover { background: #d99b00; }
        .profile-hero__photo-btn img { width: 12px; height: 12px; object-fit: contain; filter: none; }
        .profile-hero__info { flex: 1; min-width: 0; }
        .profile-hero__name {
            font-family: var(--font-display); font-size: 26px; font-weight: 700;
            margin-bottom: 4px; line-height: 1.2;
        }
        .profile-hero__email { font-size: 13.5px; color: rgba(255,255,255,0.6); margin-bottom: 10px; }
        .profile-hero__badge {
            display: inline-flex; align-items: center; gap: 5px;
            padding: 4px 12px; border-radius: 999px; font-size: 11px;
            font-weight: 700; letter-spacing: 0.06em; text-transform: uppercase;
            background: rgba(245,166,35,0.18); color: var(--accent-gold);
        }

        /* ── Two-column grid ── */
        .admin-profile-grid {
            display: grid;
            grid-template-columns: 1fr 340px;
            gap: 20px;
            align-items: start;
        }
        .admin-profile-right { display: flex; flex-direction: column; gap: 20px; }

        /* ── Profile card ── */
        .admin-profile-card {
            background: var(--white);
            border: 1px solid var(--border);
            border-radius: var(--radius);
            padding: 22px 24px;
            box-shadow: var(--shadow-card);
        }
        .admin-profile-card__header {
            display: flex; align-items: center; gap: 10px;
            margin-bottom: 20px; padding-bottom: 14px;
            border-bottom: 1px solid var(--border);
        }
        .admin-profile-card__header img { opacity: 0.55; flex-shrink: 0; filter: none; }
        .admin-profile-card__header h3 {
            font-family: var(--font-display); font-size: 17px;
            font-weight: 700; color: var(--text-primary);
        }

        /* ── Form fields ── */
        .ap-form { display: flex; flex-direction: column; gap: 18px; }
        .ap-field { display: flex; flex-direction: column; gap: 6px; }
        .ap-label {
            font-size: 11px; font-weight: 700; text-transform: uppercase;
            letter-spacing: 0.1em; color: var(--text-muted);
        }
        .ap-input {
            padding: 10px 14px; border: 1px solid var(--border); border-radius: 8px;
            font-family: var(--font-body); font-size: 14px; color: var(--text-primary);
            background: var(--bg); outline: none;
            transition: border-color 0.2s, background 0.2s, box-shadow 0.2s;
        }
        .ap-input:focus {
            border-color: var(--accent-blue); background: var(--white);
            box-shadow: 0 0 0 3px rgba(58,123,213,0.08);
        }
        .ap-input--readonly {
            background: #eef0f4; color: var(--text-muted); cursor: not-allowed;
        }
        .ap-hint { font-size: 11.5px; color: var(--text-muted); }
        .ap-actions { display: flex; gap: 10px; padding-top: 4px; }

        /* ── Buttons ── */
        .ap-btn {
            display: inline-flex; align-items: center; gap: 8px;
            padding: 10px 20px; border-radius: 8px; font-family: var(--font-body);
            font-size: 13.5px; font-weight: 600; cursor: pointer;
            text-decoration: none; border: none;
            transition: background 0.15s, transform 0.1s;
            white-space: nowrap;
        }
        .ap-btn--primary { background: var(--accent-blue); color: #fff; }
        .ap-btn--primary:hover { background: #2c63b5; transform: translateY(-1px); }
        .ap-btn--primary:disabled { opacity: 0.45; cursor: not-allowed; transform: none; }
        .ap-btn--ghost {
            background: var(--bg); color: var(--text-primary);
            border: 1px solid var(--border);
        }
        .ap-btn--ghost:hover { background: var(--border); }
        .ap-btn--full { width: 100%; justify-content: center; }

        /* ── Photo upload card ── */
        .ap-photo-form { display: flex; flex-direction: column; align-items: center; gap: 12px; }
        .ap-photo-preview {
            width: 88px; height: 88px; border-radius: 50%;
            background: linear-gradient(135deg, var(--accent-blue), #1b2332);
            color: #fff; display: flex; align-items: center; justify-content: center;
            font-size: 26px; font-weight: 700; overflow: hidden;
            border: 3px solid var(--border); flex-shrink: 0;
        }
        .ap-photo-preview img { width: 100%; height: 100%; object-fit: cover; }
        .ap-drop-zone {
            display: flex; align-items: center; justify-content: center; gap: 8px;
            width: 100%; padding: 12px 16px;
            border: 1.5px dashed var(--border); border-radius: 8px;
            background: var(--bg); font-size: 13px; font-weight: 600;
            color: var(--text-primary); cursor: pointer;
            transition: border-color 0.15s, background 0.15s;
        }
        .ap-drop-zone:hover { border-color: var(--accent-blue); background: #eef3fc; }
        .ap-drop-zone img { opacity: 0.55; flex-shrink: 0; filter: none; }
        .ap-file-input { display: none; }
        .ap-hint-sm { font-size: 11.5px; color: var(--text-muted); text-align: center; }

        /* ── Flash messages ── */
        .flash-success {
            padding: 12px 16px; border-radius: var(--radius); font-size: 14px;
            background: #e8f7ee; color: #1f6b3f; border: 1px solid #b9e5c8;
            margin-bottom: 4px;
        }
        .flash-error {
            padding: 12px 16px; border-radius: var(--radius); font-size: 14px;
            background: #fdecec; color: #8b2f2f; border: 1px solid #f5c2c2;
            margin-bottom: 4px;
        }

        /* ── Responsive ── */
        @media (max-width: 1024px) {
            .admin-profile-grid { grid-template-columns: 1fr; }
            .admin-profile-right { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; }
        }
        @media (max-width: 640px) {
            .profile-hero { flex-direction: column; align-items: flex-start; padding: 20px; }
            .admin-profile-right { grid-template-columns: 1fr; }
            .ap-actions { flex-direction: column; }
        }
    </style>
</head>
<body>

<jsp:include page="template/admin-sidebar.jsp">
    <jsp:param name="activePage" value="profile"/>
</jsp:include>

<div class="main">
    <jsp:include page="template/admin-header.jsp">
        <jsp:param name="searchPlaceholder" value="Search resources, users, or archives…"/>
    </jsp:include>

    <main class="content">

        <%-- Flash messages --%>
        <c:if test="${not empty flashMessage}">
            <p class="flash-success"><c:out value="${flashMessage}"/></p>
        </c:if>
        <c:if test="${not empty flashError}">
            <p class="flash-error"><c:out value="${flashError}"/></p>
        </c:if>

        <%-- ── HERO BANNER ── --%>
        <div class="profile-hero">
            <div class="profile-hero__avatar-wrap">
                <div class="profile-hero__avatar" id="heroAvatar">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user.profilePic}">
                            <img id="heroAvatarImg"
                                 src="${pageContext.request.contextPath}/${sessionScope.user.profilePic}"
                                 alt="Profile photo">
                        </c:when>
                        <c:otherwise>
                            <span id="heroAvatarInitials">
                                <c:out value="${sessionScope.user.initials}"/>
                            </span>
                        </c:otherwise>
                    </c:choose>
                </div>
                <label class="profile-hero__photo-btn" for="photoInput" title="Change photo">
                    <img src="${pageContext.request.contextPath}/images/home-icons/upload.png"
                         alt="" width="12" height="12">
                </label>
            </div>

            <div class="profile-hero__info">
                <h2 class="profile-hero__name">
                    <c:out value="${sessionScope.user.name}"/>
                </h2>
                <p class="profile-hero__email">
                    <c:out value="${sessionScope.user.email}"/>
                </p>
                <span class="profile-hero__badge">Administrator</span>
            </div>
        </div>

        <%-- ── TWO-COLUMN LAYOUT ── --%>
        <div class="admin-profile-grid">

            <%-- LEFT: Edit personal info --%>
            <div class="admin-profile-card">
                <div class="admin-profile-card__header">
                    <img src="${pageContext.request.contextPath}/images/home-icons/user-check.png"
                         alt="" width="18" height="18">
                    <h3>Personal Information</h3>
                </div>

                <form class="ap-form"
                      action="${pageContext.request.contextPath}/admin/profile"
                      method="post">
                    <input type="hidden" name="action" value="updateInfo">

                    <div class="ap-field">
                        <label class="ap-label" for="fullName">Full Name</label>
                        <input class="ap-input" type="text" id="fullName" name="fullName"
                               value="<c:out value='${sessionScope.user.name}'/>"
                               placeholder="Your full name" required>
                    </div>

                    <div class="ap-field">
                        <label class="ap-label" for="emailDisplay">Email Address</label>
                        <input class="ap-input ap-input--readonly" type="email"
                               id="emailDisplay"
                               value="<c:out value='${sessionScope.user.email}'/>"
                               readonly disabled
                               title="Email cannot be changed">
                        <p class="ap-hint">Email address cannot be changed.</p>
                    </div>

                    <div class="ap-field">
                        <label class="ap-label" for="phone">Phone Number</label>
                        <input class="ap-input" type="tel" id="phone" name="phone"
                               value="<c:out value='${sessionScope.user.phone}'/>"
                               placeholder="e.g. +44 7700 900000">
                    </div>

                    <div class="ap-actions">
                        <button type="submit" class="ap-btn ap-btn--primary">
                            <img src="${pageContext.request.contextPath}/images/home-icons/circle-check.png"
                                 alt="" width="15" height="15" style="filter:brightness(0) invert(1);">
                            Save Changes
                        </button>
                        <a href="${pageContext.request.contextPath}/admin/dashboard"
                           class="ap-btn ap-btn--ghost">
                            Cancel
                        </a>
                    </div>
                </form>
            </div>

            <%-- RIGHT column --%>
            <div class="admin-profile-right">

                <%-- Photo upload card --%>
                <div class="admin-profile-card">
                    <div class="admin-profile-card__header">
                        <img src="${pageContext.request.contextPath}/images/home-icons/upload.png"
                             alt="" width="18" height="18">
                        <h3>Profile Photo</h3>
                    </div>

                    <form class="ap-photo-form"
                          action="${pageContext.request.contextPath}/admin/profile"
                          method="post"
                          enctype="multipart/form-data"
                          id="photoForm">
                        <input type="hidden" name="action" value="uploadPhoto">

                        <%-- Preview --%>
                        <div class="ap-photo-preview" id="photoPreview">
                            <c:choose>
                                <c:when test="${not empty sessionScope.user.profilePic}">
                                    <img id="photoPreviewImg"
                                         src="${pageContext.request.contextPath}/${sessionScope.user.profilePic}"
                                         alt="Current photo">
                                </c:when>
                                <c:otherwise>
                                    <span id="photoPreviewInitials">
                                        <c:out value="${sessionScope.user.initials}"/>
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <label class="ap-drop-zone" for="photoInput">
                            <img src="${pageContext.request.contextPath}/images/home-icons/upload.png"
                                 alt="" width="18" height="18">
                            <span id="photoDropLabel">Click to choose a photo</span>
                            <input type="file" id="photoInput" name="profilePic"
                                   accept="image/jpeg,image/png,image/gif,image/webp"
                                   class="ap-file-input">
                        </label>
                        <p class="ap-hint-sm">JPG, PNG, GIF or WebP · Max 5 MB</p>

                        <button type="submit" class="ap-btn ap-btn--primary ap-btn--full"
                                id="photoSaveBtn" disabled>
                            <img src="${pageContext.request.contextPath}/images/home-icons/circle-check.png"
                                 alt="" width="15" height="15" style="filter:brightness(0) invert(1);">
                            Upload Photo
                        </button>
                    </form>
                </div>

                <%-- Account info card --%>
                <div class="admin-profile-card">
                    <div class="admin-profile-card__header">
                        <img src="${pageContext.request.contextPath}/images/icons-admin/dashboard.png"
                             alt="" width="18" height="18">
                        <h3>Account Details</h3>
                    </div>
                    <div style="display:flex;flex-direction:column;gap:14px;">
                        <div>
                            <p style="font-size:11px;font-weight:700;text-transform:uppercase;letter-spacing:0.1em;color:var(--text-muted);margin-bottom:4px;">Role</p>
                            <p style="font-size:14px;font-weight:600;color:var(--text-primary);">Administrator</p>
                        </div>
                        <div>
                            <p style="font-size:11px;font-weight:700;text-transform:uppercase;letter-spacing:0.1em;color:var(--text-muted);margin-bottom:4px;">Status</p>
                            <span style="display:inline-flex;align-items:center;gap:5px;padding:4px 10px;border-radius:20px;font-size:11px;font-weight:700;background:#e8f7ee;color:#1f6b3f;">
                                Active
                            </span>
                        </div>
                        <div>
                            <p style="font-size:11px;font-weight:700;text-transform:uppercase;letter-spacing:0.1em;color:var(--text-muted);margin-bottom:4px;">Email</p>
                            <p style="font-size:13.5px;color:var(--text-primary);word-break:break-all;">
                                <c:out value="${sessionScope.user.email}"/>
                            </p>
                        </div>
                    </div>
                </div>

            </div><%-- end .admin-profile-right --%>
        </div><%-- end .admin-profile-grid --%>

    </main>

    <footer class="site-footer">
        <span class="footer-copy">© 2023 ScholarShare · Institutional Repository Management System</span>
    </footer>

    <div class="sidebar-overlay" id="sidebarOverlay"></div>
    <script src="${pageContext.request.contextPath}/js/admin-dashboard-navbar.js"></script>
</div>

<script>
(function () {
    'use strict';

    const photoInput     = document.getElementById('photoInput');
    const photoSaveBtn   = document.getElementById('photoSaveBtn');
    const photoDropLabel = document.getElementById('photoDropLabel');
    const photoPreview   = document.getElementById('photoPreview');
    const heroAvatarImg  = document.getElementById('heroAvatarImg');
    const heroInitials   = document.getElementById('heroAvatarInitials');

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
                if (heroInitials) heroInitials.style.display = 'none';
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
