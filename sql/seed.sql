-- ============================================================
--  ScholarShare — Complete Fresh Seed Data
--  Run this on a completely empty database (after schema.sql).
--
--  DEMO LOGIN CREDENTIALS
--  ─────────────────────────────────────────────────────────
--  ADMIN
--    Email    : admin@scholarshare.ac.uk
--    Password : Admin@123
--
--  STUDENTS (all passwords: Student@123)
--    sarah.johnson@scholarshare.ac.uk   (active)
--    marcus.osei@scholarshare.ac.uk     (active)
--    priya.sharma@scholarshare.ac.uk    (active)
--    james.whitfield@scholarshare.ac.uk (active)
--    liu.yang@scholarshare.ac.uk        (active)
--    fatima.malik@scholarshare.ac.uk    (pending  — admin approval demo)
--    tom.bradley@scholarshare.ac.uk     (suspended — admin demo)
--
--  BCrypt hashes (cost=10, generated fresh):
--    Admin@123   → $2a$10$Amex2wSj.V/2Eolg0NQQN.u9zSqjsNRauGkmfIvk88wHIS.0QXSAC
--    Student@123 → $2a$10$MmQJ1yYmTPf3/J3o7ilpX.1MjqiV1925TsHZjhCaDX5Ay0MN3s65K
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================
-- TABLE: users
-- Purpose: One admin + five active students + one pending +
--          one suspended — covers every role/status combo.
-- ============================================================
INSERT INTO users
    (user_id, full_name, email, phone, password, role, status, profile_pic, created_at)
VALUES
-- Admin
(1, 'Dr. Alan Carter',
    'admin@scholarshare.ac.uk', '07700100001',
    '$2a$10$Amex2wSj.V/2Eolg0NQQN.u9zSqjsNRauGkmfIvk88wHIS.0QXSAC',
    'admin', 'active', NULL, '2025-08-01 08:00:00'),

-- Active students
(2, 'Sarah Johnson',
    'sarah.johnson@scholarshare.ac.uk', '07700100002',
    '$2a$10$MmQJ1yYmTPf3/J3o7ilpX.1MjqiV1925TsHZjhCaDX5Ay0MN3s65K',
    'student', 'active', NULL, '2025-09-03 09:10:00'),

(3, 'Marcus Osei',
    'marcus.osei@scholarshare.ac.uk', '07700100003',
    '$2a$10$MmQJ1yYmTPf3/J3o7ilpX.1MjqiV1925TsHZjhCaDX5Ay0MN3s65K',
    'student', 'active', NULL, '2025-09-05 10:30:00'),

(4, 'Priya Sharma',
    'priya.sharma@scholarshare.ac.uk', '07700100004',
    '$2a$10$MmQJ1yYmTPf3/J3o7ilpX.1MjqiV1925TsHZjhCaDX5Ay0MN3s65K',
    'student', 'active', NULL, '2025-09-08 11:00:00'),

(5, 'James Whitfield',
    'james.whitfield@scholarshare.ac.uk', '07700100005',
    '$2a$10$MmQJ1yYmTPf3/J3o7ilpX.1MjqiV1925TsHZjhCaDX5Ay0MN3s65K',
    'student', 'active', NULL, '2025-09-10 14:00:00'),

(6, 'Liu Yang',
    'liu.yang@scholarshare.ac.uk', '07700100006',
    '$2a$10$MmQJ1yYmTPf3/J3o7ilpX.1MjqiV1925TsHZjhCaDX5Ay0MN3s65K',
    'student', 'active', NULL, '2025-09-12 09:45:00'),

-- Pending — shows in admin user-approvals panel
(7, 'Fatima Malik',
    'fatima.malik@scholarshare.ac.uk', '07700100007',
    '$2a$10$MmQJ1yYmTPf3/J3o7ilpX.1MjqiV1925TsHZjhCaDX5Ay0MN3s65K',
    'student', 'pending', NULL, '2026-05-18 08:30:00'),

