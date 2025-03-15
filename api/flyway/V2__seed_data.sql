-- Insert data into users table
INSERT INTO users (user_id, first_name, last_name) VALUES
('google_user_123', 'John', 'Doe'),
('google_user_456', 'Jane', 'Smith'),
('google_user_789', 'Peter', 'Jones'),
('google_user_101', 'Alice', 'Brown');

-- Insert data into project_status table
INSERT INTO project_status (name) VALUES
('Planning'),
('In Progress'),
('Completed'),
('On Hold');

-- Insert data into user_roles table
INSERT INTO user_roles (role) VALUES
('Project Manager'),
('Developer'),
('Designer'),
('Tester'),
('Stakeholder');

-- Insert data into projects table
INSERT INTO projects (name, description, status_id, owner_id) VALUES
('New Website Development', 'Develop a new company website.', 1, 'google_user_123'), -- Planning, Owner: John Doe
('Mobile App Redesign', 'Redesign the existing mobile application.', 2, 'google_user_456'), -- In Progress, Owner: Jane Smith
('Marketing Campaign', 'Launch a new marketing campaign.', 3, 'google_user_123'), -- Completed, Owner: John Doe
('Internal Tool Development', 'Build an internal tool for team management.', 4, 'google_user_789'); -- On Hold, Owner: Peter Jones

-- Insert data into project_members table
INSERT INTO project_members (project_id, user_id, role_id) VALUES
(1, 'google_user_123', 1), -- Project: New Website, User: John Doe, Role: Project Manager
(1, 'google_user_456', 2), -- Project: New Website, User: Jane Smith, Role: Developer
(2, 'google_user_456', 1), -- Project: Mobile App, User: Jane Smith, Role: Project Manager
(2, 'google_user_789', 3), -- Project: Mobile App, User: Peter Jones, Role: Designer
(3, 'google_user_123', 1), -- Project: Marketing Campaign, User: John Doe, Role: Project Manager
(3, 'google_user_101', 4), -- Project: Marketing Campaign, User: Alice Brown, Role: Tester
(4, 'google_user_789', 2), -- Project: Internal Tool, User: Peter Jones, Role: Developer
(4, 'google_user_123', 5); -- Project: Internal Tool, User: John Doe, Role: Stakeholder

-- Insert data into task_status table
INSERT INTO task_status (name) VALUES
('To Do'),
('In Progress'),
('Review'),
('Done');

-- Insert data into tasks table
INSERT INTO tasks (title, description, status_id, project_id, assignee_id) VALUES
('Design Homepage Mockup', 'Create a mockup for the website homepage.', 1, 1, 'google_user_456'), -- To Do, Project: New Website, Assignee: Jane Smith
('Develop User Authentication', 'Implement user login and registration.', 2, 1, 'google_user_123'), -- In Progress, Project: New Website, Assignee: John Doe
('Review Mobile App UI', 'Review the user interface of the mobile app.', 3, 2, 'google_user_789'), -- Review, Project: Mobile App, Assignee: Peter Jones
('Write Blog Post', 'Write a blog post for the marketing campaign.', 4, 3, 'google_user_101'), -- Done, Project: Marketing, Assignee: Alice Brown
('Set up Database', 'Set up the database for the internal tool.', 1, 4, 'google_user_789'); -- To Do, Project: Internal Tool, Assignee: Peter Jones

-- Insert data into comments table
INSERT INTO comments (content, user_id, task_id) VALUES
('Looks good so far!', 'google_user_456', 1),
('Need to refine the color scheme.', 'google_user_789', 1),
('Authentication is working as expected.', 'google_user_123', 2),
('Let me know when the UI is ready for final review.', 'google_user_123', 3),
('Great job on the blog post!', 'google_user_123', 4);

-- Insert data into activity_logs table
INSERT INTO activity_logs (task_id, action) VALUES
(1, 'Task created'),
(1, 'Status changed to In Progress'),
(2, 'Task created'),
(2, 'Assignee changed to John Doe'),
(3, 'Task created'),
(4, 'Task created'),
(4, 'Status changed to Done'),
(5, 'Task created');