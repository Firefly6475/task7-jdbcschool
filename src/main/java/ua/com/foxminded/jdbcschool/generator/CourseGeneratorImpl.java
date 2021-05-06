package ua.com.foxminded.jdbcschool.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import ua.com.foxminded.jdbcschool.domain.Course;

public class CourseGeneratorImpl implements CourseGenerator {
    
    @Override
    public List<Course> generateCourses(List<String> coursesData) {
        List<Course> courses = new ArrayList<>();
        coursesData.forEach(courseName -> courses.add(Course.builder()
                    .withId(UUID.randomUUID().toString())
                    .withName(courseName)
                    .withDescription("This is " + courseName)
                    .build()));
        return courses;
    }
}
