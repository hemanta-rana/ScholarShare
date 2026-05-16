# ScholarShare — Implementation TODO (Task 2 & Task 3)

> **Generated from actual codebase analysis** (May 16, 2026)  
> **Stack:** Jakarta Servlet 6.1, JSP/JSTL 3.0, JDBC, MySQL (`sql/schema.sql`), Tomcat 10 via Cargo  
> **Context path (Cargo):** `/ScholarShare`

---

## Project Structure Findings (Reference)

| Layer | Location | Notes |
|-------|----------|-------|
| Controllers | `src/main/java/com/ScholarShare/controller/` | `@WebServlet` annotation routing; no `web.xml` servlet mappings |
| DAO interfaces | `src/main/java/com/ScholarShare/dao/` | `ResourceDao`, `CategoryDao`, `StudentDao`, etc. |
| DAO implementations | `src/main/java/com/ScholarShare/dao/daoImpl/` | Mixed naming: `CategoryDaoImpl`, `AdminDaoImp` |
| Entities | `src/main/java/com/ScholarShare/entity/` | `Resource`, `Flag` **already exist** |
| Services | `src/main/java/com/ScholarShare/service/` | Only `AuthService`, `AdminService` — **no ResourceService** |
| JSP views | `src/main/webapp/WEB-INF/views/` | Public-style pages use `headerFooter.css`; admin uses `template/admin-*.jsp` |
| Auth | `AuthenticationFilter` `@WebFilter("/*")` | Session key `"user"` → `User` via `SessionUtil` |
| DB util | `DatabaseConnection` | Hard-coded `jdbc:mysql://localhost:3306/scholarshare` |

### Schema ↔ Entity field mapping (do NOT use task-spec names in SQL)

| Task / generic name | Actual DB column | `Resource` Java property |
|---------------------|------------------|--------------------------|
| `student_id` | `user_id` | `userId` |
| `academic_integrity_confirmed` | `self_declaration` | `selfDeclaration` |
| `upload_timestamp` | `upload_date` | `uploadDate` (or DB `DEFAULT CURRENT_TIMESTAMP`) |
| `topic_category` | `topic_id` | `topicId` (FK → `topics`) |
| Flag `student_id` | `flagged_by` | `Flag.flaggedBy` |

### What does NOT exist yet (must be built)

- `ResourceUploadServlet`, `upload-resource.jsp`, `upload-resource.css` (optional)
- `FlagDao`, `FlagDaoImpl`, `FlagServlet`
- `resource-detail.jsp`, `ResourceDetailServlet` (required for flag redirect target)
- Student “my resources” list page + servlet (redirect target after upload)
- Any `MultipartConfig` / file upload code
- `getResourceById` implementation (needed by flag + detail flows)

### Existing stubs to align with

- `ResourceDaoImpl.uploadResource` / `deleteResource` → return `false`
- `StudentDaoImpl.flagResourceForPlagiarism` → return `false` (prefer new `FlagDao`; leave stub or delegate)
- `ResourceDaoImpl.getResourceById` → returns `null`

---

## Execution Order Overview

| # | Task | Depends on |
|---|------|------------|
| 0 | Prerequisites (DAO reads, student list, detail page) | — |
| 1 | Complete `ResourceDaoImpl.uploadResource` | — |
| 2 | Complete `ResourceDaoImpl.deleteResource` | `getResourceById` |
| 3 | `ResourceUploadServlet` + `upload-resource.jsp` | Task 1, 0 |
| 4 | `FlagDao` + `FlagDaoImpl` | — |
| 5 | `FlagServlet` | Task 4, 0 |
| 6 | Update `resource-detail.jsp` (create file) | Task 0, 5 |
| 7 | Filter / integration hardening | All |

---

## Task 0 — Prerequisites (required for integration)

### 0.1 Implement `ResourceDaoImpl.getResourceById`

**File:** `src/main/java/com/ScholarShare/dao/daoImpl/ResourceDaoImpl.java`

