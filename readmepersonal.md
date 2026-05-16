# student_dashboard.html → student_dashboard.jsp Migration Guide

This guide walks you through converting `student_dashboard.html` into a proper JSP file
that reads real data from the session (logged-in user) and from lists passed by a
servlet.  
Follow each commit step in order. **Do not touch the CSS or JS files.**

---

## Prerequisites / Background

| Item | Value |
|---|---|
| Source file | `src/main/webapp/WEB-INF/views/student_dashboard.html` |
| Target file | `src/main/webapp/WEB-INF/views/student_dashboard.jsp` *(new file you create)* |
| Session key | `"user"` – a `com.ScholarShare.entity.User` object stored by `LoginServlet` |
| JSTL prefix | `c` – already used in `register.jsp` and `home.jsp` |
| Context path | Always use `${pageContext.request.contextPath}` for URLs |

The `User` entity exposes: `userId`, `fullName`, `email`, `phone`, `role`,
`status`, `profilePic`, `createdAt`.

---

## COMMIT 1 — Create the file and add JSP page directives

**Commit message:** `feat: create student_dashboard.jsp with JSP page directives`

### What to do

1. **Create** a new empty file at:
   ```
   src/main/webapp/WEB-INF/views/student_dashboard.jsp
   ```

2. **At the very top of the new file (before `<!DOCTYPE html>`)**, add these two lines:

   ```jsp
   <%@ page contentType="text/html;charset=UTF-8" %>
   <%@ taglib prefix="c" uri="jakarta.tags.core" %>
   ```

3. Then copy the entire contents of `student_dashboard.html` below those two lines.

### Why
JSP files need the `page` directive so the server knows the content type and charset.
The `taglib` directive enables JSTL `<c:...>` tags used in later commits.

---

## COMMIT 2 — Fix the CSS and JS paths to use contextPath

**Commit message:** `fix: replace relative paths with contextPath for CSS and JS`

### What to DELETE (line 9 in the original HTML)
```html
<link rel="stylesheet" href="../../css/student_dashboard.css" />
```

### What to ADD in its place
```jsp
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/student_dashboard.css" />
```

---

### What to DELETE (line 288 in the original HTML)
```html
<script src="../../js/student_dashboard.js"></script>
```

### What to ADD in its place
```jsp
<script src="${pageContext.request.contextPath}/js/student_dashboard.js"></script>
```

### Why
Relative `../../` paths break when a servlet forwards to the JSP because the browser
thinks the URL is the servlet URL, not the file path. `contextPath` always points to
the application root.

---

## COMMIT 3 — Display the logged-in user's name and avatar in the sidebar

**Commit message:** `feat: bind sidebar user card to session user object`

The sidebar currently has hard-coded placeholder text and a fixed avatar seed.

### What to DELETE (lines 32–37)
```html
<img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Alex" alt="" class="sidebar__avatar" width="48"
    height="48" />
<div class="sidebar__user-meta">
    <span class="sidebar__user-name">Username</span>
    <span class="sidebar__score-badge">Score</span>
</div>
```

### What to ADD in its place
```jsp
<img src="https://api.dicebear.com/7.x/avataaars/svg?seed=${sessionScope.user.fullName}"
     alt="Avatar of ${sessionScope.user.fullName}"
     class="sidebar__avatar" width="48" height="48" />
<div class="sidebar__user-meta">
    <span class="sidebar__user-name">${sessionScope.user.fullName}</span>
    <span class="sidebar__score-badge">${sessionScope.user.role}</span>
</div>
```

### Why
`sessionScope.user` accesses the `User` object that `LoginServlet` stores via
`SessionUtil.setAttribute(req, "user", user)`. The avatar seed is changed from the
hard-coded `"Alex"` to the real user's name so each user gets a unique avatar.

---

## COMMIT 4 — Display the logged-in user's avatar in the top header

**Commit message:** `feat: bind top-header avatar to session user`

### What to DELETE (lines 98–101)
```html
<a href="#" class="top-header__user-menu" title="Account menu" aria-label="Account menu">
    <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Alex" alt="" width="40" height="40" />
    <span class="top-header__caret" aria-hidden="true">▾</span>
</a>
```

