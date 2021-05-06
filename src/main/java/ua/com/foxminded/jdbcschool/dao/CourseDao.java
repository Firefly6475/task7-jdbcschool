package ua.com.foxminded.jdbcschool.dao;

import ua.com.foxminded.jdbcschool.domain.Course;

public interface CourseDao extends CrudDao<Course> {    
    Course findByName(String courseName);
}
