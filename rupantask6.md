# Rupan — Task 6 & 7 Implementation Guide

**Project:** ScholarShare  
**Package base:** `com.ScholarShare`  
**Views path:** `src/main/webapp/WEB-INF/views/`  
**Java path:** `src/main/java/com/ScholarShare/`

---

## TASK 6 — Resource Discovery (Browse & Search)

---

### STEP 1 — Create the `ResourceDao` interface

**File:** `src/main/java/com/ScholarShare/dao/ResourceDao.java`

```java
package com.ScholarShare.dao;

import com.ScholarShare.entity.Resource;
import java.util.List;

public interface ResourceDao {
    List<Resource> getAllApproved();
    List<Resource> getByFaculty(int facultyId);
    List<Resource> getBySubject(int subjectId);
    List<Resource> getByTopic(int topicId);
    List<Resource> search(String keyword);
    Resource getById(int resourceId);
}
```

> **Commit 1:** `feat(dao): add ResourceDao interface with browse/search/filter methods`

---

### STEP 2 — Create `ResourceDaoImpl.java`

**File:** `src/main/java/com/ScholarShare/dao/daoImpl/ResourceDaoImpl.java`

- Package: `com.ScholarShare.dao.daoImpl`
- Implements: `ResourceDao`
- Import: `com.ScholarShare.util.DatabaseConnection`, `com.ScholarShare.entity.Resource`, `java.sql.*`, `java.util.*`

**Method: `getAllApproved()`**
```sql
SELECT * FROM resources WHERE status = 'approved' ORDER BY upload_date DESC
```
Map each row: `resource_id`, `user_id`, `topic_id`, `title`, `description`, `file_path`, `resource_type`, `status`, `self_declaration`, `upload_date`.  
Use `while(rs.next())` and `list.add(resource)`. Return list.

**Method: `getByFaculty(int facultyId)`**
```sql
SELECT r.* FROM resources r
JOIN topics t ON r.topic_id = t.topic_id
JOIN subjects s ON t.subject_id = s.subject_id
WHERE s.faculty_id = ? AND r.status = 'approved'
ORDER BY r.upload_date DESC
```

**Method: `getBySubject(int subjectId)`**
```sql
SELECT r.* FROM resources r
JOIN topics t ON r.topic_id = t.topic_id
WHERE t.subject_id = ? AND r.status = 'approved'
ORDER BY r.upload_date DESC
```

**Method: `getByTopic(int topicId)`**
```sql
SELECT * FROM resources WHERE topic_id = ? AND status = 'approved'
ORDER BY upload_date DESC
```

**Method: `search(String keyword)`**
```sql
SELECT * FROM resources
WHERE status = 'approved'
AND (title LIKE ? OR description LIKE ?)
ORDER BY upload_date DESC
```
Set both `?` to `"%" + keyword + "%"`.

**Method: `getById(int resourceId)`**
```sql
SELECT * FROM resources WHERE resource_id = ?
```
Return a single `Resource` or `null`.

Use the same try/catch/finally + `DatabaseConnection.closeConnection(connection)` pattern as `UserDaoImpl`.

> **Commit 2:** `feat(dao): implement ResourceDaoImpl with filter by faculty/subject/topic and keyword search`

---

### STEP 3 — Create the `FacultyDao` interface

**File:** `src/main/java/com/ScholarShare/dao/FacultyDao.java`

```java
package com.ScholarShare.dao;

import com.ScholarShare.entity.Faculty;
import java.util.List;

public interface FacultyDao {
    List<Faculty> getAllFaculties();
    Faculty getById(int facultyId);
}
```

> **Commit 3:** `feat(dao): add FacultyDao interface`

---

### STEP 4 — Create `FacultyDaoImpl.java`

**File:** `src/main/java/com/ScholarShare/dao/daoImpl/FacultyDaoImpl.java`

- Implements `FacultyDao`

**Method: `getAllFaculties()`**
```sql
SELECT * FROM faculties ORDER BY faculty_name ASC
```
Map: `faculty_id`, `faculty_name`, `created_at`. Return list.

**Method: `getById(int facultyId)`**
```sql
SELECT * FROM faculties WHERE faculty_id = ?
```
Return single `Faculty` or `null`.

> **Commit 4:** `feat(dao): implement FacultyDaoImpl`

---

### STEP 5 — Create the `SubjectDao` interface

