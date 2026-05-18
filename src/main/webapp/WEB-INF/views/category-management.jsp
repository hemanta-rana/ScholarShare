<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
<title>ScholarShare — Category Management</title>
<link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png" />
<link rel="preconnect" href="https://fonts.googleapis.com" />
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
<link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@400;500;600;700&display=swap" rel="stylesheet"/>
<link href="${pageContext.request.contextPath}/css/sidebar.css" rel="stylesheet"/>
<link href="${pageContext.request.contextPath}/css/adminDashboard.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/css/category-management.css" rel="stylesheet" />
</head>
<body>

<jsp:include page="template/admin-sidebar.jsp">
    <jsp:param name="activePage" value="categories" />
</jsp:include>

<div class="main">
    <jsp:include page="template/admin-header.jsp">
        <jsp:param name="searchPlaceholder" value="Search categories..." />
    </jsp:include>

  <div class="content">

    <!-- STATS BAR -->
    <div class="stats-bar">
      <div class="stat-card">
        <div class="stat-icon orange">
          <img src="${pageContext.request.contextPath}/images/home-icons/landmark.png" alt="" width="22" height="22">
        </div>
        <div>
          <div class="stat-val" id="statFaculty">${facultyCount != null ? facultyCount : 0}</div>
          <div class="stat-lbl">Faculties</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon blue">
          <img src="${pageContext.request.contextPath}/images/home-icons/book-open.png" alt="" width="22" height="22">
        </div>
        <div>
          <div class="stat-val" id="statSubject">${subjectCount != null ? subjectCount : 0}</div>
          <div class="stat-lbl">Subjects</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon purple">
          <img src="${pageContext.request.contextPath}/images/home-icons/discover.png" alt="" width="22" height="22">
        </div>
        <div>
          <div class="stat-val" id="statTopic">${topicCount != null ? topicCount : 0}</div>
          <div class="stat-lbl">Topics</div>
        </div>
      </div>
    </div>

    <!-- BREADCRUMB -->
    <div class="breadcrumb-nav" id="breadcrumbNav">
      <a href="${pageContext.request.contextPath}/admin/categories" class="bc-item ${empty selectedFacultyId ? 'active' : ''}">All Faculties</a>
      <c:if test="${not empty selectedFaculty}">
          <span class="bc-sep">›</span>
          <a href="${pageContext.request.contextPath}/admin/categories?facultyId=${selectedFaculty.facultyId}" class="bc-item ${empty selectedSubjectId ? 'active' : ''}">${selectedFaculty.facultyName}</a>
      </c:if>
      <c:if test="${not empty selectedSubject}">
          <span class="bc-sep">›</span>
          <span class="bc-item active">${selectedSubject.subjectName}</span>
      </c:if>
    </div>

    <!-- 3-COLUMN LAYOUT -->
    <div class="cat-layout">
      <!-- FACULTY PANEL -->
      <div class="cat-panel">
        <div class="panel-header">
          <div class="panel-title">
            <span class="dot dot-faculty"></span>
            Faculty
          </div>
          <span class="panel-count" id="facultyCount">${faculties.size()}</span>
        </div>
        <form class="add-row" method="POST" action="${pageContext.request.contextPath}/admin/categories">
          <input type="hidden" name="action" value="addFaculty" />
          <input type="text" name="facultyName" placeholder="New faculty name..." required/>
          <button type="submit" class="add-btn orange">+ Add</button>
        </form>
        <div class="items-list" id="facultyList">
            <c:forEach var="faculty" items="${faculties}">
                <a href="${pageContext.request.contextPath}/admin/categories?facultyId=${faculty.facultyId}" class="cat-item ${faculty.facultyId == selectedFacultyId ? 'selected orange-sel' : ''}">
                  <div class="cat-item-icon icon-faculty">${faculty.facultyName.substring(0,2).toUpperCase()}</div>
                  <div class="cat-item-info">
                    <div class="cat-item-name">${faculty.facultyName}</div>
                  </div>
                  <div class="cat-item-actions" onclick="event.preventDefault();">
                    <button class="icon-btn del" title="Delete" onclick="openDeleteModal('deleteFaculty', ${faculty.facultyId}, null, null, '${faculty.facultyName}')">
                      <img src="${pageContext.request.contextPath}/images/home-icons/trash_9915690.png" alt="Delete" width="16" height="16">
                    </button>
                  </div>
                </a>
            </c:forEach>
        </div>
      </div>

      <!-- SUBJECT PANEL -->
      <div class="cat-panel">
        <div class="panel-header">
          <div class="panel-title">
            <span class="dot dot-subject"></span>
            Subject
          </div>
          <span class="panel-count" id="subjectCount">${not empty subjects ? subjects.size() : '—'}</span>
        </div>
        <c:choose>
            <c:when test="${not empty selectedFacultyId}">
                <form class="add-row" method="POST" action="${pageContext.request.contextPath}/admin/categories">
                  <input type="hidden" name="action" value="addSubject" />
                  <input type="hidden" name="facultyId" value="${selectedFacultyId}" />
                  <input type="text" name="subjectName" placeholder="New subject name..." required/>
                  <button type="submit" class="add-btn">+ Add</button>
                </form>
                <div class="items-list" id="subjectList">
                    <c:forEach var="subject" items="${subjects}">
                        <a href="${pageContext.request.contextPath}/admin/categories?facultyId=${selectedFacultyId}&subjectId=${subject.subjectId}" class="cat-item ${subject.subjectId == selectedSubjectId ? 'selected' : ''}">
                          <div class="cat-item-icon icon-subject">${subject.subjectName.substring(0,2).toUpperCase()}</div>
                          <div class="cat-item-info">
                            <div class="cat-item-name">${subject.subjectName}</div>
                          </div>
                          <div class="cat-item-actions" onclick="event.preventDefault();">
                            <button class="icon-btn del" title="Delete" onclick="openDeleteModal('deleteSubject', ${subject.subjectId}, ${selectedFacultyId}, null, '${subject.subjectName}')">
                              <img src="${pageContext.request.contextPath}/images/home-icons/trash_9915690.png" alt="Delete" width="16" height="16">
                            </button>
                          </div>
                        </a>
                    </c:forEach>
                    <c:if test="${empty subjects}">
                        <div class="panel-empty" style="text-align:center; padding: 2rem 1rem;">
                            <p>No subjects in this faculty.</p>
                        </div>
                    </c:if>
                </div>
            </c:when>
            <c:otherwise>
                <div class="items-list" id="subjectList">
                  <div class="panel-empty">
                    <img src="${pageContext.request.contextPath}/images/home-icons/book-open.png" alt="" width="32" height="32">
                    <p>Select a Faculty to browse and manage its subjects</p>
                  </div>
                </div>
            </c:otherwise>
        </c:choose>
      </div>

      <!-- TOPIC PANEL -->
      <div class="cat-panel">
        <div class="panel-header">
          <div class="panel-title">
            <span class="dot dot-topic"></span>
            Topic
          </div>
          <span class="panel-count" id="topicCount">${not empty topics ? topics.size() : '—'}</span>
        </div>
        <c:choose>
            <c:when test="${not empty selectedSubjectId}">
                <form class="add-row" method="POST" action="${pageContext.request.contextPath}/admin/categories">
                  <input type="hidden" name="action" value="addTopic" />
                  <input type="hidden" name="facultyId" value="${selectedFacultyId}" />
                  <input type="hidden" name="subjectId" value="${selectedSubjectId}" />
                  <input type="text" name="topicName" placeholder="New topic name..." required/>
                  <button type="submit" class="add-btn purple">+ Add</button>
                </form>
                <div class="items-list" id="topicList">
                    <c:forEach var="topic" items="${topics}">
                        <div class="cat-item">
                          <div class="cat-item-icon icon-topic">${topic.topicName.substring(0,2).toUpperCase()}</div>
                          <div class="cat-item-info">
                            <div class="cat-item-name">${topic.topicName}</div>
                          </div>
                          <div class="cat-item-actions">
                            <button class="icon-btn del" title="Delete" onclick="openDeleteModal('deleteTopic', ${topic.topicId}, ${selectedFacultyId}, ${selectedSubjectId}, '${topic.topicName}')">
                              <img src="${pageContext.request.contextPath}/images/home-icons/trash_9915690.png" alt="Delete" width="16" height="16">
                            </button>
                          </div>
                        </div>
                    </c:forEach>
                    <c:if test="${empty topics}">
                        <div class="panel-empty" style="text-align:center; padding: 2rem 1rem;">
                            <p>No topics in this subject.</p>
                        </div>
                    </c:if>
                </div>
            </c:when>
            <c:otherwise>
                <div class="items-list" id="topicList">
                  <div class="panel-empty">
                    <img src="${pageContext.request.contextPath}/images/home-icons/discover.png" alt="" width="32" height="32">
                    <p>Select a Subject to browse and manage its topics</p>
                  </div>
                </div>
            </c:otherwise>
        </c:choose>
      </div>
    </div>
  </div>

    <footer class="site-footer">
        <span class="footer-copy">© 2023 ScholarShare · Institutional Repository Management System</span>
        <div class="footer-links">
            <a href="${pageContext.request.contextPath}/docs">Documentation</a>
            <a href="${pageContext.request.contextPath}/support">Support Desk</a>
            <a href="${pageContext.request.contextPath}/status">System Status</a>
        </div>
    </footer>
    <div class="sidebar-overlay" id="sidebarOverlay"></div>