### What to ADD in its place
```jsp
<a href="${pageContext.request.contextPath}/profile"
   class="top-header__user-menu" title="Account menu" aria-label="Account menu">
    <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=${sessionScope.user.fullName}"
         alt="Avatar of ${sessionScope.user.fullName}" width="40" height="40" />
    <span class="top-header__caret" aria-hidden="true">▾</span>
</a>
```

---

## COMMIT 5 — Make the hero welcome greeting dynamic

**Commit message:** `feat: replace hard-coded greeting with dynamic user name in hero`

### What to DELETE (line 113)
```html
<h2 id="hero-heading" class="hero__title">Welcome back, Alex</h2>
```

### What to ADD in its place
```jsp
<h2 id="hero-heading" class="hero__title">Welcome back, ${sessionScope.user.fullName}</h2>
```

### Why
The greeting was hard-coded to "Alex". Using `${sessionScope.user.fullName}` shows the
actual logged-in student's name.

---

## COMMIT 6 — Replace hard-coded stat card values with session/request attributes

**Commit message:** `feat: bind stat cards to dynamic model attributes`

The four stat cards currently show hard-coded numbers (87, 14, 3, 22).
The servlet will need to put these values into request attributes before forwarding
(you will handle that in the servlet commit). For now, update the JSP to read from EL
expressions with fallback defaults.

### Reputation Score card — What to DELETE (lines 127–131)
```html
<p class="stat-card__value">87 <span class="stat-card__suffix">/ 100</span></p>
<div class="progress-bar" role="progressbar" aria-valuenow="87" aria-valuemin="0"
    aria-valuemax="100">
    <div class="progress-bar__fill" style="width: 87%"></div>
</div>
```

### What to ADD in its place
```jsp
<p class="stat-card__value">${reputationScore} <span class="stat-card__suffix">/ 100</span></p>
<div class="progress-bar" role="progressbar"
     aria-valuenow="${reputationScore}" aria-valuemin="0" aria-valuemax="100">
    <div class="progress-bar__fill" style="width: ${reputationScore}%"></div>
</div>
```

---

### Approved Uploads card — What to DELETE (lines 136–137)
```html
<p class="stat-card__value">14</p>
<p class="stat-card__hint stat-card__hint--green">+2 from last month</p>
```

### What to ADD in its place
```jsp
<p class="stat-card__value">${approvedUploads}</p>
<p class="stat-card__hint stat-card__hint--green">+${newUploadsThisMonth} from last month</p>
```

---

### Pending Review card — What to DELETE (lines 142–143)
```html
<p class="stat-card__value">3</p>
<p class="stat-card__hint">Expected by Friday</p>
```

### What to ADD in its place
```jsp
<p class="stat-card__value">${pendingUploads}</p>
<p class="stat-card__hint">Expected by Friday</p>
```

---

### Saved Resources card — What to DELETE (lines 148–149)
```html
<p class="stat-card__value">22</p>
<p class="stat-card__hint">Active libraries</p>
```

### What to ADD in its place
```jsp
<p class="stat-card__value">${savedResources}</p>
<p class="stat-card__hint">Active libraries</p>
```

---

## COMMIT 7 — Replace hard-coded submissions table with JSTL forEach loop

**Commit message:** `feat: render Recent Submissions table dynamically using JSTL forEach`

The table body currently has 4 hard-coded `<tr>` rows. Replace the **entire `<tbody>`
block** with a JSTL loop.

### What to DELETE (lines 174–199)
```html
<tbody>
    <tr>
        <td>Advanced Microeconomics Notes</td>
        <td>PDF</td>
        <td>Apr 2, 2026</td>
        <td><span class="badge badge--approved">Approved</span></td>
    </tr>
    <tr>
        <td>Linear Algebra Problem Set 4</td>
        <td>Solution Set</td>
        <td>Apr 5, 2026</td>
        <td><span class="badge badge--pending">Pending</span></td>
    </tr>
    <tr>
        <td>Data Structures Cheat Sheet</td>
        <td>PDF</td>
        <td>Apr 8, 2026</td>
        <td><span class="badge badge--approved">Approved</span></td>
    </tr>
    <tr>
        <td>Organic Chemistry Lab Guide</td>
        <td>Document</td>
        <td>Apr 10, 2026</td>
        <td><span class="badge badge--pending">Pending</span></td>
    </tr>
</tbody>
```

