
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100),
    gender VARCHAR(10),
    email VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE posts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    date DATE,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);


CREATE VIEW post_details AS
SELECT
    p.id AS post_id,
    p.title AS post_title,
    p.content AS post_content,
    p.date AS post_date,
    u.id AS user_id,
    u.name AS user_name,
    u.gender AS user_gender,
    u.email AS user_email,
    u.created_at AS user_created_at
FROM posts p
JOIN users u ON p.user_id = u.id;