**Purpose:** Load a single resource for ownership checks, detail view, and flag validation.

**Method signature:** (already on interface)
```java
@Override
public Resource getResourceById(int resourceId)
```

**SQL:**
```sql
SELECT r.*, t.topic_name, u.full_name AS submitter_name
FROM resources r
JOIN topics t ON r.topic_id = t.topic_id
JOIN users u ON r.user_id = u.user_id
WHERE r.resource_id = ?
```

**Mapping:** Mirror `AdminDaoImp.getRecentSubmission()` row mapping:
- `resource_id`, `user_id`, `topic_id`, `title`, `description`, `file_path`, `resource_type`, `status`, `self_declaration`, `upload_date`
- Joined: `topic_name` → `setTopicName`, `submitter_name` → `setSubmitterName`

**Validation:** Return `null` if not found.

**Testing checklist:**
- [ ] Existing ID returns populated `Resource`
- [ ] Invalid ID returns `null`
- [ ] SQLException logged, returns `null`

---

### 0.2 Student resource list (upload redirect target)

**Files to create:**
- `src/main/java/com/ScholarShare/controller/StudentResourceServlet.java`
- `src/main/webapp/WEB-INF/views/my-resources.jsp`
- `src/main/webapp/css/my-resources.css` (optional; can reuse `Register.css` card layout)

**Purpose:** Display logged-in student's uploads; success redirect from upload lands here.

**Servlet:**
```java
@WebServlet("/student/resources")
public class StudentResourceServlet extends HttpServlet
```

**GET flow:**
1. `User user = (User) SessionUtil.getAttribute(request, "user");`
2. Reject if `user == null || !user.isStudent()` → redirect `contextPath + "/login"`
3. `List<Resource> list = new ResourceDaoImpl().getResourcesByUser(user.getUserId());`
4. `request.setAttribute("resources", list);`
5. Forward to `/WEB-INF/views/my-resources.jsp`

**Implement `getResourcesByUser` in `ResourceDaoImpl`:**
```sql
SELECT r.*, t.topic_name
FROM resources r
JOIN topics t ON r.topic_id = t.topic_id
WHERE r.user_id = ?
ORDER BY r.upload_date DESC
```

**Testing checklist:**
- [ ] Unauthenticated user redirected
- [ ] Student sees only own resources
- [ ] Empty list shows friendly message

---

### 0.3 Resource detail page (flag redirect target)

**Files to create:**
- `src/main/java/com/ScholarShare/controller/ResourceDetailServlet.java`
- `src/main/webapp/WEB-INF/views/resource-detail.jsp`

**Servlet:**
```java
@WebServlet("/resource/detail")
public class ResourceDetailServlet extends HttpServlet
```

**GET:** `resourceId` request param → `getResourceById` → forward to JSP or 404 redirect.

**JSP must expose:** `${resource}`, flash params `success`, `error`, `duplicateFlag` (see Task 6).

**Flag eligibility:** Only show “Flag for Plagiarism” when `resource.status == 'approved'` (community flags published content per `schema.sql` comments).

**Testing checklist:**
- [ ] Approved resource shows flag UI
- [ ] Pending resource hides flag button
- [ ] Invalid `resourceId` handled

---

## Task 1 — Complete `ResourceDaoImpl.uploadResource`

**File:** `src/main/java/com/ScholarShare/dao/daoImpl/ResourceDaoImpl.java`

**Purpose:** Persist upload metadata after servlet saves physical file.

### Method signature
```java
@Override
public boolean uploadResource(Resource resource)
```

### Pre-insert validation (DAO layer)
| Rule | Check |
|------|-------|
| Title | `ValidationUtil.isNullOrEmpty(resource.getTitle())` OR length > 200 |
| Resource type | Must be one of ENUM: `notes`, `past_paper`, `summary`, `revision_guide`, `other` |
| Topic | `resource.getTopicId() > 0` |
| Owner | `resource.getUserId() > 0` |
| File path | `ValidationUtil.isNullOrEmpty(resource.getFilePath())` |
| Integrity pledge | `resource.isSelfDeclaration()` must be `true` |
| Duplicate filename | See SQL below |