### What to ADD in its place
```jsp
<tbody>
    <c:forEach var="resource" items="${recentSubmissions}">
        <tr>
            <td>${resource.title}</td>
            <td>${resource.resourceType}</td>
            <td>${resource.uploadedAt}</td>
            <td>
                <c:choose>
                    <c:when test="${resource.status == 'approved'}">
                        <span class="badge badge--approved">Approved</span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge badge--pending">Pending</span>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty recentSubmissions}">
        <tr>
            <td colspan="4" style="text-align:center; color: var(--text-muted);">
                No submissions yet.
            </td>
        </tr>
    </c:if>
</tbody>
```

### Why
`recentSubmissions` will be a `List<Resource>` set by the servlet.
`<c:forEach>` iterates the list. `<c:choose>` picks the right badge CSS class.
The empty-state row shows a friendly message when the list is null or empty.

> **Note:** `resource.title`, `resource.resourceType`, `resource.status`, and
> `resource.uploadedAt` must match the getter names in `Resource.java`
> (`getTitle()`, `getResourceType()`, `getStatus()`, `getUploadedAt()`).
> Verify these against `src/main/java/com/ScholarShare/entity/Resource.java`.

---

## COMMIT 8 — Replace hard-coded reputation numbers with dynamic values

**Commit message:** `feat: bind reputation donut and metrics to dynamic attributes`

### Donut chart — What to DELETE (lines 207–212)
```html
<div class="donut" role="img" aria-label="87 percent top tier">
    <div class="donut__hole">
        <span class="donut__percent">87%</span>
        <span class="donut__label">Top Tier</span>
    </div>
</div>
```

### What to ADD in its place
```jsp
<div class="donut" role="img" aria-label="${reputationScore} percent top tier">
    <div class="donut__hole">
        <span class="donut__percent">${reputationScore}%</span>
        <span class="donut__label">Top Tier</span>
    </div>
</div>
```

---

### Accuracy Rate metric — What to DELETE (lines 218–224)
```html
<div class="metric__row">
    <span>Accuracy Rate</span>
    <span class="metric__value">98%</span>
</div>
<div class="progress-bar progress-bar--thin">
    <div class="progress-bar__fill" style="width: 98%"></div>
</div>
```

### What to ADD in its place
```jsp
<div class="metric__row">
    <span>Accuracy Rate</span>
    <span class="metric__value">${accuracyRate}%</span>
</div>
<div class="progress-bar progress-bar--thin">
    <div class="progress-bar__fill" style="width: ${accuracyRate}%"></div>
</div>
```

---

### Peer Helpfulness metric — What to DELETE (lines 226–234)
```html
<div class="metric__row">
    <span>Peer Helpfulness</span>
    <span class="metric__value">72%</span>
</div>
<div class="progress-bar progress-bar--thin">
    <div class="progress-bar__fill progress-bar__fill--teal" style="width: 72%"></div>
</div>
```

### What to ADD in its place
```jsp
<div class="metric__row">
    <span>Peer Helpfulness</span>
    <span class="metric__value">${peerHelpfulness}%</span>
</div>
<div class="progress-bar progress-bar--thin">
    <div class="progress-bar__fill progress-bar__fill--teal" style="width: ${peerHelpfulness}%"></div>
</div>
```

---

## COMMIT 9 — Replace hard-coded resource cards with JSTL forEach loop

**Commit message:** `feat: render Recently Added Resources cards dynamically with JSTL`

### What to DELETE (lines 250–266) — the entire `recent__cards` div contents
```html
<div class="recent__cards">
    <a href="#" class="resource-card">
        <span class="resource-card__type">PDF</span>
        <h3 class="resource-card__name">Macroeconomics Midterm Review</h3>
        <p class="resource-card__meta">Economics · Added 2 days ago</p>
    </a>
    <a href="#" class="resource-card">
        <span class="resource-card__type">Slides</span>
        <h3 class="resource-card__name">HCI Design Principles</h3>
        <p class="resource-card__meta">CS · Added 4 days ago</p>
    </a>
    <a href="#" class="resource-card">
        <span class="resource-card__type">Practice</span>
        <h3 class="resource-card__name">Probability Worksheet Pack</h3>
        <p class="resource-card__meta">Math · Added 1 week ago</p>
    </a>
</div>
```

