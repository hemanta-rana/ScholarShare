-- ============================================================
--  ScholarShare Database Schema

-- Create the database if it does not already exist
CREATE DATABASE IF NOT EXISTS scholarshare;

USE scholarshare;

-- ============================================================
--  TABLE 1: users
--
--  Stores EVERYONE who has an account on ScholarShare.
--  Both students and admins are stored here.
--  The 'role' column tells us which type of user they are.
--
--  A student goes through this journey:
--    1. Registers → status = 'pending'
--    2. Admin approves → status = 'active'
--    3. They can now log in and use the platform
-- ============================================================
CREATE TABLE users (

    -- A unique number automatically given to each user (1, 2, 3 ...)
    user_id        INT            NOT NULL AUTO_INCREMENT,

    -- The user's full name e.g. "Hemanta Rana"
    full_name      VARCHAR(100)   NOT NULL,

    -- Email address used to log in e.g. "sarah@gmail.com"
    -- UNIQUE means no two accounts can share the same email
    email          VARCHAR(100)   NOT NULL UNIQUE,

    -- Phone number used to detect duplicate accounts
    -- UNIQUE means no two accounts can share the same phone number
    phone          VARCHAR(15)    NOT NULL UNIQUE,

    -- The password stored as a secure hash (never plain text)
    -- BCrypt turns "Admin@123" into a long scrambled string
    password       VARCHAR(255)   NOT NULL,

    -- 'student' = a regular student using the platform
    -- 'admin'   = an administrator who manages the platform
    role           ENUM('student', 'admin')                   NOT NULL DEFAULT 'student',

    -- Whether this account is allowed to log in
    -- 'pending'   = just registered, waiting for admin approval
    -- 'active'    = approved, can log in normally
    -- 'suspended' = account has been disabled by admin
    status         ENUM('pending', 'active', 'suspended')     NOT NULL DEFAULT 'pending',

    -- A path to the user's profile picture file (optional)
    profile_pic    VARCHAR(255)   NULL DEFAULT NULL,

    -- The date and time this account was created
    created_at     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Make user_id the primary key (the main unique identifier)
    PRIMARY KEY (user_id)

);

-- ============================================================
--  TABLE 2: integrity_pledges
--
--  When a student registers, they must agree to the
--  Academic Integrity Pledge before they can use the platform.
--  This table records that agreement permanently.
--
--  One student → one pledge record (one-to-one relationship)
-- ============================================================
CREATE TABLE integrity_pledges (

    -- A unique number for this pledge record
    pledge_id      INT            NOT NULL AUTO_INCREMENT,

    -- Which user agreed to the pledge
    -- This links back to the users table
    user_id        INT            NOT NULL,

    -- Did they agree? Always TRUE if this record exists
    agreed         TINYINT(1)     NOT NULL DEFAULT 1,

    -- The exact date and time they agreed
    agreed_at      TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (pledge_id),

    -- Link user_id to the users table
    -- If a user is deleted, their pledge record is also deleted
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE

);

-- ============================================================
--  TABLE 3: faculties
--
--  The TOP LEVEL of the category system.
--  Think of this as the broadest academic division.
--
--  Examples:
--    - Computing & Technology
--    - Business & Management
--    - Health Sciences
-- ============================================================
CREATE TABLE faculties (

    -- A unique number for this faculty
    faculty_id     INT            NOT NULL AUTO_INCREMENT,

    -- The name of the faculty e.g. "Computing & Technology"
    faculty_name   VARCHAR(100)   NOT NULL UNIQUE,

    -- When this faculty was added
    created_at     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (faculty_id)

);

-- ============================================================
--  TABLE 4: subjects
--
--  The MIDDLE LEVEL of the category system.
--  Each subject belongs to one faculty.
-- ============================================================
CREATE TABLE subjects (

    -- A unique number for this subject
    subject_id     INT            NOT NULL AUTO_INCREMENT,

    -- Which faculty this subject belongs to
    faculty_id     INT            NOT NULL,

    -- The name of the subject e.g. "Advanced Programming"
    subject_name   VARCHAR(100)   NOT NULL,

    -- When this subject was added
    created_at     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (subject_id),

    -- Link faculty_id to the faculties table
    FOREIGN KEY (faculty_id) REFERENCES faculties(faculty_id) ON DELETE CASCADE

);