-- Suspended — shows in admin user management
(8, 'Tom Bradley',
    'tom.bradley@scholarshare.ac.uk', '07700100008',
    '$2a$10$MmQJ1yYmTPf3/J3o7ilpX.1MjqiV1925TsHZjhCaDX5Ay0MN3s65K',
    'student', 'suspended', NULL, '2025-10-01 10:00:00')

ON DUPLICATE KEY UPDATE full_name = VALUES(full_name);

-- ============================================================
-- TABLE: integrity_pledges
-- Purpose: Every student who registered agreed to the pledge.
--          Admin (id=1) has no pledge — admins are not students.
-- ============================================================
INSERT INTO integrity_pledges (user_id, agreed, agreed_at)
VALUES
(2, 1, '2025-09-03 09:11:00'),
(3, 1, '2025-09-05 10:31:00'),
(4, 1, '2025-09-08 11:01:00'),
(5, 1, '2025-09-10 14:01:00'),
(6, 1, '2025-09-12 09:46:00'),
(7, 1, '2026-05-18 08:31:00'),
(8, 1, '2025-10-01 10:01:00')
ON DUPLICATE KEY UPDATE agreed = VALUES(agreed);

-- ============================================================
-- TABLE: faculties
-- Purpose: Five distinct top-level academic divisions so the
--          browser filter dropdown shows real variety.
-- ============================================================
INSERT INTO faculties (faculty_id, faculty_name, created_at)
VALUES
(1, 'Computing & Technology',       '2025-08-01 09:00:00'),
(2, 'Business & Management',        '2025-08-01 09:05:00'),
(3, 'Health Sciences',              '2025-08-01 09:10:00'),
(4, 'Engineering & Mathematics',    '2025-08-01 09:15:00'),
(5, 'Social Sciences & Humanities', '2025-08-01 09:20:00')
ON DUPLICATE KEY UPDATE faculty_name = VALUES(faculty_name);

-- ============================================================
-- TABLE: subjects
-- Purpose: 2-3 subjects per faculty so the cascading
--          Faculty → Subject → Topic filter chain works.
-- ============================================================
INSERT INTO subjects (subject_id, faculty_id, subject_name, created_at)
VALUES
-- Computing & Technology
(1,  1, 'Advanced Programming & Technologies', '2025-08-02 09:00:00'),
(2,  1, 'Database Systems',                    '2025-08-02 09:05:00'),
(3,  1, 'Web Development',                     '2025-08-02 09:10:00'),
(4,  1, 'Cybersecurity Fundamentals',          '2025-08-02 09:15:00'),
-- Business & Management
(5,  2, 'Principles of Marketing',             '2025-08-02 09:20:00'),
(6,  2, 'Financial Accounting',                '2025-08-02 09:25:00'),
(7,  2, 'Organisational Behaviour',            '2025-08-02 09:30:00'),
-- Health Sciences
(8,  3, 'Human Anatomy & Physiology',          '2025-08-02 09:35:00'),
(9,  3, 'Clinical Pharmacology',               '2025-08-02 09:40:00'),
-- Engineering & Mathematics
(10, 4, 'Calculus & Linear Algebra',           '2025-08-02 09:45:00'),
(11, 4, 'Electrical Circuit Theory',           '2025-08-02 09:50:00'),
-- Social Sciences & Humanities
(12, 5, 'Introduction to Psychology',          '2025-08-02 09:55:00'),
(13, 5, 'Research Methods',                    '2025-08-02 10:00:00')
ON DUPLICATE KEY UPDATE subject_name = VALUES(subject_name);