**File:** `src/main/java/com/ScholarShare/dao/SubjectDao.java`

```java
package com.ScholarShare.dao;

import com.ScholarShare.entity.Subject;
import java.util.List;

public interface SubjectDao {
    List<Subject> getAllSubjects();
    List<Subject> getByFaculty(int facultyId);
    Subject getById(int subjectId);
}
```

> **Commit 5:** `feat(dao): add SubjectDao interface`

---

### STEP 6 — Create `SubjectDaoImpl.java`

**File:** `src/main/java/com/ScholarShare/dao/daoImpl/SubjectDaoImpl.java`

- Implements `SubjectDao`

**Method: `getAllSubjects()`**
```sql
SELECT s.*, f.faculty_name FROM subjects s
JOIN faculties f ON s.faculty_id = f.faculty_id
ORDER BY s.subject_name ASC
```
Map: `subject_id`, `faculty_id`, `subject_name`, `faculty_name`, `created_at`.

**Method: `getByFaculty(int facultyId)`**
```sql
SELECT s.*, f.faculty_name FROM subjects s
JOIN faculties f ON s.faculty_id = f.faculty_id
WHERE s.faculty_id = ?
ORDER BY s.subject_name ASC
```

**Method: `getById(int subjectId)`**
```sql
SELECT s.*, f.faculty_name FROM subjects s
JOIN faculties f ON s.faculty_id = f.faculty_id
WHERE s.subject_id = ?
```

> **Commit 6:** `feat(dao): implement SubjectDaoImpl`

---

### STEP 7 — Create the `TopicDao` interface

**File:** `src/main/java/com/ScholarShare/dao/TopicDao.java`

```java
package com.ScholarShare.dao;

import com.ScholarShare.entity.Topic;
import java.util.List;

public interface TopicDao {
    List<Topic> getBySubject(int subjectId);
    Topic getById(int topicId);
}
```

> **Commit 7:** `feat(dao): add TopicDao interface`

---

### STEP 8 — Create `TopicDaoImpl.java`

**File:** `src/main/java/com/ScholarShare/dao/daoImpl/TopicDaoImpl.java`

- Implements `TopicDao`

**Method: `getBySubject(int subjectId)`**
```sql
SELECT t.*, s.subject_name, f.faculty_name
FROM topics t
JOIN subjects s ON t.subject_id = s.subject_id
JOIN faculties f ON s.faculty_id = f.faculty_id
WHERE t.subject_id = ?
ORDER BY t.topic_name ASC
```
Map: `topic_id`, `topic_name`, `subject_id`, `subject_name`, `faculty_name`, `created_at`.

**Method: `getById(int topicId)`**
```sql
SELECT t.*, s.subject_name, f.faculty_name
FROM topics t
JOIN subjects s ON t.subject_id = s.subject_id
JOIN faculties f ON s.faculty_id = f.faculty_id
WHERE t.topic_id = ?
```

> **Commit 8:** `feat(dao): implement TopicDaoImpl`

---

### STEP 9 — Create `BrowseServlet.java`

**File:** `src/main/java/com/ScholarShare/controller/BrowseServlet.java`

- Annotation: `@WebServlet("/browse")`
- Extends: `HttpServlet`

**`doGet` logic:**
1. Read query params: `facultyId`, `subjectId`, `topicId`, `q` (keyword)  — use `request.getParameter(...)`, parse to int with `Integer.parseInt()`, wrap in try/catch defaulting to `0`.
2. Instantiate DAOs: `new FacultyDaoImpl()`, `new SubjectDaoImpl()`, `new TopicDaoImpl()`, `new ResourceDaoImpl()`.
3. Always load all faculties: `List<Faculty> faculties = facultyDao.getAllFaculties()`.
4. Decision tree:
   - If `topicId > 0` → `resources = resourceDao.getByTopic(topicId)`; load subjects for sidebar using `subjectId`; load topics using `subjectId`.
   - Else if `subjectId > 0` → `resources = resourceDao.getBySubject(subjectId)`; load topics using `subjectId`.
   - Else if `facultyId > 0` → `resources = resourceDao.getByFaculty(facultyId)`; load subjects for that faculty.
   - Else if `q != null && !q.trim().isEmpty()` → `resources = resourceDao.search(q.trim())`.
   - Else → `resources = resourceDao.getAllApproved()`.