### Duplicate filename protection SQL
```sql
SELECT COUNT(*) AS cnt FROM resources WHERE file_path = ?
```
If `cnt > 0` → return `false` (servlet should surface “file already exists”).

### Insert SQL (actual columns)
```sql
INSERT INTO resources (
    user_id, topic_id, title, description, file_path,
    resource_type, status, self_declaration, upload_date
) VALUES (?, ?, ?, ?, ?, ?, 'pending', ?, ?)
```

**PreparedStatement bindings:**
1. `user_id` ← `resource.getUserId()`
2. `topic_id` ← `resource.getTopicId()`
3. `title` ← trim, max 200 chars
4. `description` ← nullable TEXT
5. `file_path` ← relative path e.g. `uploads/resources/{uuid}_{sanitized}.pdf`
6. `resource_type` ← ENUM string
7. `self_declaration` ← `resource.isSelfDeclaration() ? 1 : 0`
8. `upload_date` ← `new Timestamp(System.currentTimeMillis())` OR omit and use DB default

### Transaction safety
Use try-with-resources (match `CategoryDaoImpl` style):
```java
try (Connection conn = DatabaseConnection.getConnection()) {
    conn.setAutoCommit(false);
    // duplicate check + insert in same connection
    conn.commit();
} catch (SQLException e) {
    // rollback in catch
    return false;
}
```

### Exception handling
- Log: `System.out.println("uploadResource failed: " + e.getMessage());` (consistent with `CollectionDaoImpl`)
- Return `false` on any failure; do not throw to servlet

### Exact code scaffold
```java
private static final Set<String> ALLOWED_TYPES = Set.of(
    "notes", "past_paper", "summary", "revision_guide", "other");

private boolean isValidResourceType(String type) {
    return type != null && ALLOWED_TYPES.contains(type);
}

@Override
public boolean uploadResource(Resource resource) {
    if (resource == null || !resource.isSelfDeclaration()) return false;
    if (ValidationUtil.isNullOrEmpty(resource.getTitle())
            || ValidationUtil.isNullOrEmpty(resource.getFilePath())
            || !isValidResourceType(resource.getResourceType())
            || resource.getUserId() <= 0
            || resource.getTopicId() <= 0) {
        return false;
    }

    String sqlCheck = "SELECT COUNT(*) FROM resources WHERE file_path = ?";
    String sqlInsert = """
        INSERT INTO resources (user_id, topic_id, title, description, file_path,
            resource_type, status, self_declaration, upload_date)
        VALUES (?, ?, ?, ?, ?, ?, 'pending', ?, ?)
        """;

    try (Connection conn = DatabaseConnection.getConnection()) {
        conn.setAutoCommit(false);
        try (PreparedStatement check = conn.prepareStatement(sqlCheck)) {
            check.setString(1, resource.getFilePath());
            try (ResultSet rs = check.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    conn.rollback();
                    return false;
                }
            }
        }
        try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
            ps.setInt(1, resource.getUserId());
            ps.setInt(2, resource.getTopicId());
            ps.setString(3, resource.getTitle().trim());
            ps.setString(4, resource.getDescription());
            ps.setString(5, resource.getFilePath());
            ps.setString(6, resource.getResourceType());
            ps.setBoolean(7, resource.isSelfDeclaration());
            ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            boolean ok = ps.executeUpdate() > 0;
            if (ok) conn.commit(); else conn.rollback();
            return ok;
        }
    } catch (SQLException e) {
        System.out.println("uploadResource failed: " + e.getMessage());
        return false;
    }
}
```

### Testing checklist
- [ ] Valid insert creates row with `status='pending'`, `self_declaration=1`
- [ ] Missing pledge returns `false` without insert
- [ ] Duplicate `file_path` blocked
- [ ] Invalid `resource_type` rejected
- [ ] DB failure rolls back (no partial row)