-- ============================================================
-- TABLE: topics
-- Purpose: 2-3 topics per subject — resources are tagged to
--          a topic, so these must exist before any resource.
-- ============================================================
INSERT INTO topics (topic_id, subject_id, topic_name, created_at)
VALUES
-- Advanced Programming & Technologies (subject 1)
(1,  1, 'MVC Architecture',             '2025-08-03 09:00:00'),
(2,  1, 'Design Patterns',              '2025-08-03 09:05:00'),
(3,  1, 'Java Servlets & JSP',          '2025-08-03 09:10:00'),
-- Database Systems (subject 2)
(4,  2, 'SQL Query Optimisation',       '2025-08-03 09:15:00'),
(5,  2, 'Normalisation & ER Modelling', '2025-08-03 09:20:00'),
-- Web Development (subject 3)
(6,  3, 'HTML5 & CSS3',                 '2025-08-03 09:25:00'),
(7,  3, 'RESTful API Design',           '2025-08-03 09:30:00'),
-- Cybersecurity Fundamentals (subject 4)
(8,  4, 'Cryptography Basics',          '2025-08-03 09:35:00'),
(9,  4, 'Network Security Protocols',   '2025-08-03 09:40:00'),
-- Principles of Marketing (subject 5)
(10, 5, 'Consumer Behaviour',           '2025-08-03 09:45:00'),
(11, 5, 'Digital Marketing Strategy',   '2025-08-03 09:50:00'),
-- Financial Accounting (subject 6)
(12, 6, 'Double-Entry Bookkeeping',     '2025-08-03 09:55:00'),
(13, 6, 'Financial Statement Analysis', '2025-08-03 10:00:00'),
-- Human Anatomy & Physiology (subject 8)
(14, 8, 'Cardiovascular System',        '2025-08-03 10:05:00'),
(15, 8, 'Nervous System Overview',      '2025-08-03 10:10:00'),
-- Calculus & Linear Algebra (subject 10)
(16, 10, 'Differential Equations',      '2025-08-03 10:15:00'),
(17, 10, 'Matrix Operations',           '2025-08-03 10:20:00'),
-- Introduction to Psychology (subject 12)
(18, 12, 'Cognitive Psychology',        '2025-08-03 10:25:00'),
(19, 12, 'Developmental Psychology',    '2025-08-03 10:30:00'),
-- Research Methods (subject 13)
(20, 13, 'Quantitative Research',       '2025-08-03 10:35:00')
ON DUPLICATE KEY UPDATE topic_name = VALUES(topic_name);

-- ============================================================
-- TABLE: resources
-- Purpose: Core content. Covers every status (pending,
--          under_review, approved, rejected) and every
--          resource_type enum value (notes, past_paper,
--          summary, revision_guide, other). Each active
--          student has a mix of statuses so their personal
--          dashboard, uploads page, and reputation score
--          all show meaningful data.
--
--  Student upload summary:
--    Sarah   (id=2) — 4 approved, 1 pending
--    Marcus  (id=3) — 3 approved, 1 under_review, 1 rejected
--    Priya   (id=4) — 3 approved, 1 pending
--    James   (id=5) — 2 approved, 1 under_review
--    Liu     (id=6) — 2 approved, 1 pending
-- ============================================================
INSERT INTO resources
    (resource_id, user_id, topic_id, title, description,
     file_path, resource_type, status, self_declaration, upload_date)
VALUES

-- ── Sarah Johnson (id=2) ──────────────────────────────────
(1,  2,  1,
 'MVC Architecture — Complete Lecture Notes',
 'Comprehensive notes covering the Model-View-Controller pattern with Java EE examples, servlet lifecycle diagrams, and JSP integration walkthroughs. Includes annotated code samples from weeks 1-5.',
 'uploads/resources/mvc_notes_sarah.pdf',
 'notes', 'approved', 1, '2025-10-05 10:00:00'),

(2,  2,  3,
 'Java Servlets & JSP — Quick Reference Cheat Sheet',
 'A concise two-page reference covering servlet annotations, request/response lifecycle, session management, JSTL tags, and EL expressions. Ideal for exam revision.',
 'uploads/resources/servlet_cheatsheet_sarah.pdf',
 'summary', 'approved', 1, '2025-10-18 14:30:00'),

(3,  2,  4,
 'SQL Query Optimisation — Past Paper 2024 with Model Answers',
 'Full past examination paper from the 2024 Database Systems module. Includes model answers, examiner commentary, and common pitfalls to avoid.',
 'uploads/resources/sql_past_paper_2024.pdf',
 'past_paper', 'approved', 1, '2025-11-02 09:15:00'),