5. Set attributes on request:
   - `request.setAttribute("faculties", faculties)`
   - `request.setAttribute("resources", resources)`
   - `request.setAttribute("selectedFacultyId", facultyId)`
   - `request.setAttribute("selectedSubjectId", subjectId)`
   - `request.setAttribute("selectedTopicId", topicId)`
   - `request.setAttribute("keyword", q)`
   - `request.setAttribute("subjects", subjects)` (if loaded)
   - `request.setAttribute("topics", topics)` (if loaded)
6. Forward to `"/WEB-INF/views/search-results.jsp"`.

> **Commit 9:** `feat(controller): add BrowseServlet handling faculty/subject/topic filter and keyword search`

---

### STEP 10 — Create `search-results.jsp`

**File:** `src/main/webapp/WEB-INF/views/search-results.jsp`

Top of file:
```jsp
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
```

**Page structure:**
- `<title>Browse Resources — ScholarShare</title>`
- Link `student_dashboard.css` (reuse existing stylesheet)
- 2-column layout: left filter sidebar + right results area

**Left sidebar — Filter Panel:**
- Heading: "Browse by Faculty"
- `<c:forEach var="f" items="${faculties}">` → render a link:  
  `<a href="${pageContext.request.contextPath}/browse?facultyId=${f.facultyId}">${f.facultyName}</a>`  
  Add `class="active"` when `${f.facultyId == selectedFacultyId}`.

- If `selectedFacultyId > 0` and `subjects` is not empty, render a "Subjects" sub-list:  
  `<c:forEach var="s" items="${subjects}">` →  
  `<a href="...?facultyId=${selectedFacultyId}&subjectId=${s.subjectId}">${s.subjectName}</a>`

- If `selectedSubjectId > 0` and `topics` is not empty, render a "Topics" sub-list similarly.

- Add a keyword search `<form action="${pageContext.request.contextPath}/browse" method="get">` with `<input type="search" name="q" value="${keyword}">` and a Submit button.

**Right area — Results:**
- Heading: "Resources" with count badge `(${resources.size()})`
- `<c:choose>`:
  - `<c:when test="${empty resources}">` → show empty state message "No resources found."
  - `<c:otherwise>` → `<c:forEach var="r" items="${resources}">` → render a card:
    ```
    <a href="${pageContext.request.contextPath}/resource?id=${r.resourceId}" class="resource-card">
        <span class="resource-card__type">${r.resourceType}</span>
        <h3 class="resource-card__name">${r.title}</h3>
        <p class="resource-card__meta">${r.description}</p>
    </a>
    ```

Add sidebar nav links updated: "Browse" link points to `/browse`, "Dashboard" to `/student-dashboard`.

> **Commit 10:** `feat(view): create search-results.jsp with faculty/subject/topic filter sidebar and resource cards`

---

### STEP 11 — Create `ResourceDetailServlet.java`

**File:** `src/main/java/com/ScholarShare/controller/ResourceDetailServlet.java`

- Annotation: `@WebServlet("/resource")`
- Extends: `HttpServlet`

**`doGet` logic:**
1. `String idParam = request.getParameter("id")`
2. If `idParam == null` → `response.sendRedirect(request.getContextPath() + "/browse")` and return.
3. Parse: `int resourceId = Integer.parseInt(idParam)`
4. `Resource resource = new ResourceDaoImpl().getById(resourceId)`
5. If `resource == null` → redirect to `/browse`.
6. `request.setAttribute("resource", resource)`
7. Forward to `"/WEB-INF/views/resource-detail.jsp"`

> **Commit 11:** `feat(controller): add ResourceDetailServlet to load single resource by id`

---

### STEP 12 — Create `resource-detail.jsp`

**File:** `src/main/webapp/WEB-INF/views/resource-detail.jsp`

Top:
```jsp
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
```

**Page structure:**
- `<title>${resource.title} — ScholarShare</title>`
- Link `student_dashboard.css`
- Breadcrumb: `Browse › ${resource.title}`
- Detail card containing:
  - `<h1>${resource.title}</h1>`
  - `<p>${resource.description}</p>`
  - Type badge: `<span class="badge">${resource.resourceType}</span>`
  - Status badge: `<span class="badge badge--${resource.status}">${resource.status}</span>`
  - Upload date: `${resource.uploadDate}`
  - Download link: `<a href="${pageContext.request.contextPath}/${resource.filePath}" download>Download</a>`
