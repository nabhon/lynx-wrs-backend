-- ==========================================
-- Mock Data Seeder for Lynx WRS Schema (Extended)
-- ==========================================

TRUNCATE task_activity, tasks, sprints, cycles, project_members, projects, users RESTART IDENTITY CASCADE;

-- ==========================================
-- USERS
-- ==========================================
INSERT INTO users (email, password, display_name, role, is_active, last_login_at)
VALUES
    ('admin@lynx.com', '$2a$12$v6.Br8hmI/8mm/Omfh/2QORXwEa0VJ7sNGDNdSx0c1H7b4ykJ4CGu', 'System Admin', 'ADMIN', true, now()),
    ('moderator@lynx.com', '$2a$12$s04UiQAIi7pQm/VCEZNsHOGcGfsYPT3MiNYykqF17YKbb08DUyngK', 'Project Moderator', 'MODERATOR', true, now()),
    ('alice@lynx.com', '$2a$12$op/9NtRuUQp1/w2MykjFvee0.6abEjUbyH.D1zw96OQjpC92j0FJy', 'Alice Johnson', 'USER', true, now()),
    ('bob@lynx.com', '$2a$12$kFhQg9FeCNDKhOtCGqyR7O6xWEKDI0tfxxW1.BXj.cURqsX5qmBJe', 'Bob Lee', 'USER', true, now()),
    ('charlie@lynx.com', '$2a$12$E9Uz3WrVpVMmtoi4m191D.s3oxXSTWlV65IRYXLT7CJXI21aEHs2C', 'Charlie Kim', 'USER', true, now()),
    ('diana@lynx.com', '$2a$12$O5AXZyEpCUwirWt50T0yX.Z5VLS5dEnlONic89QWGP3tF26eLWn5.', 'Diana Wong', 'USER', true, now()),
    ('eric@lynx.com', '$2a$12$43PqQnz4E60u/de1KWnBiuwf7Z5ZgrTpaHNeakqoK1wxBGSMLM2Ru', 'Eric Tan', 'USER', true, now());

-- ==========================================
-- PROJECTS
-- ==========================================
INSERT INTO projects (key, name, description, created_by)
VALUES
    ('LYNX', 'Lynx Work Tracking', 'A lightweight work tracking tool like Jira.', 1),
    ('PET', 'PetCare System', 'Pet care management software.', 2),
    ('FIN', 'Finance Tracker', 'Personal finance and expense management system.', 2);

-- ==========================================
-- PROJECT MEMBERS
-- ==========================================
INSERT INTO project_members (project_id, user_id, role)
VALUES
    (1, 1, 'OWNER'),
    (1, 2, 'MEMBER'),
    (1, 3, 'MEMBER'),
    (1, 4, 'MEMBER'),
    (2, 2, 'OWNER'),
    (2, 5, 'MEMBER'),
    (2, 6, 'MEMBER'),
    (3, 2, 'OWNER'),
    (3, 3, 'MEMBER'),
    (3, 7, 'MEMBER');

-- ==========================================
-- CYCLES
-- ==========================================
INSERT INTO cycles (project_id, cycle_count, start_date, end_date)
VALUES
-- Project 1: Lynx
(1, 1, '2025-10-01', '2025-10-31'),
(1, 2, '2025-11-01', '2025-11-30'),
(1, 3, '2025-12-01', '2025-12-31'),
-- Project 2: PetCare
(2, 1, '2025-09-15', '2025-10-15'),
(2, 2, '2025-10-16', '2025-11-15'),
-- Project 3: Finance Tracker
(3, 1, '2025-10-01', '2025-10-31'),
(3, 2, '2025-11-01', '2025-11-30');

-- ==========================================
-- SPRINTS
-- ==========================================
INSERT INTO sprints (cycle_id, sprint_count, start_date, end_date)
VALUES
-- Lynx Cycle 1
(1, 1, '2025-10-01', '2025-10-07'),
(1, 2, '2025-10-08', '2025-10-15'),
(1, 3, '2025-10-16', '2025-10-23'),
(1, 4, '2025-10-24', '2025-10-31'),
-- Lynx Cycle 2
(2, 1, '2025-11-01', '2025-11-07'),
(2, 2, '2025-11-08', '2025-11-15'),
(2, 3, '2025-11-16', '2025-11-23'),
(2, 4, '2025-11-24', '2025-11-30'),
-- PetCare Cycles
(4, 1, '2025-09-15', '2025-09-22'),
(4, 2, '2025-09-23', '2025-09-30'),
(5, 1, '2025-10-16', '2025-10-23'),
(5, 2, '2025-10-24', '2025-11-01'),
-- Finance Tracker Cycles
(6, 1, '2025-10-01', '2025-10-10'),
(6, 2, '2025-10-11', '2025-10-20'),
(7, 1, '2025-11-01', '2025-11-10'),
(7, 2, '2025-11-11', '2025-11-20');