(4,  2,  2,
 'Design Patterns Revision Guide — GoF Patterns',
 'Covers all 23 Gang of Four patterns with UML diagrams, Java code examples, and real-world use cases. Organised by Creational, Structural, and Behavioural categories.',
 'uploads/resources/design_patterns_sarah.pdf',
 'revision_guide', 'approved', 1, '2025-11-20 11:00:00'),

(5,  2,  5,
 'Database Normalisation — 1NF to BCNF Step-by-Step',
 'Step-by-step walkthrough of normalisation from first normal form through Boyce-Codd normal form. Includes ten fully worked examples with functional dependency analysis.',
 'uploads/resources/normalisation_sarah.pdf',
 'notes', 'pending', 1, '2026-05-10 08:00:00'),

-- ── Marcus Osei (id=3) ────────────────────────────────────
(6,  3,  6,
 'HTML5 Semantic Elements — Visual Reference Card',
 'Visual reference card for all HTML5 semantic elements including article, section, aside, nav, figure, and main. Each element includes a usage example and accessibility note.',
 'uploads/resources/html5_reference_marcus.pdf',
 'summary', 'approved', 1, '2025-10-12 13:00:00'),

(7,  3,  7,
 'RESTful API Design — Best Practices & Patterns',
 'Detailed notes on REST principles, HTTP verbs, status codes, versioning strategies, HATEOAS, and authentication patterns including JWT and OAuth2.',
 'uploads/resources/rest_api_marcus.pdf',
 'notes', 'approved', 1, '2025-11-08 10:45:00'),

(8,  3, 10,
 'Consumer Behaviour — Exam Revision Pack',
 'Revision pack covering buyer decision models, psychological influences, cultural factors, Maslow hierarchy, and ten past exam questions with mark-scheme answers.',
 'uploads/resources/consumer_behaviour_marcus.pdf',
 'revision_guide', 'approved', 1, '2025-12-01 15:20:00'),

(9,  3,  8,
 'Introduction to Cryptography — Weeks 1-6 Lecture Notes',
 'Notes from weeks 1-6 covering symmetric and asymmetric encryption, hashing algorithms (MD5, SHA-256), digital signatures, and PKI infrastructure.',
 'uploads/resources/cryptography_marcus.pdf',
 'notes', 'under_review', 1, '2026-05-08 09:30:00'),

(10, 3, 11,
 'Digital Marketing Strategy — Channel Overview',
 'Brief overview of digital marketing channels without academic references or analysis.',
 'uploads/resources/digital_marketing_marcus.pdf',
 'other', 'rejected', 0, '2026-03-15 16:00:00'),

-- ── Priya Sharma (id=4) ───────────────────────────────────
(11, 4, 12,
 'Double-Entry Bookkeeping — Twenty Worked Examples',
 'Twenty fully worked bookkeeping exercises covering journals, ledgers, trial balance, and error correction. Each exercise includes a step-by-step solution with explanatory notes.',
 'uploads/resources/bookkeeping_priya.pdf',
 'notes', 'approved', 1, '2025-10-25 11:30:00'),

(12, 4, 14,
 'Cardiovascular System — Illustrated Anatomy Summary',
 'Illustrated summary of heart anatomy, cardiac cycle, blood pressure regulation, ECG interpretation, and common pathologies including hypertension and arrhythmia.',
 'uploads/resources/cardiovascular_priya.pdf',
 'summary', 'approved', 1, '2025-11-14 14:00:00'),

(13, 4, 16,
 'Differential Equations — Past Paper 2023 with Full Solutions',
 'Past examination paper with full worked solutions for first and second order differential equations, including separable, linear, and homogeneous types.',
 'uploads/resources/diff_equations_priya.pdf',
 'past_paper', 'approved', 1, '2025-12-10 10:00:00'),

