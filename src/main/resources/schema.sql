-- ==========================================
-- 1. UTILITY INVENTORY INDEPENDENT TABLES
-- ==========================================

CREATE TABLE IF NOT EXISTS uploads (
   id BIGSERIAL PRIMARY KEY,
   url TEXT NOT NULL,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL, -- Restricted length for memory compaction
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    profile_picture BIGINT,
    FOREIGN KEY (profile_picture) REFERENCES uploads(id) ON DELETE SET NULL
);

-- ==========================================
-- 2. CLASSROOM CORE INFRASTRUCTURE
-- ==========================================

CREATE TABLE IF NOT EXISTS classrooms (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    invite_code VARCHAR(6) NOT NULL UNIQUE, -- Alphanumeric join token (e.g., 'X7Y2Z1')
    classroom_cover_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (classroom_cover_id) REFERENCES uploads(id) ON DELETE SET NULL
);

-- Consolidated unified roster mapping (Many-to-Many)
CREATE TABLE IF NOT EXISTS classrooms_users (
    user_id BIGINT NOT NULL,
    classroom_id BIGINT NOT NULL,
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, classroom_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (classroom_id) REFERENCES classrooms(id) ON DELETE CASCADE
);

-- ==========================================
-- 3. ACADEMIC ASSIGNMENTS
-- ==========================================

CREATE TABLE IF NOT EXISTS assignments (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    classroom_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (classroom_id) REFERENCES classrooms(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS assignment_uploads (
    assignment_id BIGINT NOT NULL,
    upload_id BIGINT NOT NULL,
    PRIMARY KEY (assignment_id, upload_id),
    FOREIGN KEY (assignment_id) REFERENCES assignments(id) ON DELETE CASCADE,
    FOREIGN KEY (upload_id) REFERENCES uploads(id) ON DELETE CASCADE
);

-- ==========================================
-- 4. STUDENT SUBMISSION FLOWS
-- ==========================================

CREATE TABLE IF NOT EXISTS submissions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    assignment_id BIGINT NOT NULL,
    submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT unique_student_assignment UNIQUE (user_id, assignment_id),

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (assignment_id) REFERENCES assignments(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS submission_uploads (
    submission_id BIGINT NOT NULL,
    upload_id BIGINT NOT NULL,
    PRIMARY KEY (submission_id, upload_id),
    FOREIGN KEY (submission_id) REFERENCES submissions(id) ON DELETE CASCADE,
    FOREIGN KEY (upload_id) REFERENCES uploads(id) ON DELETE CASCADE
);