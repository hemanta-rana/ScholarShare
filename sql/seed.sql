-- ============================================================
-- ScholarShare — Demo Seed Data
-- Purpose : Populate all 11 tables with realistic data so a
--           professor can log in as any existing user and see
--           every feature of the platform working end-to-end.
--
-- Existing users (DO NOT TOUCH):
--   id=1  → admin
--   id=2  → student (active)
--   id=9  → student (active)
--   id=13 → student (active)
--
-- New demo users added below:
--   id=20  student  priya.sharma@scholarshare.ac.uk  / Student@123
--   id=21  student  james.okafor@scholarshare.ac.uk  / Student@123
--   id=22  student  (pending — for admin approval demo)
--   id=23  student  (suspended — for admin demo)
--
-- BCrypt hash for "Student@123" (cost=10):
--   $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHi
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================
-- TABLE: users
-- Purpose: Add extra demo students so every status value is
--          visible in the admin user-approvals panel.
-- ============================================================
INSERT INTO users (user_id, full_name, email, phone, password, role, status, profile_pic, created_at)
VALUES
  (20, 'Priya Sharma',   'priya.sharma@scholarshare.ac.uk',  '07700900020', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHi', 'student', 'active',    NULL, '2026-01-10 09:00:00'),
  (21, 'James Okafor',   'james.okafor@scholarshare.ac.uk',  '07700900021', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHi', 'student', 'active',    NULL, '2026-01-12 10:30:00'),
  (22, 'Mei Lin Chen',   'mei.chen@scholarshare.ac.uk',      '07700900022', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHi', 'student', 'pending',   NULL, '2026-05-14 08:15:00'),
  (23, 'Tariq Hassan',   'tariq.hassan@scholarshare.ac.uk',  '07700900023', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHi', 'student', 'suspended', NULL, '2026-02-01 11:00:00')
ON DUPLICATE KEY UPDATE full_name = VALUES(full_name);

-- ============================================================
-- TABLE: integrity_pledges
-- Purpose: Every active/pending student must have agreed to
--          the pledge before they could register.
-- ============================================================
INSERT INTO integrity_pledges (user_id, agreed, agreed_at)
VALUES
  (2,  1, '2025-09-01 10:00:00'),
  (9,  1, '2025-09-05 14:30:00'),
  (13, 1, '2025-10-02 09:45:00'),
  (20, 1, '2026-01-10 09:01:00'),
  (21, 1, '2026-01-12 10:31:00'),
  (22, 1, '2026-05-14 08:16:00'),
  (23, 1, '2026-02-01 11:01:00')
ON DUPLICATE KEY UPDATE agreed = VALUES(agreed);

-- ============================================================
-- TABLE: faculties
-- Purpose: Top-level category hierarchy — 5 distinct faculties
--          so the browser filter shows real variety.
-- ============================================================
INSERT INTO faculties (faculty_id, faculty_name, created_at)
VALUES
  (1, 'Computing & Technology',       '2025-08-01 08:00:00'),
  (2, 'Business & Management',        '2025-08-01 08:05:00'),
  (3, 'Health Sciences',              '2025-08-01 08:10:00'),
  (4, 'Engineering & Mathematics',    '2025-08-01 08:15:00'),
  (5, 'Social Sciences & Humanities', '2025-08-01 08:20:00')
ON DUPLICATE KEY UPDATE faculty_name = VALUES(faculty_name);

-- ============================================================
-- TABLE: subjects
-- Purpose: Middle-level categories — 2-3 subjects per faculty
--          so the cascading filter in the browser works.
-- ============================================================
INSERT INTO subjects (subject_id, faculty_id, subject_name, created_at)
VALUES
  -- Computing & Technology (faculty 1)
  (1,  1, 'Advanced Programming & Technologies', '2025-08-02 09:00:00'),
  (2,  1, 'Database Systems',                    '2025-08-02 09:05:00'),
  (3,  1, 'Web Development',                     '2025-08-02 09:10:00'),
  (4,  1, 'Cybersecurity Fundamentals',          '2025-08-02 09:15:00'),
  -- Business & Management (faculty 2)
  (5,  2, 'Principles of Marketing',             '2025-08-02 09:20:00'),
  (6,  2, 'Financial Accounting',                '2025-08-02 09:25:00'),
  (7,  2, 'Organisational Behaviour',            '2025-08-02 09:30:00'),
  -- Health Sciences (faculty 3)
  (8,  3, 'Human Anatomy & Physiology',          '2025-08-02 09:35:00'),
  (9,  3, 'Clinical Pharmacology',               '2025-08-02 09:40:00'),
  -- Engineering & Mathematics (faculty 4)
  (10, 4, 'Calculus & Linear Algebra',           '2025-08-02 09:45:00'),
  (11, 4, 'Electrical Circuit Theory',           '2025-08-02 09:50:00'),
  -- Social Sciences & Humanities (faculty 5)
  (12, 5, 'Introduction to Psychology',          '2025-08-02 09:55:00'),
  (13, 5, 'Research Methods',                    '2025-08-02 10:00:00')
ON DUPLICATE KEY UPDATE subject_name = VALUES(subject_name);

-- ============================================================
-- TABLE: topics
-- Purpose: Bottom-level categories — 2-3 topics per subject.
--          Resources are tagged to a topic, so these must exist
--          before any resource can be inserted.
-- ============================================================
INSERT INTO topics (topic_id, subject_id, topic_name, created_at)
VALUES
  -- Advanced Programming & Technologies (subject 1)
  (1,  1, 'MVC Architecture',              '2025-08-03 09:00:00'),
  (2,  1, 'Design Patterns',               '2025-08-03 09:05:00'),
  (3,  1, 'Java Servlets & JSP',           '2025-08-03 09:10:00'),
  -- Database Systems (subject 2)
  (4,  2, 'SQL Query Optimisation',        '2025-08-03 09:15:00'),
  (5,  2, 'Normalisation & ER Modelling',  '2025-08-03 09:20:00'),
  -- Web Development (subject 3)
  (6,  3, 'HTML5 & CSS3',                  '2025-08-03 09:25:00'),
  (7,  3, 'RESTful API Design',            '2025-08-03 09:30:00'),
  -- Cybersecurity Fundamentals (subject 4)
  (8,  4, 'Cryptography Basics',           '2025-08-03 09:35:00'),
  (9,  4, 'Network Security Protocols',    '2025-08-03 09:40:00'),
  -- Principles of Marketing (subject 5)
  (10, 5, 'Consumer Behaviour',            '2025-08-03 09:45:00'),
  (11, 5, 'Digital Marketing Strategy',   '2025-08-03 09:50:00'),
  -- Financial Accounting (subject 6)
  (12, 6, 'Double-Entry Bookkeeping',      '2025-08-03 09:55:00'),
  (13, 6, 'Financial Statement Analysis', '2025-08-03 10:00:00'),
  -- Human Anatomy & Physiology (subject 8)
  (14, 8, 'Cardiovascular System',         '2025-08-03 10:05:00'),
  (15, 8, 'Nervous System Overview',       '2025-08-03 10:10:00'),
  -- Calculus & Linear Algebra (subject 10)
  (16, 10, 'Differential Equations',       '2025-08-03 10:15:00'),
  (17, 10, 'Matrix Operations',            '2025-08-03 10:20:00'),
  -- Introduction to Psychology (subject 12)
  (18, 12, 'Cognitive Psychology',         '2025-08-03 10:25:00'),
  (19, 12, 'Developmental Psychology',     '2025-08-03 10:30:00')
ON DUPLICATE KEY UPDATE topic_name = VALUES(topic_name);

-- ============================================================
-- TABLE: resources
-- Purpose: Core content of the platform. Covers every status
--          value (pending, under_review, approved, rejected)
--          and every resource_type enum value. Resources are
--          spread across all three existing students (2, 9, 13)
--          plus the two new active students (20, 21) so each
--          person has personalised dashboard data.
--          file_path uses a placeholder path — no real file
--          is needed for the demo UI to render correctly.
-- ============================================================
INSERT INTO resources (resource_id, user_id, topic_id, title, description, file_path, resource_type, status, self_declaration, upload_date)
VALUES
  -- ── Student id=2 (approved resources — boosts reputation score) ──
  (1,  2,  1, 'Complete MVC Architecture Notes — Week 3',
      'Comprehensive notes covering the Model-View-Controller pattern with Java EE examples, servlet lifecycle, and JSP integration.',
      'uploads/resources/mvc_notes_week3.pdf', 'notes', 'approved', 1, '2026-02-10 10:00:00'),

  (2,  2,  3, 'Java Servlets & JSP Cheat Sheet',
      'A concise two-page reference covering servlet annotations, request/response lifecycle, session management, and JSTL tags.',
      'uploads/resources/servlet_cheatsheet.pdf', 'summary', 'approved', 1, '2026-02-18 14:30:00'),

  (3,  2,  4, 'SQL Query Optimisation — Past Paper 2024',
      'Full past examination paper from the 2024 Database Systems module with model answers and examiner commentary.',
      'uploads/resources/sql_past_paper_2024.pdf', 'past_paper', 'approved', 1, '2026-03-05 09:15:00'),

  (4,  2,  2, 'Design Patterns Revision Guide',
      'Covers Singleton, Factory, Observer, Strategy, and Decorator patterns with UML diagrams and Java code examples.',
      'uploads/resources/design_patterns_revision.pdf', 'revision_guide', 'approved', 1, '2026-03-20 11:00:00'),

  -- ── Student id=2 (pending — shows in admin pipeline) ──
  (5,  2,  5, 'Database Normalisation — 1NF to BCNF Explained',
      'Step-by-step walkthrough of normalisation from first normal form through Boyce-Codd normal form with worked examples.',
      'uploads/resources/normalisation_guide.pdf', 'notes', 'pending', 1, '2026-05-10 08:00:00'),

  -- ── Student id=9 (approved resources) ──
  (6,  9,  6, 'HTML5 Semantic Elements Reference Sheet',
      'Visual reference card for all HTML5 semantic elements including article, section, aside, nav, and figure with usage examples.',
      'uploads/resources/html5_reference.pdf', 'summary', 'approved', 1, '2026-01-25 13:00:00'),

  (7,  9,  7, 'RESTful API Design Best Practices',
      'Detailed notes on REST principles, HTTP verbs, status codes, versioning strategies, and authentication patterns.',
      'uploads/resources/rest_api_notes.pdf', 'notes', 'approved', 1, '2026-02-14 10:45:00'),

  (8,  9, 10, 'Consumer Behaviour — Exam Revision Pack',
      'Revision pack covering buyer decision models, psychological influences, cultural factors, and past exam questions.',
      'uploads/resources/consumer_behaviour_revision.pdf', 'revision_guide', 'approved', 1, '2026-03-01 15:20:00'),

  -- ── Student id=9 (under_review — shows in admin pipeline) ──
  (9,  9,  8, 'Introduction to Cryptography — Lecture Notes',
      'Notes from weeks 1-6 covering symmetric and asymmetric encryption, hashing algorithms, and digital signatures.',
      'uploads/resources/cryptography_notes.pdf', 'notes', 'under_review', 1, '2026-05-08 09:30:00'),

  -- ── Student id=9 (rejected — shows rejection in student uploads) ──
  (10, 9, 11, 'Digital Marketing Strategy Overview',
      'Brief overview of digital marketing channels.',
      'uploads/resources/digital_marketing_brief.pdf', 'other', 'rejected', 0, '2026-04-02 16:00:00'),

  -- ── Student id=13 (approved resources) ──
  (11, 13, 12, 'Double-Entry Bookkeeping — Worked Examples',
      'Twenty fully worked bookkeeping exercises covering journals, ledgers, trial balance, and error correction.',
      'uploads/resources/bookkeeping_examples.pdf', 'notes', 'approved', 1, '2026-02-20 11:30:00'),

  (12, 13, 14, 'Cardiovascular System — Anatomy Summary',
      'Illustrated summary of heart anatomy, cardiac cycle, blood pressure regulation, and common pathologies.',
      'uploads/resources/cardiovascular_summary.pdf', 'summary', 'approved', 1, '2026-03-10 14:00:00'),

  (13, 13, 16, 'Differential Equations — Past Paper 2023',
      'Past examination paper with full worked solutions for first and second order differential equations.',
      'uploads/resources/diff_equations_2023.pdf', 'past_paper', 'approved', 1, '2026-03-25 10:00:00'),

  -- ── Student id=13 (pending — shows in admin pipeline) ──
  (14, 13, 18, 'Cognitive Psychology — Lecture Notes Weeks 1-8',
      'Comprehensive notes covering attention, memory models, perception, language processing, and problem solving.',
      'uploads/resources/cognitive_psych_notes.pdf', 'notes', 'pending', 1, '2026-05-12 09:00:00'),

  -- ── Student id=20 (approved — contributes to analytics top contributors) ──
  (15, 20,  9, 'Network Security Protocols — Study Guide',
      'Covers TLS/SSL, IPSec, SSH, and firewall configuration with practical lab notes and exam tips.',
      'uploads/resources/network_security_guide.pdf', 'revision_guide', 'approved', 1, '2026-02-28 12:00:00'),

  (16, 20, 13, 'Financial Statement Analysis — Worked Cases',
      'Three full company case studies analysing income statements, balance sheets, and cash flow statements.',
      'uploads/resources/financial_analysis_cases.pdf', 'other', 'approved', 1, '2026-03-15 09:45:00'),

  -- ── Student id=21 (approved) ──
  (17, 21, 17, 'Matrix Operations — Quick Reference',
      'Concise reference covering matrix addition, multiplication, determinants, inverses, and eigenvalues.',
      'uploads/resources/matrix_operations_ref.pdf', 'summary', 'approved', 1, '2026-03-08 13:30:00'),

  -- ── Student id=21 (under_review) ──
  (18, 21, 19, 'Developmental Psychology — Piaget & Vygotsky',
      'Comparative analysis of Piaget and Vygotsky developmental theories with evaluation and exam application tips.',
      'uploads/resources/developmental_psych.pdf', 'notes', 'under_review', 1, '2026-05-15 10:00:00'),

  -- ── Student id=21 (pending — for pipeline demo) ──
  (19, 21, 15, 'Nervous System — Revision Notes',
      'Revision notes covering central and peripheral nervous systems, neurotransmitters, and reflex arcs.',
      'uploads/resources/nervous_system_revision.pdf', 'revision_guide', 'pending', 1, '2026-05-17 08:30:00')
ON DUPLICATE KEY UPDATE title = VALUES(title);

-- ============================================================
-- TABLE: moderation_logs
-- Purpose: Admin audit trail — covers all four action ENUM
--          values so the audit trail page is populated.
--          All actions performed by admin id=1.
-- ============================================================
INSERT INTO moderation_logs (log_id, resource_id, admin_id, action, note, actioned_at)
VALUES
  (1,  1,  1, 'approved',       'Well-structured notes with clear examples. Approved for publication.', '2026-02-12 09:00:00'),
  (2,  2,  1, 'approved',       'Accurate and concise cheat sheet. Good for student reference.', '2026-02-20 10:30:00'),
  (3,  3,  1, 'approved',       'Verified against official past paper archive. Approved.', '2026-03-07 11:00:00'),
  (4,  4,  1, 'approved',       'Comprehensive revision guide with correct UML diagrams.', '2026-03-22 14:00:00'),
  (5,  6,  1, 'approved',       'Accurate HTML5 reference. Approved for the browse library.', '2026-01-27 09:15:00'),
  (6,  7,  1, 'approved',       'REST notes are thorough and well-referenced.', '2026-02-16 10:00:00'),
  (7,  8,  1, 'approved',       'Good revision pack with relevant past questions.', '2026-03-03 13:00:00'),
  (8,  10, 1, 'rejected',       'Content is too brief and lacks academic references. Please resubmit with full citations.', '2026-04-05 09:00:00'),
  (9,  11, 1, 'approved',       'Worked examples are accurate and well-presented.', '2026-02-22 11:00:00'),
  (10, 12, 1, 'approved',       'Illustrations are clear and medically accurate.', '2026-03-12 14:30:00'),
  (11, 13, 1, 'approved',       'Past paper verified against module records. Approved.', '2026-03-27 10:00:00'),
  (12, 15, 1, 'approved',       'Comprehensive security guide. Approved.', '2026-03-02 09:00:00'),
  (13, 16, 1, 'approved',       'Case studies are well-analysed and academically sound.', '2026-03-17 10:00:00'),
  (14, 17, 1, 'approved',       'Accurate mathematical reference. Approved.', '2026-03-10 11:00:00'),
  -- flag_upheld and flag_dismissed will be added after flags table
  (15, 7,  1, 'flag_dismissed', 'Reviewed the flagged resource. Content is original and properly cited. Flag dismissed.', '2026-04-20 14:00:00'),
  (16, 1,  1, 'flag_upheld',    'Second review confirmed significant overlap with published material. Resource removed.', '2026-05-01 10:00:00')
ON DUPLICATE KEY UPDATE note = VALUES(note);

-- ============================================================
-- TABLE: flags
-- Purpose: Community plagiarism reports. Covers all three
--          status values (open, upheld, dismissed). Multiple
--          students flag the same resource to demonstrate the
--          "most flagged resources" analytics widget.
-- ============================================================
INSERT INTO flags (flag_id, resource_id, flagged_by, reason, status, created_at)
VALUES
  -- open flags (visible in admin dashboard and flags management)
  (1,  3,  9,  'This past paper appears to be identical to one posted on a public exam-sharing website. I believe it was not the student''s original work.', 'open', '2026-04-10 10:00:00'),
  (2,  3,  13, 'I found the same document on Scribd uploaded by a different user two years ago. Possible plagiarism.', 'open', '2026-04-11 11:30:00'),
  (3,  11, 20, 'Several of the worked examples in this resource match a textbook solution manual word-for-word without attribution.', 'open', '2026-04-15 09:00:00'),
  (4,  12, 21, 'The cardiovascular diagrams appear to be scanned directly from a copyrighted anatomy atlas without permission.', 'open', '2026-04-18 14:00:00'),
  (5,  15, 2,  'The network security content closely mirrors a published Cisco study guide. Attribution is missing.', 'open', '2026-05-02 10:30:00'),

  -- dismissed flag (admin reviewed and kept the resource)
  (6,  7,  13, 'I think this REST API document was copied from an online tutorial.', 'dismissed', '2026-04-19 09:00:00'),

  -- upheld flag (admin agreed — resource was removed from public view)
  (7,  1,  20, 'This MVC notes document is a near-verbatim copy of a blog post I can link to. The student did not write this.', 'upheld', '2026-04-28 15:00:00')
ON DUPLICATE KEY UPDATE reason = VALUES(reason);

-- ============================================================
-- TABLE: collections
-- Purpose: Personal study folders for each student. Each
--          existing student gets 2-3 named collections so the
--          collections page and the "saved resources" counter
--          on the dashboard show real data.
-- ============================================================
INSERT INTO collections (collection_id, user_id, collection_name, created_at)
VALUES
  -- Student id=2
  (1,  2,  'Exam Revision — Semester 2',  '2026-03-01 10:00:00'),
  (2,  2,  'Java & Web Dev Resources',    '2026-03-15 11:00:00'),
  -- Student id=9
  (3,  9,  'Marketing Module Prep',       '2026-02-10 09:00:00'),
  (4,  9,  'Security & Crypto Notes',     '2026-03-20 14:00:00'),
  (5,  9,  'Saved for Later',             '2026-04-01 08:30:00'),
  -- Student id=13
  (6,  13, 'Accounting Coursework',       '2026-02-25 10:00:00'),
  (7,  13, 'Science & Maths',             '2026-03-12 13:00:00'),
  -- Student id=20
  (8,  20, 'Cybersecurity Study Pack',    '2026-03-05 09:00:00'),
  -- Student id=21
  (9,  21, 'Psychology & Maths',          '2026-03-10 10:00:00')
ON DUPLICATE KEY UPDATE collection_name = VALUES(collection_name);

-- ============================================================
-- TABLE: collection_items
-- Purpose: Resources saved inside each collection. Gives each
--          student a non-empty library and demonstrates the
--          many-to-many relationship between resources and
--          collections.
-- ============================================================
INSERT INTO collection_items (item_id, collection_id, resource_id, added_at)
VALUES
  -- Student id=2 — Exam Revision collection
  (1,  1,  3,  '2026-03-02 10:00:00'),
  (2,  1,  13, '2026-03-02 10:05:00'),
  (3,  1,  17, '2026-03-02 10:10:00'),
  -- Student id=2 — Java & Web Dev collection
  (4,  2,  6,  '2026-03-16 09:00:00'),
  (5,  2,  7,  '2026-03-16 09:05:00'),
  -- Student id=9 — Marketing Module Prep
  (6,  3,  8,  '2026-02-11 10:00:00'),
  (7,  3,  16, '2026-02-11 10:05:00'),
  -- Student id=9 — Security & Crypto Notes
  (8,  4,  15, '2026-03-21 14:00:00'),
  -- Student id=9 — Saved for Later
  (9,  5,  11, '2026-04-02 08:30:00'),
  (10, 5,  12, '2026-04-02 08:35:00'),
  -- Student id=13 — Accounting Coursework
  (11, 6,  11, '2026-02-26 10:00:00'),
  (12, 6,  16, '2026-02-26 10:05:00'),
  -- Student id=13 — Science & Maths
  (13, 7,  12, '2026-03-13 13:00:00'),
  (14, 7,  13, '2026-03-13 13:05:00'),
  (15, 7,  17, '2026-03-13 13:10:00'),
  -- Student id=20 — Cybersecurity Study Pack
  (16, 8,  15, '2026-03-06 09:00:00'),
  (17, 8,  7,  '2026-03-06 09:05:00'),
  -- Student id=21 — Psychology & Maths
  (18, 9,  17, '2026-03-11 10:00:00'),
  (19, 9,  12, '2026-03-11 10:05:00')
ON DUPLICATE KEY UPDATE added_at = VALUES(added_at);

-- ============================================================
-- TABLE: ratings
-- Purpose: Star ratings (1-5) on approved resources. Multiple
--          students rate the same resource so the average score
--          widget on the resource detail page shows real data.
--          The UNIQUE constraint (resource_id, user_id) means
--          each student can only rate a resource once.
-- ============================================================
INSERT INTO ratings (rating_id, resource_id, user_id, score, rated_at)
VALUES
  -- Resource 2 (Servlet Cheat Sheet) — rated by 3 students → avg 4.3
  (1,  2,  9,  5, '2026-02-22 10:00:00'),
  (2,  2,  13, 4, '2026-02-23 11:00:00'),
  (3,  2,  20, 4, '2026-02-24 09:30:00'),

  -- Resource 3 (SQL Past Paper) — rated by 4 students → avg 4.5
  (4,  3,  2,  5, '2026-03-10 10:00:00'),
  (5,  3,  9,  4, '2026-03-11 11:00:00'),
  (6,  3,  20, 5, '2026-03-12 09:00:00'),
  (7,  3,  21, 4, '2026-03-13 14:00:00'),

  -- Resource 6 (HTML5 Reference) — rated by 2 students → avg 4.5
  (8,  6,  2,  5, '2026-02-01 10:00:00'),
  (9,  6,  13, 4, '2026-02-02 11:00:00'),

  -- Resource 7 (REST API Notes) — rated by 3 students → avg 4.7
  (10, 7,  2,  5, '2026-02-18 10:00:00'),
  (11, 7,  9,  5, '2026-02-19 11:00:00'),
  (12, 7,  20, 4, '2026-02-20 09:00:00'),

  -- Resource 8 (Consumer Behaviour) — rated by 2 students → avg 4.0
  (13, 8,  2,  4, '2026-03-05 10:00:00'),
  (14, 8,  21, 4, '2026-03-06 11:00:00'),

  -- Resource 11 (Bookkeeping) — rated by 3 students → avg 4.3
  (15, 11, 2,  4, '2026-02-28 10:00:00'),
  (16, 11, 9,  5, '2026-03-01 11:00:00'),
  (17, 11, 21, 4, '2026-03-02 09:00:00'),

  -- Resource 12 (Cardiovascular) — rated by 2 students → avg 4.5
  (18, 12, 2,  5, '2026-03-15 10:00:00'),
  (19, 12, 9,  4, '2026-03-16 11:00:00'),

  -- Resource 13 (Differential Equations Past Paper) — rated by 3 students → avg 4.7
  (20, 13, 2,  5, '2026-03-30 10:00:00'),
  (21, 13, 9,  5, '2026-03-31 11:00:00'),
  (22, 13, 20, 4, '2026-04-01 09:00:00'),

  -- Resource 15 (Network Security Guide) — rated by 2 students → avg 4.5
  (23, 15, 2,  5, '2026-03-05 10:00:00'),
  (24, 15, 13, 4, '2026-03-06 11:00:00'),

  -- Resource 17 (Matrix Operations) — rated by 2 students → avg 4.0
  (25, 17, 2,  4, '2026-03-12 10:00:00'),
  (26, 17, 13, 4, '2026-03-13 11:00:00')
ON DUPLICATE KEY UPDATE score = VALUES(score);

-- ============================================================
SET FOREIGN_KEY_CHECKS = 1;
-- ============================================================
-- END OF SEED FILE
-- ============================================================
