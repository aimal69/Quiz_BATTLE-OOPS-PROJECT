
CREATE DATABASE quiz_database;
USE quiz_database;

CREATE TABLE questions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question TEXT,
    option_a VARCHAR(100),
    option_b VARCHAR(100),
    option_c VARCHAR(100),
    option_d VARCHAR(100),
    correct_option CHAR(1)
);

CREATE TABLE players (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    score INT
);
INSERT INTO questions (question, option_a, option_b, option_c, option_d, correct_option)
VALUES ('Which language is used for Android development?', 'Java', 'Python', 'C++', 'Ruby', 'A')
,
("what is capital of india?",'mumbai','delhi','gujrat','punjab','B')
,
("what ia capital of japan?",'mumbai','tokyo','paris','new york','B')
,
(who is the founder of microsoft?",'zorawar','behram','bill gates','kashif ali','c');;;