- Placeholder section `<!-- RATING SECTION — Hemanta will implement here -->`
- Placeholder section `<!-- FLAG SECTION — Sajan will implement here -->`
- "Add to Collection" button (placeholder for Task 7 — wire up in Step 18):
  ```html
  <button id="btn-add-collection" type="button">+ Add to Collection</button>
  ```
- Back link: `<a href="${pageContext.request.contextPath}/browse">← Back to Browse</a>`

> **Commit 12:** `feat(view): create resource-detail.jsp as base detail page with placeholders for rating and flagging`

---

### STEP 13 — Update sidebar nav links in `student_dashboard.jsp`

In `student_dashboard.jsp`, update the "Browse" `<a href="#">` to:
```html
<a href="${pageContext.request.contextPath}/browse" class="sidebar__nav-link">
```

> **Commit 13:** `fix(view): wire Browse nav link in student_dashboard.jsp to /browse`

---

## TASK 7 — Study Collections Integration

---

### STEP 14 — Create the `CollectionDao` interface

**File:** `src/main/java/com/ScholarShare/dao/CollectionDao.java`

```java
package com.ScholarShare.dao;

import com.ScholarShare.entity.Collection;
import java.util.List;

public interface CollectionDao {
    List<Collection> getByUser(int userId);
    boolean createCollection(int userId, String name);
    boolean addResource(int collectionId, int resourceId);
    boolean removeResource(int collectionId, int resourceId);
    boolean deleteCollection(int collectionId);
    Collection getById(int collectionId);
}
```

> **Commit 14:** `feat(dao): add CollectionDao interface`

---

### STEP 15 — Create `CollectionDaoImpl.java`

**File:** `src/main/java/com/ScholarShare/dao/daoImpl/CollectionDaoImpl.java`

- Implements `CollectionDao`

**Method: `getByUser(int userId)`**
```sql
SELECT * FROM collections WHERE user_id = ? ORDER BY created_at DESC
```
Map `collection_id`, `user_id`, `collection_name`, `created_at`. For each collection, also load its resources:
```sql
SELECT r.* FROM resources r
JOIN collection_items ci ON r.resource_id = ci.resource_id
WHERE ci.collection_id = ?
```
Call `collection.setResources(resources)`. Return list.

**Method: `createCollection(int userId, String name)`**
```sql
INSERT INTO collections(user_id, collection_name) VALUES(?, ?)
```
Return `statement.executeUpdate() > 0`.

**Method: `addResource(int collectionId, int resourceId)`**
```sql
INSERT INTO collection_items(collection_id, resource_id) VALUES(?, ?)
```
Return `statement.executeUpdate() > 0`.

**Method: `removeResource(int collectionId, int resourceId)`**
```sql
DELETE FROM collection_items WHERE collection_id = ? AND resource_id = ?
```
Return `statement.executeUpdate() > 0`.

**Method: `deleteCollection(int collectionId)`**
```sql
DELETE FROM collections WHERE collection_id = ?
```
Return `statement.executeUpdate() > 0`.

**Method: `getById(int collectionId)`**
```sql
SELECT * FROM collections WHERE collection_id = ?
```
Return single `Collection` or `null`.

> **Commit 15:** `feat(dao): implement CollectionDaoImpl with CRUD and resource add/remove`

---

### STEP 16 — Create `CollectionServlet.java`

**File:** `src/main/java/com/ScholarShare/controller/CollectionServlet.java`

- Annotation: `@WebServlet("/collections")`
- Extends: `HttpServlet`

**`doGet` — View collections page:**
1. Get user from session: `User user = (User) session.getAttribute("user")`
2. If `user == null` → redirect to `/login`.
3. `List<Collection> collections = new CollectionDaoImpl().getByUser(user.getUserId())`
4. `request.setAttribute("collections", collections)`
5. Forward to `"/WEB-INF/views/collections.jsp"`

**`doPost` — Handle actions via `action` parameter:**

Read: `String action = request.getParameter("action")`

- `"create"`:
  - `String name = request.getParameter("collectionName")`
  - Validate not blank, then `collectionDao.createCollection(user.getUserId(), name)`
  - Redirect to `/collections`

- `"add"`:
  - Parse `collectionId = Integer.parseInt(request.getParameter("collectionId"))`
  - Parse `resourceId = Integer.parseInt(request.getParameter("resourceId"))`
  - `collectionDao.addResource(collectionId, resourceId)`
  - Redirect to `/resource?id=` + resourceId