---

## Task 2 — Complete `ResourceDaoImpl.deleteResource`

**File:** `src/main/java/com/ScholarShare/dao/daoImpl/ResourceDaoImpl.java`

**Purpose:** Delete resource owned by student; remove file from disk.

### Recommended interface extension
Current signature: `boolean deleteResource(int resourceId)` — **cannot verify ownership with ID alone.**

**Option A (preferred):** Add to `ResourceDao.java`:
```java
boolean deleteResource(int resourceId, int userId);
```
Keep old method delegating or remove after servlet updated.

**Option B:** Servlet calls `getResourceById`, verifies `userId`, then calls DAO.

This TODO uses **Option A**.

### Method signatures
```java
boolean deleteResource(int resourceId, int userId);  // new
// OR verify ownership inside DAO using getResourceById first
```

### Ownership verification SQL
```sql
SELECT user_id, file_path FROM resources WHERE resource_id = ?
```
If no row → `false`. If `user_id != userId` → `false`.

### Delete DB row SQL
```sql
DELETE FROM resources WHERE resource_id = ? AND user_id = ?
```

### Physical file deletion
```java
String absolutePath = servletContext.getRealPath("/" + filePath);
// OR Paths.get(uploadBaseDir, filePath) if stored outside webapp
Files.deleteIfExists(Paths.get(absolutePath));
```
**Note:** File deletion belongs in **servlet/service** if DAO has no `ServletContext`. Split:
- DAO: ownership check + DB delete (transaction)
- Servlet: resolve path + delete file **after** successful DB delete, or delete file first then DB with rollback on DB failure

**Safer order:** DB delete first (CASCADE cleans flags/ratings); then delete file. If file delete fails, log warning only.

### Transaction scaffold
```java
@Override
public boolean deleteResource(int resourceId, int userId) {
    String selectSql = "SELECT user_id, file_path FROM resources WHERE resource_id = ?";
    String deleteSql = "DELETE FROM resources WHERE resource_id = ? AND user_id = ?";
    try (Connection conn = DatabaseConnection.getConnection()) {
        conn.setAutoCommit(false);
        // select, verify userId, delete, commit
    } catch (SQLException e) {
        // rollback
        return false;
    }
}
```

Return deleted `file_path` to servlet via wrapper or two-step: `getResourceById` then `deleteResource`.

### Testing checklist
- [ ] Owner can delete; file removed
- [ ] Non-owner cannot delete
- [ ] Non-existent ID returns `false`
- [ ] SQLException rolls back

---

## Task 3 — `ResourceUploadServlet`

**File to create:** `src/main/java/com/ScholarShare/controller/ResourceUploadServlet.java`

**Purpose:** Multipart upload endpoint for students.

### Class-level annotation
```java
@WebServlet("/student/upload")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,      // 1 MB
    maxFileSize = 10 * 1024 * 1024,       // 10 MB per file
    maxRequestSize = 15 * 1024 * 1024   // 15 MB total
)
public class ResourceUploadServlet extends HttpServlet {
    private final ResourceDao resourceDao = new ResourceDaoImpl();
    private final CategoryDao categoryDao = new CategoryDaoImpl();
}
```

### GET
1. Session user must be `user.isStudent()`
2. Load category data for dropdowns:
   - `request.setAttribute("faculties", categoryDao.getAllFaculties());`
   - Optional query params `facultyId`, `subjectId` → load subjects/topics (copy pattern from `AdminServlet` `/categories`)
3. Forward: `/WEB-INF/views/upload-resource.jsp`
4. Preserve repost values: `title`, `description`, `resourceType`, `topicId`, `errors` list

### POST parameters
| Param | Type | Maps to |
|-------|------|---------|
| `title` | String | `Resource.title` |
| `description` | String | `Resource.description` |
| `resourceType` | String | `Resource.resourceType` |
| `topicId` | int | `Resource.topicId` |
| `integrityPledge` | checkbox `on` / absent | `Resource.selfDeclaration` |
| `file` | `Part` | disk + `filePath` |