(14, 4, 18,
 'Cognitive Psychology — Lecture Notes Weeks 1-8',
 'Comprehensive notes covering attention models, working memory, long-term memory, perception, language processing, and problem-solving theories.',
 'uploads/resources/cognitive_psych_priya.pdf',
 'notes', 'pending', 1, '2026-05-12 09:00:00'),

-- ── James Whitfield (id=5) ────────────────────────────────
(15, 5,  9,
 'Network Security Protocols — Comprehensive Study Guide',
 'Covers TLS/SSL handshake process, IPSec tunnel and transport modes, SSH key exchange, firewall configuration, and IDS/IPS systems with practical lab notes.',
 'uploads/resources/network_security_james.pdf',
 'revision_guide', 'approved', 1, '2025-11-05 12:00:00'),

(16, 5, 13,
 'Financial Statement Analysis — Three Company Case Studies',
 'Three full company case studies analysing income statements, balance sheets, and cash flow statements. Includes ratio analysis, trend analysis, and investment recommendations.',
 'uploads/resources/financial_analysis_james.pdf',
 'other', 'approved', 1, '2025-12-03 09:45:00'),

(17, 5, 17,
 'Matrix Operations — Lecture Notes & Worked Problems',
 'Covers matrix addition, multiplication, determinants, inverses, eigenvalues, and eigenvectors. Includes fifteen worked problems with full solutions.',
 'uploads/resources/matrix_james.pdf',
 'notes', 'under_review', 1, '2026-05-15 10:00:00'),

-- ── Liu Yang (id=6) ───────────────────────────────────────
(18, 6, 19,
 'Developmental Psychology — Piaget vs Vygotsky Comparative Analysis',
 'Comparative analysis of Piaget and Vygotsky developmental theories covering cognitive stages, zone of proximal development, scaffolding, and evaluation for exam application.',
 'uploads/resources/developmental_psych_liu.pdf',
 'notes', 'approved', 1, '2025-11-22 13:30:00'),

(19, 6, 15,
 'Nervous System — Central & Peripheral Revision Notes',
 'Revision notes covering central and peripheral nervous systems, neurotransmitters, action potentials, reflex arcs, and common neurological disorders.',
 'uploads/resources/nervous_system_liu.pdf',
 'revision_guide', 'approved', 1, '2025-12-08 11:00:00'),

(20, 6, 20,
 'Quantitative Research Methods — Sampling & Statistics Guide',
 'Guide covering probability and non-probability sampling, descriptive statistics, hypothesis testing, t-tests, ANOVA, and chi-square with SPSS output interpretation.',
 'uploads/resources/quant_research_liu.pdf',
 'notes', 'pending', 1, '2026-05-17 08:30:00')

ON DUPLICATE KEY UPDATE title = VALUES(title);

-- ============================================================
-- TABLE: moderation_logs
-- Purpose: Admin audit trail — covers all four action ENUM
--          values: approved, rejected, flag_upheld,
--          flag_dismissed. All actions by admin id=1.
-- ============================================================
INSERT INTO moderation_logs
    (log_id, resource_id, admin_id, action, note, actioned_at)
VALUES
-- Approved resources
(1,  1,  1, 'approved',
 'Well-structured notes with clear diagrams and code examples. Approved for the library.',
 '2025-10-07 09:00:00'),
(2,  2,  1, 'approved',
 'Accurate and concise cheat sheet. Good for student reference.',
 '2025-10-20 10:30:00'),
(3,  3,  1, 'approved',
 'Verified against official past paper archive. Model answers are correct.',
 '2025-11-04 11:00:00'),
(4,  4,  1, 'approved',
 'Comprehensive revision guide with correct UML diagrams and working code.',
 '2025-11-22 14:00:00'),
(5,  6,  1, 'approved',
 'Accurate HTML5 reference with good accessibility notes. Approved.',
 '2025-10-14 09:15:00'),
(6,  7,  1, 'approved',
 'REST notes are thorough, well-referenced, and technically accurate.',
 '2025-11-10 10:00:00'),
(7,  8,  1, 'approved',
 'Good revision pack with relevant past questions and mark-scheme answers.',
 '2025-12-03 13:00:00'),