-- ==========================================
-- TASKS
-- ==========================================
INSERT INTO tasks (
    project_id, cycle_id, sprint_id, key, title, description, type, status, priorities,
    estimate_points, actual_points, start_date, due_date, finished_at,
    assigned_to, audited_by, created_by
)
VALUES
-- LYNX Project
(1, 1, 1, 'LYNX-1', 'Setup DB schema', 'Design and implement PostgreSQL schema.', 'DBD', 'DONE', 'HIGH', 5, 5, '2025-10-01', '2025-10-05', '2025-10-04', 3, 2, 1),
(1, 1, 2, 'LYNX-2', 'Add JWT auth', 'Implement access and refresh tokens.', 'AD', 'IN_PROGRESS', 'HIGH', 8, NULL, '2025-10-08', '2025-10-15', NULL, 3, 1, 1),
(1, 1, 3, 'LYNX-3', 'Dashboard UI', 'Create dashboard page and layout.', 'UXI', 'TODO', 'MEDIUM', 5, NULL, '2025-10-16', '2025-10-25', NULL, 4, 2, 2),
(1, 2, 5, 'LYNX-4', 'Add project stats', 'Show velocity and progress metrics.', 'SQ', 'REVIEW', 'MEDIUM', 3, 3, '2025-11-02', '2025-11-07', '2025-11-06', 3, 2, 1),
(1, 2, 6, 'LYNX-5', 'Optimize queries', 'Improve DB indexes and joins.', 'ER', 'IN_PROGRESS', 'HIGH', 8, NULL, '2025-11-10', '2025-11-17', NULL, 4, 2, 1),
(1, 3, 7, 'LYNX-6', 'Refactor code', 'Refactor API service layer.', 'SD', 'TODO', 'LOW', 5, NULL, '2025-12-05', '2025-12-15', NULL, 3, 1, 1),

-- PET Project
(2, 4, 9, 'PET-1', 'Pet registration', 'Create backend API for pet registration.', 'SD', 'DONE', 'HIGH', 5, 5, '2025-09-16', '2025-09-21', '2025-09-20', 5, 2, 2),
(2, 4, 10, 'PET-2', 'Appointment module', 'CRUD for vet appointments.', 'MIT', 'LATE', 'MEDIUM', 8, 3, '2025-09-22', '2025-09-30', NULL, 6, 2, 2),
(2, 5, 11, 'PET-3', 'Notifications', 'Implement pet vaccination reminders.', 'MFT', 'TODO', 'HIGH', 5, NULL, '2025-10-18', '2025-10-26', NULL, 6, 2, 2),
(2, 5, 12, 'PET-4', 'User dashboard', 'Owner dashboard for pet overview.', 'UXI', 'IN_PROGRESS', 'MEDIUM', 8, NULL, '2025-10-25', '2025-11-05', NULL, 5, 2, 2),

-- FIN Project
(3, 6, 13, 'FIN-1', 'Setup schema', 'Design tables for income and expenses.', 'DBD', 'DONE', 'HIGH', 5, 5, '2025-10-02', '2025-10-06', '2025-10-06', 7, 2, 2),
(3, 6, 14, 'FIN-2', 'Add login', 'Implement OAuth2 login.', 'AD', 'IN_PROGRESS', 'HIGH', 8, NULL, '2025-10-11', '2025-10-20', NULL, 3, 1, 2),
(3, 7, 15, 'FIN-3', 'Reports module', 'Generate financial summary reports.', 'SQ', 'REVIEW', 'MEDIUM', 5, 4, '2025-11-03', '2025-11-09', '2025-11-08', 7, 1, 2),
(3, 7, 16, 'FIN-4', 'Budget tracker UI', 'Frontend page for monthly budgets.', 'UXI', 'TODO', 'MEDIUM', 5, NULL, '2025-11-10', '2025-11-20', NULL, 3, 2, 2);

-- ==========================================
-- TASK ACTIVITY
-- ==========================================
INSERT INTO task_activity (task_id, actor_id, from_status, to_status, occurred_at)
VALUES
    (1, 3, 'TODO', 'IN_PROGRESS', '2025-10-02 09:00:00'),
    (1, 3, 'IN_PROGRESS', 'DONE', '2025-10-04 18:00:00'),
    (2, 3, 'TODO', 'IN_PROGRESS', '2025-10-09 10:00:00'),
    (4, 3, 'IN_PROGRESS', 'REVIEW', '2025-11-06 17:00:00'),
    (8, 6, 'IN_PROGRESS', 'LATE', '2025-09-29 20:00:00'),
    (13, 7, 'TODO', 'IN_PROGRESS', '2025-10-04 09:30:00'),
    (13, 7, 'IN_PROGRESS', 'DONE', '2025-10-06 16:45:00');