### Validation rules (reject → forward with `errors`)
| Rule | Error message |
|------|---------------|
| Not logged in / not student | Redirect `/login` |
| Pledge unchecked | `You must confirm the Academic Integrity Pledge.` |
| Empty title | `Title is required.` |
| Title length > 200 | `Title must be 200 characters or fewer.` |
| Empty description | `Description is required.` (if required by coursework) |
| Invalid / missing `topicId` | `Please select a topic category.` |
| Invalid `resourceType` | `Invalid resource type.` |
| No file / `part.getSize() == 0` | `Please select a file to upload.` |
| File too large | `File exceeds maximum size of 10 MB.` |
| Invalid extension | `Allowed types: PDF, DOC, DOCX, PPT, PPTX.` |

**Allowed extensions (project has no prior list — use):**
`.pdf`, `.doc`, `.docx`, `.ppt`, `.pptx`

### Unique filename generation
```java
String original = Paths.get(part.getSubmittedFileName()).getFileName().toString();
String ext = "";
int dot = original.lastIndexOf('.');
if (dot >= 0) ext = original.substring(dot).toLowerCase();
String storedName = UUID.randomUUID() + "_" + System.currentTimeMillis() + ext;
String relativePath = "uploads/resources/" + storedName;
```

### Save file (project convention from `schema.sql` comment)
```java
String uploadDir = getServletContext().getRealPath("/uploads/resources");
Files.createDirectories(Paths.get(uploadDir));
part.write(uploadDir + File.separator + storedName);
```

Add to `.gitignore`:
```
src/main/webapp/uploads/
```

### Build `Resource` and call DAO
```java
Resource r = new Resource();
r.setUserId(user.getUserId());
r.setTopicId(topicId);
r.setTitle(title);
r.setDescription(description);
r.setResourceType(resourceType);
r.setFilePath(relativePath);
r.setSelfDeclaration("on".equals(req.getParameter("integrityPledge")));

if (resourceDao.uploadResource(r)) {
    resp.sendRedirect(req.getContextPath() + "/student/resources?upload=success");
} else {
    // delete orphaned file if DB failed
    req.setAttribute("errors", List.of("Upload failed. Please try again."));
    doGet(req, resp);
}
```

### Response flow
| Result | Action |
|--------|--------|
| Success | `sendRedirect` → `/student/resources?upload=success` |
| Failure | `forward` → `upload-resource.jsp` with `errors` |

### Testing checklist
- [ ] GET shows form when logged in as student
- [ ] POST without pledge shows error
- [ ] POST with invalid extension rejected
- [ ] POST with oversized file rejected (413 or validation message)
- [ ] Success creates DB row + file on disk
- [ ] DB failure does not leave orphan file (or cleanup runs)

---

## Task 4 — `upload-resource.jsp`

**File to create:** `src/main/webapp/WEB-INF/views/upload-resource.jsp`

**Purpose:** Upload form UI matching existing auth page patterns.

### Layout conventions (from `register.jsp` / `login.jsp`)
- `<%@ taglib prefix="c" uri="jakarta.tags.core" %>`
- Fonts: DM Sans, DM Serif Display (Google Fonts)
- CSS: `headerFooter.css` + `Register.css` (or new `upload-resource.css`)
- Navbar block copied from `register.jsp` (lines 22–63 pattern)
- Error box style: Bootstrap-like alert (`.invalid-error` inline styles from register.jsp)

### Form attributes
```html
<form action="${pageContext.request.contextPath}/student/upload"
      method="post"
      enctype="multipart/form-data"
      id="uploadResourceForm">
```