(8,  11, 1, 'approved',
 'Worked examples are accurate and clearly presented. Approved.',
 '2025-10-27 11:00:00'),
(9,  12, 1, 'approved',
 'Illustrations are clear and medically accurate. Approved.',
 '2025-11-16 14:30:00'),
(10, 13, 1, 'approved',
 'Past paper verified against module records. Solutions are correct.',
 '2025-12-12 10:00:00'),
(11, 15, 1, 'approved',
 'Comprehensive security guide with accurate protocol descriptions. Approved.',
 '2025-11-07 09:00:00'),
(12, 16, 1, 'approved',
 'Case studies are well-analysed with sound financial reasoning.',
 '2025-12-05 10:00:00'),
(13, 18, 1, 'approved',
 'Comparative analysis is balanced and academically rigorous.',
 '2025-11-24 11:00:00'),
(14, 19, 1, 'approved',
 'Accurate nervous system notes with good clinical relevance.',
 '2025-12-10 09:30:00'),

-- Rejected resource
(15, 10, 1, 'rejected',
 'Content is too brief and lacks academic references. No analysis or evaluation present. Please resubmit with full citations and critical discussion.',
 '2026-03-18 09:00:00'),

-- Flag dismissed — admin reviewed and kept the resource
(16, 7,  1, 'flag_dismissed',
 'Reviewed the flagged resource thoroughly. Content is original, properly cited, and does not match the alleged source. Flag dismissed.',
 '2026-04-22 14:00:00'),

-- Flag upheld — admin agreed, resource removed
(17, 3,  1, 'flag_upheld',
 'Second review confirmed significant textual overlap with a published exam-sharing website. Resource removed from the library pending further investigation.',
 '2026-05-03 10:00:00')

ON DUPLICATE KEY UPDATE note = VALUES(note);

-- ============================================================
-- TABLE: flags
-- Purpose: Community plagiarism reports. Covers all three
--          status values: open, upheld, dismissed.
--          Multiple students flag the same resource to
--          demonstrate the "most flagged" analytics widget.
-- ============================================================
INSERT INTO flags
    (flag_id, resource_id, flagged_by, reason, status, created_at)
VALUES
-- Open flags (visible in admin dashboard and flags management)
(1,  3,  3,
 'This past paper appears identical to one posted on a public exam-sharing website. I do not believe it was the uploader''s original work.',
 'open', '2026-04-12 10:00:00'),

(2,  3,  4,
 'I found the same document on Scribd uploaded by a different user two years ago. This looks like a direct copy.',
 'open', '2026-04-13 11:30:00'),

(3,  11, 5,
 'Several worked examples in this resource match a textbook solution manual word-for-word without any attribution or citation.',
 'open', '2026-04-18 09:00:00'),

(4,  12, 6,
 'The cardiovascular diagrams appear to be scanned directly from a copyrighted anatomy atlas without permission or attribution.',
 'open', '2026-04-20 14:00:00'),

(5,  15, 2,
 'The network security content closely mirrors a published Cisco study guide. The source is not cited anywhere in the document.',
 'open', '2026-05-04 10:30:00'),

(6,  1,  5,
 'I believe this MVC notes document contains sections copied from a well-known Java EE tutorial blog without attribution.',
 'open', '2026-05-10 09:00:00'),

-- Dismissed flag (admin reviewed and kept the resource)
(7,  7,  4,
 'I think this REST API document was copied from an online tutorial.',
 'dismissed', '2026-04-21 09:00:00'),

-- Upheld flag (admin agreed — resource removed)
(8,  3,  6,
 'This is a near-verbatim copy of a past paper that was leaked online. The student did not create this.',
 'upheld', '2026-05-01 15:00:00')

ON DUPLICATE KEY UPDATE reason = VALUES(reason);

-- ============================================================
-- TABLE: collections
-- Purpose: Personal study folders for each active student.
--          Each student gets 2-3 named collections so the
--          collections page and the "saved resources" counter
--          on the dashboard show real personalised data.
-- ============================================================
INSERT INTO collections
    (collection_id, user_id, collection_name, created_at)