### What to ADD in its place
```jsp
<div class="recent__cards">
    <c:forEach var="res" items="${recentResources}">
        <a href="${pageContext.request.contextPath}/resource/${res.resourceId}" class="resource-card">
            <span class="resource-card__type">${res.resourceType}</span>
            <h3 class="resource-card__name">${res.title}</h3>
            <p class="resource-card__meta">${res.subjectName} · Added recently</p>
        </a>
    </c:forEach>
    <c:if test="${empty recentResources}">
        <p style="color: var(--text-muted);">No recent resources found.</p>
    </c:if>
</div>
```

---

## COMMIT 10 — Fix the Sign Out link to call the logout servlet

**Commit message:** `feat: wire Sign Out link to /logout servlet`

### What to DELETE (line 63)
```html
<a href="#" class="sidebar__footer-link">Sign Out</a>
```

### What to ADD in its place
```jsp
<a href="${pageContext.request.contextPath}/logout" class="sidebar__footer-link">Sign Out</a>
```

### Why
The `href="#"` was a placeholder. Pointing it to `/logout` will invoke the logout
servlet (which you will create separately) that invalidates the session.

---

## COMMIT 11 — Create the DashboardServlet to forward data to the JSP

**Commit message:** `feat: add DashboardServlet to populate model attributes for student_dashboard.jsp`

Create a new file:
```
src/main/java/com/ScholarShare/controller/DashboardServlet.java
```

### Full code to write into DashboardServlet.java

```java
package com.ScholarShare.controller;

import com.ScholarShare.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // Redirect to login if no session or no user
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        // TODO: Replace hard-coded values below with real DB queries
        //       using a service/DAO class (e.g., ResourceService, UserStatsService)
        req.setAttribute("reputationScore",       87);
        req.setAttribute("approvedUploads",        14);
        req.setAttribute("newUploadsThisMonth",     2);
        req.setAttribute("pendingUploads",           3);
        req.setAttribute("savedResources",          22);
        req.setAttribute("accuracyRate",            98);
        req.setAttribute("peerHelpfulness",         72);

        // TODO: Replace nulls with real list from DB
        req.setAttribute("recentSubmissions",  null);   // List<Resource>
        req.setAttribute("recentResources",    null);   // List<Resource>

        req.getRequestDispatcher("/WEB-INF/views/student_dashboard.jsp")
           .forward(req, resp);
    }
}
```

### Why
The servlet acts as the controller. It checks authentication, sets all the model
attributes the JSP needs, then forwards. Later you replace the hard-coded numbers with
real DAO/service calls.

---

## COMMIT 12 — (Optional cleanup) Delete the old HTML file

**Commit message:** `chore: remove student_dashboard.html now replaced by JSP`

Once everything is working end-to-end, delete the original HTML file:
```
src/main/webapp/WEB-INF/views/student_dashboard.html
```

---

## Summary of all model attributes the JSP expects

| EL Expression | Type | Set by |
|---|---|---|
| `${sessionScope.user.fullName}` | `String` | `LoginServlet` |
| `${sessionScope.user.role}` | `String` | `LoginServlet` |
| `${reputationScore}` | `int` | `DashboardServlet` |
| `${approvedUploads}` | `int` | `DashboardServlet` |
| `${newUploadsThisMonth}` | `int` | `DashboardServlet` |
| `${pendingUploads}` | `int` | `DashboardServlet` |
| `${savedResources}` | `int` | `DashboardServlet` |
| `${accuracyRate}` | `int` | `DashboardServlet` |
| `${peerHelpfulness}` | `int` | `DashboardServlet` |
| `${recentSubmissions}` | `List<Resource>` | `DashboardServlet` |
| `${recentResources}` | `List<Resource>` | `DashboardServlet` |

---

## Quick checklist before testing

- [ ] `student_dashboard.jsp` exists in `WEB-INF/views/`
- [ ] Top two lines are the JSP directives (page + taglib)
- [ ] All `../../css/` and `../../js/` paths replaced with `${pageContext.request.contextPath}/...`
- [ ] Sidebar user card shows `${sessionScope.user.fullName}`
- [ ] Hero title shows `${sessionScope.user.fullName}`
- [ ] Stat card values use EL expressions (not numbers)
- [ ] Submissions `<tbody>` uses `<c:forEach>`
- [ ] Recent Resources `.recent__cards` uses `<c:forEach>`
- [ ] Sign Out links to `/logout`
- [ ] `DashboardServlet.java` created and mapped to `/dashboard`
- [ ] Log in and visit `http://localhost:8080/<contextPath>/dashboard` — page loads with your name