### Fields
| Field | HTML |
|-------|------|
| Title | `<input type="text" name="title" maxlength="200" required value="${title}">` |
| Description | `<textarea name="description" required>${description}</textarea>` |
| Resource Type | `<select name="resourceType" required>` — options: notes, past_paper, summary, revision_guide, other |
| Topic Category | Cascading: Faculty → Subject → Topic **or** single `<select name="topicId">` populated server-side |
| File | `<input type="file" name="file" accept=".pdf,.doc,.docx,.ppt,.pptx" required>` |
| Integrity pledge | Checkbox `name="integrityPledge"` value `on` required |

### Mandatory pledge label (exact text)
> I confirm that this uploaded resource is my original academic work or properly cited material, and I understand that submitting plagiarized content violates academic integrity policies.

### Client-side validation
**File to create:** `src/main/webapp/js/upload-resource.js`
- Mirror `Register.js` pattern: `DOMContentLoaded`, form `submit` listener
- Block submit if pledge unchecked
- Validate file extension and max size (10 MB) before submit

### Success message
On `my-resources.jsp`: `<c:if test="${param.upload eq 'success'}">` success banner.

### Error display
```jsp
<c:if test="${not empty errors}">
  <c:forEach var="err" items="${errors}">
    <p class="invalid-error">${err}</p>
  </c:forEach>
</c:if>
```

### Testing checklist
- [ ] Checkbox required (HTML5 + JS)
- [ ] Errors render without losing typed values
- [ ] Mobile layout acceptable (viewport meta present)

---

## Task 5 — `FlagDao` + `FlagDaoImpl`

### 5.1 Create `FlagDao.java`

**File:** `src/main/java/com/ScholarShare/dao/FlagDao.java`

```java
package com.ScholarShare.dao;

import com.ScholarShare.entity.Flag;

public interface FlagDao {
    boolean createFlag(Flag flag);
    boolean hasStudentAlreadyFlagged(int resourceId, int studentId);
}
```

**Note:** Parameter `studentId` maps to DB column `flagged_by`.

---

### 5.2 Create `FlagDaoImpl.java`

**File:** `src/main/java/com/ScholarShare/dao/daoImpl/FlagDaoImpl.java`

#### `hasStudentAlreadyFlagged`
```sql
SELECT COUNT(*) FROM flags
WHERE resource_id = ? AND flagged_by = ?
```
Return `count > 0`.

#### `createFlag`
**Pre-check:** Call `hasStudentAlreadyFlagged` → return `false` if duplicate.

```sql
INSERT INTO flags (resource_id, flagged_by, reason, status, created_at)
VALUES (?, ?, ?, 'open', ?)
```

Bindings:
- `resource_id` ← `flag.getResourceId()`
- `flagged_by` ← `flag.getFlaggedBy()`
- `reason` ← trimmed, not empty
- `created_at` ← `new Timestamp(System.currentTimeMillis())`

#### Validation rules
| Rule | Enforcement |
|------|-------------|
| `resourceId > 0` | DAO |
| `flaggedBy > 0` | DAO |
| Reason not empty, min 10 chars, max 2000 | DAO or servlet |
| Duplicate | `hasStudentAlreadyFlagged` |

#### Exception handling
Same pattern as `CollectionDaoImpl` — log and return `false`.

#### Testing checklist
- [ ] First flag inserts with `status='open'`
- [ ] Second flag by same user on same resource returns `false`
- [ ] Empty reason rejected

---

## Task 6 — `Flag.java` entity

**File:** `src/main/java/com/ScholarShare/entity/Flag.java` — **ALREADY EXISTS**

**Do NOT recreate.** Use existing fields:

| Spec name | Existing field |
|-----------|----------------|
| `id` | `flagId` |
| `resourceId` | `resourceId` |
| `studentId` | `flaggedBy` |
| `reason` | `reason` |
| `createdAt` | `createdAt` |
| `status` | `status` |

Optional: add alias methods `getStudentId()` / `setStudentId()` delegating to `flaggedBy` for clarity.

---

## Task 7 — `FlagServlet`

**File to create:** `src/main/java/com/ScholarShare/controller/FlagServlet.java`