- `"remove"`:
  - Parse `collectionId`, `resourceId`
  - `collectionDao.removeResource(collectionId, resourceId)`
  - Redirect to `/collections`

- `"delete"`:
  - Parse `collectionId`
  - `collectionDao.deleteCollection(collectionId)`
  - Redirect to `/collections`

> **Commit 16:** `feat(controller): add CollectionServlet handling view/create/add/remove/delete`

---

### STEP 17 — Create `collections.jsp`

**File:** `src/main/webapp/WEB-INF/views/collections.jsp`

Top:
```jsp
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
```

**Page structure:**
- `<title>My Collections — ScholarShare</title>`
- Link `student_dashboard.css`
- Include the same sidebar markup as `student_dashboard.jsp` (copy sidebar `<aside>` block, update active nav link to "Collections")
- Update the "Collections" sidebar link:  
  `<a href="${pageContext.request.contextPath}/collections" class="sidebar__nav-link">`

**Main content:**

- Page title: `<h1>My Collections</h1>`

- **Create new collection form:**
  ```html
  <form action="${pageContext.request.contextPath}/collections" method="post">
      <input type="hidden" name="action" value="create">
      <input type="text" name="collectionName" placeholder="New collection name..." required>
      <button type="submit">Create</button>
  </form>
  ```

- **Collection list:**
  ```jsp
  <c:choose>
      <c:when test="${empty collections}">
          <p>You have no collections yet.</p>
      </c:when>
      <c:otherwise>
          <c:forEach var="col" items="${collections}">
              <div class="panel">
                  <div class="panel__head">
                      <h2>${col.collectionName}</h2>
                      <form action="${pageContext.request.contextPath}/collections" method="post" style="display:inline">
                          <input type="hidden" name="action" value="delete">
                          <input type="hidden" name="collectionId" value="${col.collectionId}">
                          <button type="submit">Delete</button>
                      </form>
                  </div>
                  <c:choose>
                      <c:when test="${empty col.resources}">
                          <p>No resources saved yet.</p>
                      </c:when>
                      <c:otherwise>
                          <c:forEach var="r" items="${col.resources}">
                              <div class="resource-card">
                                  <a href="${pageContext.request.contextPath}/resource?id=${r.resourceId}">${r.title}</a>
                                  <form action="${pageContext.request.contextPath}/collections" method="post" style="display:inline">
                                      <input type="hidden" name="action" value="remove">
                                      <input type="hidden" name="collectionId" value="${col.collectionId}">
                                      <input type="hidden" name="resourceId" value="${r.resourceId}">
                                      <button type="submit">Remove</button>
                                  </form>
                              </div>
                          </c:forEach>
                      </c:otherwise>
                  </c:choose>
              </div>
          </c:forEach>
      </c:otherwise>
  </c:choose>
  ```

> **Commit 17:** `feat(view): create collections.jsp with collection list, create form, and remove resource buttons`

---

### STEP 18 — Add "Add to Collection" dropdown to `resource-detail.jsp`

In `resource-detail.jsp`, replace the placeholder `<button id="btn-add-collection">` with a full form:

1. In `ResourceDetailServlet.doGet`, before forwarding, also load the user's collections:
   ```java
   User user = (User) session.getAttribute("user");
   if (user != null) {
       List<Collection> collections = new CollectionDaoImpl().getByUser(user.getUserId());
       request.setAttribute("collections", collections);
   }
   ```

2. In `resource-detail.jsp`, add after the download link:
   ```jsp
   <c:if test="${not empty collections}">
       <form action="${pageContext.request.contextPath}/collections" method="post">
           <input type="hidden" name="action" value="add">
           <input type="hidden" name="resourceId" value="${resource.resourceId}">
           <label for="collectionSelect">Add to Collection:</label>
           <select id="collectionSelect" name="collectionId">
               <c:forEach var="col" items="${collections}">
                   <option value="${col.collectionId}">${col.collectionName}</option>
               </c:forEach>
           </select>
           <button type="submit">Save</button>
       </form>
   </c:if>
   <c:if test="${empty collections}">
       <p><a href="${pageContext.request.contextPath}/collections">Create a collection first</a></p>
   </c:if>
   ```

