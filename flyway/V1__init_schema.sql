-- Drop tables if they exist (for development/testing purposes)
DROP TABLE IF EXISTS activity_logs;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS task_status;
DROP TABLE IF EXISTS project_members;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS project_status;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    user_id VARCHAR PRIMARY KEY, -- Using Google Sign-In ID as primary key
    first_name VARCHAR NOT NULL,
    last_name VARCHAR NOT NULL
);

-- Create project_status table
CREATE TABLE project_status (
    project_status_id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL UNIQUE
);

-- Create projects table
CREATE TABLE projects (
    project_id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    description TEXT,
    status_id INTEGER NOT NULL,
    owner_id VARCHAR NOT NULL,
    FOREIGN KEY (status_id) REFERENCES project_status(project_status_id) ON DELETE RESTRICT,
    FOREIGN KEY (owner_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Create project_members table
CREATE TABLE project_members (
    project_member_id SERIAL PRIMARY KEY,
    project_id INTEGER NOT NULL,
    user_id VARCHAR NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Create task_status table
CREATE TABLE task_status (
    task_status_id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL UNIQUE
);

-- Create tasks table
CREATE TABLE tasks (
    task_id SERIAL PRIMARY KEY,
    title VARCHAR NOT NULL,
    description TEXT,
    status_id INTEGER NOT NULL,
    project_id INTEGER NOT NULL,
    assignee_id VARCHAR NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (status_id) REFERENCES task_status(task_status_id) ON DELETE RESTRICT,
    FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE,
    FOREIGN KEY (assignee_id) REFERENCES users(user_id) ON DELETE SET NULL
);

-- Create comments table
CREATE TABLE comments (
    comment_id SERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    user_id VARCHAR NOT NULL,
    task_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (task_id) REFERENCES tasks(task_id) ON DELETE CASCADE
);

-- Create activity_logs table
CREATE TABLE activity_logs (
    activity_log_id SERIAL PRIMARY KEY,
    task_id INTEGER NOT NULL,
    action VARCHAR NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES tasks(task_id) ON DELETE CASCADE
);