```java
@WebServlet("/resource/flag")
public class FlagServlet extends HttpServlet {
    private final FlagDao flagDao = new FlagDaoImpl();
    private final ResourceDao resourceDao = new ResourceDaoImpl();
}
```

### POST flow
1. **Session:** `User user = (User) SessionUtil.getAttribute(request, "user");` — if null or `!user.isStudent()` → redirect login
2. **Params:** `resourceId`, `reason`
3. **Validate resource exists:** `Resource r = resourceDao.getResourceById(resourceId);` if null → redirect with `error=notfound`
4. **Validate approved only:** if `!r.isApproved()` → `error=notapproved`
5. **Validate reason:** `ValidationUtil.isNullOrEmpty(reason)` or length < 10 → forward/back with error
6. **Duplicate:** if `flagDao.hasStudentAlreadyFlagged(resourceId, user.getUserId())` → redirect `.../resource/detail?resourceId=X&duplicateFlag=1`
7. **Save:**
```java
Flag flag = new Flag();
flag.setResourceId(resourceId);
flag.setFlaggedBy(user.getUserId());
flag.setReason(reason.trim());
flag.setStatus("open");
flagDao.createFlag(flag);
```
8. **Success:** `sendRedirect(contextPath + "/resource/detail?resourceId=" + resourceId + "&flagSuccess=1");`

### GET
Not required; return 405 or redirect to detail page.

### Testing checklist
- [ ] Guest cannot POST
- [ ] Duplicate shows warning on detail page
- [ ] Success shows confirmation
- [ ] Flagging pending resource blocked

---

## Task 8 — Update `resource-detail.jsp`

**File to create:** `src/main/webapp/WEB-INF/views/resource-detail.jsp`  
(Depends on Task 0.3 `ResourceDetailServlet`)

### Display resource
- Title, description, type, topic name, uploader, status badge, upload date

### Flag UI (approved resources only)
```jsp
<c:if test="${resource.approved}">
  <!-- Button -->
  <button type="button" id="openFlagModal">Flag for Plagiarism</button>

  <!-- Modal -->
  <motion/div id="flagModal" class="flag-modal hidden">
    <p class="flag-warning">False reporting may result in disciplinary action.</p>
    <form action="${pageContext.request.contextPath}/resource/flag" method="post">
      <input type="hidden" name="resourceId" value="${resource.resourceId}" />
      <textarea name="reason" required minlength="10" placeholder="Describe your concern..."></textarea>
      <button type="submit">Submit Flag</button>
      <button type="button" id="cancelFlagModal">Cancel</button>
    </form>
  </motion/div>
</c:if>
```

Use `resource.approved` EL → calls `Resource.isApproved()`.

### Feedback messages
```jsp
<c:if test="${param.flagSuccess eq '1'}">
  <p class="success-msg">Your report has been submitted for admin review.</p>
</c:if>
<c:if test="${param.duplicateFlag eq '1'}">
  <p class="warning-msg">You have already flagged this resource.</p>
</c:if>
<c:if test="${not empty error}">
  <p class="invalid-error">${error}</p>
</c:if>
```

### JS
**File:** `src/main/webapp/js/resource-detail.js` — open/close modal (vanilla JS, match project style).

### CSS
Add modal styles to new `resource-detail.css` or extend `home.css` color tokens (`#36454F`, `#F5DEB3`).

### Testing checklist
- [ ] Modal opens/closes
- [ ] Warning text visible before submit
- [ ] Success/duplicate/error messages display
- [ ] Form POST reaches `FlagServlet`

---

## Task 9 — Integration & hardening

### 9.1 `AuthenticationFilter` updates

**File:** `src/main/java/com/ScholarShare/filter/AuthenticationFilter.java`

Current public paths: `/css`, `/js`, `/images`, `/home`, `/aboutUs`

**Add student path protection (optional enhancement):**
- If path starts with `/admin` and user is student → redirect `/home`
- Fix latent NPE: `assert user != null` on line 39 runs for logged-out users on non-auth pages — replace with null-safe checks

