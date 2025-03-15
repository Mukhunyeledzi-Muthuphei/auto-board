CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    first_name VARCHAR NOT NULL,
    last_name VARCHAR NOT NULL
);

CREATE TABLE project_status (
    project_status_id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE projects (
    project_id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    description TEXT,
    status_id INTEGER NOT NULL,
    owner_id INTEGER NOT NULL,
    FOREIGN KEY (status_id) REFERENCES project_status(project_status_id) ON DELETE RESTRICT,
    FOREIGN KEY (owner_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE user_roles (
    role_id SERIAL PRIMARY KEY,
    role VARCHAR NOT NULL
);

CREATE TABLE project_members (
    project_member_id SERIAL PRIMARY KEY,
    project_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL, -- Assuming you want to link to user_roles
    FOREIGN KEY (role_id) REFERENCES user_roles(role_id) ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE task_status (
    task_status_id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE tasks (
    task_id SERIAL PRIMARY KEY,
    title VARCHAR NOT NULL,
    description TEXT,
    status_id INTEGER NOT NULL,
    project_id INTEGER NOT NULL,
    assignee_id INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (status_id) REFERENCES task_status(task_status_id) ON DELETE RESTRICT,
    FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE,
    FOREIGN KEY (assignee_id) REFERENCES users(user_id) ON DELETE SET NULL
);

CREATE TABLE comments (
    comment_id SERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    user_id INTEGER NOT NULL,
    task_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (task_id) REFERENCES tasks(task_id) ON DELETE CASCADE
);

CREATE TABLE activity_logs (
    activity_log_id SERIAL PRIMARY KEY,
    task_id INTEGER NOT NULL,
    action VARCHAR NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES tasks(task_id) ON DELETE CASCADE
);