> **Commit 18:** `feat(view): add 'Add to Collection' dropdown form to resource-detail.jsp`

---

### STEP 19 — Update sidebar nav links in `student_dashboard.jsp`

Update the "Collections" / "Resources" placeholder `<a href="#">` links to point to real URLs:
- "Browse" → `${pageContext.request.contextPath}/browse`
- "Collections" (if listed) → `${pageContext.request.contextPath}/collections`

Also update `search-results.jsp` and `collections.jsp` sidebar nav links to be consistent.

> **Commit 19:** `fix(view): update all sidebar nav links across dashboard, search-results, collections pages`

---

### STEP 20 — Smoke test & final cleanup

1. Deploy: `mvn cargo:run`
2. Go to `http://localhost:8080/ScholarShare/browse` — verify faculty list loads.
3. Click a faculty → subjects appear in sidebar.
4. Click a subject → topics appear.
5. Click a topic → resources with status `approved` show only.
6. Use keyword search → results filter correctly.
7. Click a resource card → `resource-detail.jsp` loads with title, description, type, download link.
8. Go to `http://localhost:8080/ScholarShare/collections` — verify empty state shows.
9. Create a collection → verify it appears.
10. On a resource detail page, select collection from dropdown → save → verify it appears in the collection.
11. Remove a resource from a collection → verify it disappears.
12. Delete a collection → verify it is gone.
13. Fix any null pointer exceptions (e.g. session user null checks).
14. Fix any SQL errors in console.

> **Commit 20:** `fix: smoke test fixes — null checks, SQL corrections, nav link updates`

---

## File Summary

| # | File | Type | Task |
|---|------|------|------|
| 1 | `dao/ResourceDao.java` | Interface | 6 |
| 2 | `dao/daoImpl/ResourceDaoImpl.java` | Implementation | 6 |
| 3 | `dao/FacultyDao.java` | Interface | 6 |
| 4 | `dao/daoImpl/FacultyDaoImpl.java` | Implementation | 6 |
| 5 | `dao/SubjectDao.java` | Interface | 6 |
| 6 | `dao/daoImpl/SubjectDaoImpl.java` | Implementation | 6 |
| 7 | `dao/TopicDao.java` | Interface | 6 |
| 8 | `dao/daoImpl/TopicDaoImpl.java` | Implementation | 6 |
| 9 | `controller/BrowseServlet.java` | Servlet | 6 |
| 10 | `views/search-results.jsp` | View | 6 |
| 11 | `controller/ResourceDetailServlet.java` | Servlet | 6 |
| 12 | `views/resource-detail.jsp` | View | 6 |
| 13 | `views/student_dashboard.jsp` | Modified | 6 |
| 14 | `dao/CollectionDao.java` | Interface | 7 |
| 15 | `dao/daoImpl/CollectionDaoImpl.java` | Implementation | 7 |
| 16 | `controller/CollectionServlet.java` | Servlet | 7 |
| 17 | `views/collections.jsp` | View | 7 |
| 18 | `views/resource-detail.jsp` | Modified | 7 |
| 19 | (all views) | Modified | 7 |

## Commit Summary

| # | Commit Message |
|---|----------------|
| 1 | `feat(dao): add ResourceDao interface` |
| 2 | `feat(dao): implement ResourceDaoImpl` |
| 3 | `feat(dao): add FacultyDao interface` |
| 4 | `feat(dao): implement FacultyDaoImpl` |
| 5 | `feat(dao): add SubjectDao interface` |
| 6 | `feat(dao): implement SubjectDaoImpl` |
| 7 | `feat(dao): add TopicDao interface` |
| 8 | `feat(dao): implement TopicDaoImpl` |
| 9 | `feat(controller): add BrowseServlet` |
| 10 | `feat(view): create search-results.jsp` |
| 11 | `feat(controller): add ResourceDetailServlet` |
| 12 | `feat(view): create resource-detail.jsp` |
| 13 | `fix(view): wire Browse link in student_dashboard.jsp` |
| 14 | `feat(dao): add CollectionDao interface` |
| 15 | `feat(dao): implement CollectionDaoImpl` |
| 16 | `feat(controller): add CollectionServlet` |
| 17 | `feat(view): create collections.jsp` |
| 18 | `feat(view): add Add to Collection form in resource-detail.jsp` |
| 19 | `fix(view): update all sidebar nav links` |
| 20 | `fix: smoke test fixes` |