### 9.2 Align `StudentDaoImpl.flagResourceForPlagiarism`

Either:
- Implement as delegate to `FlagDaoImpl`, or
- Leave stub and document that `FlagDao` is canonical

### 9.3 `web.xml` welcome file

Already maps to `home` — no change required.

---

## Missing Dependencies

| Item | Status |
|------|--------|
| Jakarta Servlet API | ✅ `pom.xml` (provided) |
| MySQL connector | ✅ |
| JSTL | ✅ |
| Apache Commons FileUpload | ❌ **Not needed** — use `@MultipartConfig` + `Part` |
| JSON library | ❌ Not required for these tasks |

**Server config:** Ensure Tomcat has adequate `maxPostSize` if uploads fail at container level.

---

## Integration Risks

| Risk | Impact | Mitigation |
|------|--------|------------|
| Field name mismatch (`student_id` vs `user_id`) | SQL errors | Use schema column names only |
| `Flag.java` already exists | Duplicate class | Extend existing entity |
| No student dashboard JSP | Broken redirect | Build Task 0.2 first |
| `resource-detail.jsp` missing | Flag redirect 404 | Build Task 0.3 first |
| `AuthenticationFilter` redirects guests to `/home` not `/login` | Confusing UX | Accept or adjust filter |
| `deleteResource(int)` no ownership param | Security hole | Add `userId` parameter |
| Files under `webapp/uploads/` wiped on redeploy | Lost files | Document; consider external storage path |
| `AdminDaoImp` uses wrong table name `USER` in places | Unrelated admin bugs | Do not copy that SQL |
| `StudentDaoImpl.getUserById` uses `WHERE id = ?` | Broken student lookup | Fix separately if used |
| No unique DB constraint on `(resource_id, flagged_by)` | Race duplicate flags | DAO check + optional UNIQUE index |
| Filter does not block students from `/admin/*` | Students see admin UI | Add role check in filter |

---

## Suggested Code Changes Summary (by file)

| File | Action |
|------|--------|
| `ResourceDaoImpl.java` | Implement `uploadResource`, `deleteResource`, `getResourceById`, `getResourcesByUser` |
| `ResourceDao.java` | Add `deleteResource(int resourceId, int userId)` if using Option A |
| `ResourceUploadServlet.java` | **Create** — multipart upload |
| `upload-resource.jsp` | **Create** |
| `upload-resource.js` | **Create** |
| `StudentResourceServlet.java` | **Create** — list + redirect target |
| `my-resources.jsp` | **Create** |
| `ResourceDetailServlet.java` | **Create** |
| `resource-detail.jsp` | **Create** with flag modal |
| `resource-detail.js` | **Create** |
| `FlagDao.java` | **Create** |
| `FlagDaoImpl.java` | **Create** |
| `FlagServlet.java` | **Create** |
| `Flag.java` | Keep; optional alias getters |
| `AuthenticationFilter.java` | Admin role guard + null-safety |
| `.gitignore` | Add `src/main/webapp/uploads/` |

---

## Master Testing Checklist (end-to-end)

### Upload flow
- [ ] Student logs in → `/student/upload` → form loads with categories
- [ ] Submit valid PDF with pledge → redirect to `/student/resources?upload=success`
- [ ] Submit without pledge → errors on form
- [ ] Submit duplicate filename path → error (if same stored name — unlikely with UUID)
- [ ] DB row: `status=pending`, `self_declaration=1`

### Delete flow (if exposed in UI later)
- [ ] Owner deletes → row gone, file gone
- [ ] Other student cannot delete

### Flag flow
- [ ] Open approved resource detail → flag modal works
- [ ] Submit reason → success message
- [ ] Submit second flag → duplicate warning
- [ ] Flag pending resource → blocked
- [ ] Admin `flags-management.jsp` shows new open flag (after admin list wired to live data)