</div>

<!-- DELETE MODAL -->
<div class="modal-overlay" id="deleteModal">
  <div class="modal">
    <div class="modal-title">Confirm Deletion</div>
    <div class="modal-body" id="deleteModalBody">Are you sure you want to delete this item? <strong>This cannot be undone.</strong></div>
    <div class="modal-actions">
      <button class="btn btn-ghost" onclick="closeDeleteModal()">Cancel</button>
      <form id="deleteForm" method="POST" action="${pageContext.request.contextPath}/admin/categories" style="margin:0;">
          <input type="hidden" name="action" id="deleteAction" />
          <input type="hidden" name="id" id="deleteId" />
          <input type="hidden" name="facultyId" id="deleteFacultyId" />
          <input type="hidden" name="subjectId" id="deleteSubjectId" />
          <button type="submit" class="btn btn-danger">Delete</button>
      </form>
    </div>
  </div>
</div>

<div class="toast-container" id="toastContainer"></div>

<script src="${pageContext.request.contextPath}/js/admin-dashboard-navbar.js"></script>

<script>
function openDeleteModal(action, id, facultyId, subjectId, itemName) {
    document.getElementById('deleteAction').value = action;
    document.getElementById('deleteId').value = id;
    if(facultyId) document.getElementById('deleteFacultyId').value = facultyId;
    if(subjectId) document.getElementById('deleteSubjectId').value = subjectId;
    
    document.getElementById('deleteModalBody').innerHTML = "Are you sure you want to delete <strong>" + itemName + "</strong>? This cannot be undone.";
    document.getElementById('deleteModal').classList.add('open');
}

function closeDeleteModal() {
    document.getElementById('deleteModal').classList.remove('open');
}
</script>

</body>
</html>