-- ============================================================
--  TABLE 5: topics
--
--  The BOTTOM LEVEL of the category system.
--  Each topic belongs to one subject.
--  Resources are tagged to a specific topic.
-- ============================================================
CREATE TABLE topics (

    -- A unique number for this topic
    topic_id       INT            NOT NULL AUTO_INCREMENT,

    -- Which subject this topic belongs to
    subject_id     INT            NOT NULL,

    -- The name of the topic e.g. "MVC Architecture"
    topic_name     VARCHAR(100)   NOT NULL,

    -- When this topic was added
    created_at     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (topic_id),

    -- Link subject_id to the subjects table
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE

);

-- ============================================================
--  TABLE 6: resources
--
--  The MAIN TABLE of the whole system.
--  Every study material uploaded by a student is stored here.
--
--  A resource goes through this journey:
--    1. Student uploads it   → status = 'pending'
--    2. Admin opens it       → status = 'under_review'
--    3a. Admin approves it   → status = 'approved'  (visible to everyone)
--    3b. Admin rejects it    → status = 'rejected'  (student is told why)
-- ============================================================
CREATE TABLE resources (

    -- A unique number for this resource
    resource_id       INT          NOT NULL AUTO_INCREMENT,

    -- Which student uploaded this resource
    user_id           INT          NOT NULL,

    -- Which topic this resource belongs to
    topic_id          INT          NOT NULL,

    -- The title of the resource e.g. "Complete MVC Notes Week 3"
    title             VARCHAR(200) NOT NULL,

    -- A short description of what the resource covers
    description       TEXT         NULL DEFAULT NULL,

    -- The file path where the uploaded file is saved on the server
    -- e.g. "uploads/resources/abc123.pdf"
    file_path         VARCHAR(255) NOT NULL,

    -- What type of resource this is
    resource_type     ENUM(
                          'notes',
                          'past_paper',
                          'summary',
                          'revision_guide',
                          'other'
                      ) NOT NULL,

    -- Where this resource is in the moderation pipeline
    status            ENUM(
                          'pending',
                          'under_review',
                          'approved',
                          'rejected'
                      ) NOT NULL DEFAULT 'pending',

    -- Did the student tick the "this is my original work" checkbox?
    -- 1 = Yes they confirmed it  |  0 = No they did not
    self_declaration  TINYINT(1)   NOT NULL DEFAULT 0,

    -- When this resource was uploaded
    upload_date       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (resource_id),

    -- Link user_id to the users table (who uploaded it)
    FOREIGN KEY (user_id)  REFERENCES users(user_id)   ON DELETE CASCADE,

    -- Link topic_id to the topics table (what category it is)
    FOREIGN KEY (topic_id) REFERENCES topics(topic_id) ON DELETE CASCADE

);

-- ============================================================
--  TABLE 7: moderation_logs
--
--  Every time an admin approves or rejects a resource,
--  that action is saved here permanently.
--  This creates a full history of every moderation decision.
--
--  Think of it like an activity logbook for admins.
-- ============================================================
CREATE TABLE moderation_logs (

    -- A unique number for this log entry
    log_id         INT            NOT NULL AUTO_INCREMENT,

    -- Which resource was acted on
    resource_id    INT            NOT NULL,

    -- Which admin took the action
    admin_id       INT            NOT NULL,

    -- What action the admin took
    -- 'approved'       = resource is now live on the platform
    -- 'rejected'       = resource was declined
    -- 'flag_upheld'    = a community flag was upheld (resource removed)
    -- 'flag_dismissed' = a community flag was dismissed (resource stays)
    action         ENUM(
                       'approved',
                       'rejected',
                       'flag_upheld',
                       'flag_dismissed'
                   ) NOT NULL,

    -- The admin's written note explaining their decision
    -- For rejections this should explain why
    note           TEXT           NULL DEFAULT NULL,

    -- When this action was taken
    actioned_at    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (log_id),

    FOREIGN KEY (resource_id) REFERENCES resources(resource_id) ON DELETE CASCADE,
    FOREIGN KEY (admin_id)    REFERENCES users(user_id)          ON DELETE CASCADE

);

-- ============================================================
--  TABLE 8: flags
--
--  When a student thinks a published resource is plagiarised
--  or breaks the rules, they can "flag" it.
--  This table stores those reports for admin review.
--
--  The admin then either:
--    - Upholds the flag   → resource gets removed
--    - Dismisses the flag → resource stays on the platform
-- ============================================================
CREATE TABLE flags (

    -- A unique number for this flag
    flag_id        INT            NOT NULL AUTO_INCREMENT,

    -- Which resource is being reported
    resource_id    INT            NOT NULL,

    -- Which student submitted this flag
    flagged_by     INT            NOT NULL,

    -- The reason the student gave for flagging it
    reason         TEXT           NOT NULL,

    -- What happened with this flag
    -- 'open'      = not yet reviewed by admin
    -- 'upheld'    = admin agreed, resource was removed
    -- 'dismissed' = admin disagreed, resource stays live
    status         ENUM('open', 'upheld', 'dismissed')  NOT NULL DEFAULT 'open',

    -- When the flag was submitted
    created_at     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (flag_id),

    FOREIGN KEY (resource_id) REFERENCES resources(resource_id) ON DELETE CASCADE,
    FOREIGN KEY (flagged_by)  REFERENCES users(user_id)          ON DELETE CASCADE

);

