DROP TABLE IF EXISTS groups, students, courses;
CREATE TABLE groups(
	group_id int;
	group_name varchar(255)
);
CREATE TABLE students(
	student_id int,
	group_id int,
	first_name varchar(255),
	last_name varchar(255)
);
CREATE TABLE courses(
	course_id int,
	course_name varchar(255),
	course_description varchar(255)
);