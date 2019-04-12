use students;

ALTER DATABASE students
  DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;

CREATE TABLE IF NOT EXISTS students_data (
  id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(30),
  last_name VARCHAR(30),
  INDEX(last_name)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS registration_data (
  id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  course_name VARCHAR(30),
  student_id INT (4) UNSIGNED NOT NULL,
  FOREIGN KEY (student_id) REFERENCES students_data(id)
) engine=InnoDB;