-- ============================================================
--  TABLE 9: collections
--
--  Students can create personal "folders" to save and
--  organise resources they want to study later.
--  Each student can have many collections with different names.
--
--  Examples of collection names a student might create:
--    - "Exam Revision"
--    - "Java Assignment Help"
--    - "Week 6 Prep"
-- ============================================================
CREATE TABLE collections (

    -- A unique number for this collection
    collection_id    INT          NOT NULL AUTO_INCREMENT,

    -- Which student owns this collection
    user_id          INT          NOT NULL,

    -- The name the student gave their collection
    collection_name  VARCHAR(100) NOT NULL,

    -- When this collection was created
    created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (collection_id),

    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE

);

-- ============================================================
--  TABLE 10: collection_items
--
--  This is a BRIDGE TABLE that connects resources to collections.
--  It handles the many-to-many relationship:
--    - One collection can contain MANY resources
--    - One resource can be saved in MANY different collections
--
--  Think of it as the list of items inside each folder.
-- ============================================================
CREATE TABLE collection_items (

    -- A unique number for this saved item
    item_id          INT          NOT NULL AUTO_INCREMENT,

    -- Which collection this item is saved in
    collection_id    INT          NOT NULL,

    -- Which resource was saved
    resource_id      INT          NOT NULL,

    -- When the student saved this resource to the collection
    added_at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (item_id),

    FOREIGN KEY (collection_id) REFERENCES collections(collection_id) ON DELETE CASCADE,
    FOREIGN KEY (resource_id)   REFERENCES resources(resource_id)     ON DELETE CASCADE

);

-- ============================================================
--  TABLE 11: ratings
--
--  Students can give star ratings (1 to 5) to approved resources.
--  The UNIQUE rule means each student can only rate
--  a resource once — no voting multiple times.
--
--  The average of all ratings is shown on the resource card.
-- ============================================================
CREATE TABLE ratings (

    -- A unique number for this rating
    rating_id      INT            NOT NULL AUTO_INCREMENT,

    -- Which resource is being rated
    resource_id    INT            NOT NULL,

    -- Which student gave this rating
    user_id        INT            NOT NULL,

    -- The star score given (must be between 1 and 5)
    score          TINYINT        NOT NULL,

    -- When this rating was given
    rated_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (rating_id),

    -- This rule stops a student from rating the same resource twice
    UNIQUE KEY one_rating_per_student (resource_id, user_id),

    FOREIGN KEY (resource_id) REFERENCES resources(resource_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id)     REFERENCES users(user_id)          ON DELETE CASCADE

);

-- ============================================================
--  DONE! All 11 tables created successfully.
--
--  SUMMARY OF TABLES:
--  1.  users              - All accounts (students + admins)
--  2.  integrity_pledges  - Records of students agreeing to the pledge
--  3.  faculties          - Top level categories (e.g. Computing)
--  4.  subjects           - Middle level categories (e.g. Advanced Programming)
--  5.  topics             - Bottom level categories (e.g. MVC Architecture)
--  6.  resources          - All uploaded study materials
--  7.  moderation_logs    - History of every admin moderation action
--  8.  flags              - Community reports of suspicious content
--  9.  collections        - Students' personal study folders
--  10. collection_items   - The resources saved inside each folder
--  11. ratings            - Star ratings given to resources
--
--  HOW TABLES LINK TOGETHER:
-- 
--  users ─── integrity_pledges     (1 student → 1 pledge)
--  users ─── resources             (1 student → many uploads)
--  users ─── collections           (1 student → many folders)
--  users ─── flags                 (1 student → many reports)
--  users ─── ratings               (1 student → many ratings)
--  resources ─── moderation_logs   (1 resource → many admin actions)
--  resources ─── flags             (1 resource → many reports)
--  resources ─── collection_items  (1 resource → saved in many folders)
--  resources ─── ratings           (1 resource → many ratings)
--  collections ─── collection_items (1 folder → many resources inside)
--  faculties ─── subjects ─── topics ─── resources  (category chain)
-- ============================================================
