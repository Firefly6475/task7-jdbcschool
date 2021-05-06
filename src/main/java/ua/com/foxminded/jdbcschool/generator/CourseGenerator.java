package ua.com.foxminded.jdbcschool.generator;

import java.util.List;
import ua.com.foxminded.jdbcschool.domain.Course;

public interface CourseGenerator {
    List<Course> generateCourses(List<String> coursesData);
}