VALUES
-- Sarah (id=2)
(1,  2, 'Exam Revision — Semester 1',   '2025-11-01 10:00:00'),
(2,  2, 'Java & Web Dev Resources',     '2025-11-15 11:00:00'),
(3,  2, 'Saved for Later',              '2025-12-01 09:00:00'),
-- Marcus (id=3)
(4,  3, 'Marketing Module Prep',        '2025-11-10 09:00:00'),
(5,  3, 'Security & Crypto Notes',      '2025-12-05 14:00:00'),
-- Priya (id=4)
(6,  4, 'Accounting Coursework',        '2025-11-20 10:00:00'),
(7,  4, 'Science & Maths',              '2025-12-08 13:00:00'),
-- James (id=5)
(8,  5, 'Cybersecurity Study Pack',     '2025-11-25 09:00:00'),
(9,  5, 'Finance & Business',           '2025-12-10 10:00:00'),
-- Liu (id=6)
(10, 6, 'Psychology & Research',        '2025-12-01 10:00:00'),
(11, 6, 'Health Sciences Notes',        '2025-12-15 11:00:00')
ON DUPLICATE KEY UPDATE collection_name = VALUES(collection_name);

-- ============================================================
-- TABLE: collection_items
-- Purpose: Resources saved inside each collection. Gives each
--          student a non-empty library and demonstrates the
--          many-to-many relationship. Only approved resources
--          are saved (students browse approved content).
-- ============================================================
INSERT INTO collection_items
    (item_id, collection_id, resource_id, added_at)
VALUES
-- Sarah — Exam Revision
(1,  1,  3,  '2025-11-02 10:00:00'),
(2,  1,  13, '2025-11-02 10:05:00'),
(3,  1,  4,  '2025-11-03 09:00:00'),
-- Sarah — Java & Web Dev
(4,  2,  6,  '2025-11-16 09:00:00'),
(5,  2,  7,  '2025-11-16 09:05:00'),
(6,  2,  2,  '2025-11-17 10:00:00'),
-- Sarah — Saved for Later
(7,  3,  15, '2025-12-02 08:30:00'),
(8,  3,  18, '2025-12-02 08:35:00'),
-- Marcus — Marketing Module Prep
(9,  4,  8,  '2025-11-11 10:00:00'),
(10, 4,  16, '2025-11-11 10:05:00'),
-- Marcus — Security & Crypto Notes
(11, 5,  15, '2025-12-06 14:00:00'),
(12, 5,  1,  '2025-12-06 14:05:00'),
-- Priya — Accounting Coursework
(13, 6,  11, '2025-11-21 10:00:00'),
(14, 6,  16, '2025-11-21 10:05:00'),
-- Priya — Science & Maths
(15, 7,  12, '2025-12-09 13:00:00'),
(16, 7,  13, '2025-12-09 13:05:00'),
(17, 7,  19, '2025-12-09 13:10:00'),
-- James — Cybersecurity Study Pack
(18, 8,  15, '2025-11-26 09:00:00'),
(19, 8,  7,  '2025-11-26 09:05:00'),
-- James — Finance & Business
(20, 9,  11, '2025-12-11 10:00:00'),
(21, 9,  16, '2025-12-11 10:05:00'),
-- Liu — Psychology & Research
(22, 10, 18, '2025-12-02 10:00:00'),
(23, 10, 8,  '2025-12-02 10:05:00'),
-- Liu — Health Sciences Notes
(24, 11, 12, '2025-12-16 11:00:00'),
(25, 11, 19, '2025-12-16 11:05:00')
ON DUPLICATE KEY UPDATE added_at = VALUES(added_at);

-- ============================================================
-- TABLE: ratings
-- Purpose: Star ratings (1-5) on approved resources.
--          Multiple students rate the same resource so the
--          average score widget on the resource detail page
--          shows a real calculated average.
--          UNIQUE constraint (resource_id, user_id) enforced —
--          each student rates each resource at most once.
-- ============================================================
INSERT INTO ratings
    (rating_id, resource_id, user_id, score, rated_at)
