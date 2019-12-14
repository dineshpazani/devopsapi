DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  username VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL,
  email VARCHAR(250) DEFAULT NULL
);

INSERT INTO users (username, password, email) VALUES
('dinesh', 'pazani', 'din@gmail.com'),
('karthick', 'palani', 'karthick@gmail.com'),
('Folrunsho', 'Alakija', 'test@gmail.com');
