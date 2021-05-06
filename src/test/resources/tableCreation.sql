DROP TABLE IF EXISTS groups, students, courses, students_courses;
CREATE TABLE groups(group_id varchar(255), group_name varchar(255), PRIMARY KEY (group_id));
CREATE TABLE students(student_id varchar(255), group_id varchar(255), first_name varchar(255), last_name varchar(255), PRIMARY KEY (student_id), FOREIGN KEY (group_id) REFERENCES groups(group_id));
CREATE TABLE courses(course_id varchar(255), course_name varchar(255), course_description varchar(255), PRIMARY KEY (course_id));
CREATE TABLE students_courses (student_id varchar(255), course_id varchar(255), FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE, FOREIGN KEY (course_id) REFERENCES courses(course_id))