VALUES
-- Resource 1 (MVC Notes) — avg 4.5
(1,  1,  3, 5, '2025-10-10 10:00:00'),
(2,  1,  4, 4, '2025-10-11 11:00:00'),
(3,  1,  5, 5, '2025-10-12 09:00:00'),
(4,  1,  6, 4, '2025-10-13 14:00:00'),

-- Resource 2 (Servlet Cheat Sheet) — avg 4.3
(5,  2,  3, 5, '2025-10-22 10:00:00'),
(6,  2,  4, 4, '2025-10-23 11:00:00'),
(7,  2,  6, 4, '2025-10-24 09:30:00'),

-- Resource 4 (Design Patterns) — avg 4.7
(8,  4,  3, 5, '2025-11-25 10:00:00'),
(9,  4,  4, 5, '2025-11-26 11:00:00'),
(10, 4,  5, 4, '2025-11-27 09:00:00'),

-- Resource 6 (HTML5 Reference) — avg 4.5
(11, 6,  2, 5, '2025-10-15 10:00:00'),
(12, 6,  4, 4, '2025-10-16 11:00:00'),
(13, 6,  5, 5, '2025-10-17 09:00:00'),

-- Resource 7 (REST API Notes) — avg 4.7
(14, 7,  2, 5, '2025-11-12 10:00:00'),
(15, 7,  4, 5, '2025-11-13 11:00:00'),
(16, 7,  6, 4, '2025-11-14 09:00:00'),

-- Resource 8 (Consumer Behaviour) — avg 4.0
(17, 8,  2, 4, '2025-12-05 10:00:00'),
(18, 8,  5, 4, '2025-12-06 11:00:00'),
(19, 8,  6, 4, '2025-12-07 09:00:00'),

-- Resource 11 (Bookkeeping) — avg 4.3
(20, 11, 2, 4, '2025-10-29 10:00:00'),
(21, 11, 3, 5, '2025-10-30 11:00:00'),
(22, 11, 5, 4, '2025-10-31 09:00:00'),

-- Resource 12 (Cardiovascular) — avg 4.7
(23, 12, 2, 5, '2025-11-18 10:00:00'),
(24, 12, 3, 5, '2025-11-19 11:00:00'),
(25, 12, 5, 4, '2025-11-20 09:00:00'),

-- Resource 13 (Differential Equations Past Paper) — avg 4.7
(26, 13, 2, 5, '2025-12-14 10:00:00'),
(27, 13, 3, 5, '2025-12-15 11:00:00'),
(28, 13, 6, 4, '2025-12-16 09:00:00'),

-- Resource 15 (Network Security Guide) — avg 4.5
(29, 15, 2, 5, '2025-11-09 10:00:00'),
(30, 15, 4, 4, '2025-11-10 11:00:00'),
(31, 15, 6, 5, '2025-11-11 09:00:00'),

-- Resource 16 (Financial Analysis) — avg 4.0
(32, 16, 2, 4, '2025-12-07 10:00:00'),
(33, 16, 3, 4, '2025-12-08 11:00:00'),
(34, 16, 4, 4, '2025-12-09 09:00:00'),

-- Resource 18 (Developmental Psychology) — avg 4.3
(35, 18, 2, 4, '2025-11-26 10:00:00'),
(36, 18, 3, 5, '2025-11-27 11:00:00'),
(37, 18, 5, 4, '2025-11-28 09:00:00'),

-- Resource 19 (Nervous System) — avg 4.5
(38, 19, 2, 5, '2025-12-12 10:00:00'),
(39, 19, 3, 4, '2025-12-13 11:00:00'),
(40, 19, 4, 5, '2025-12-14 09:00:00')

ON DUPLICATE KEY UPDATE score = VALUES(score);

-- ============================================================
SET FOREIGN_KEY_CHECKS = 1;
-- ============================================================
-- END OF SEED FILE
-- ============================================================
