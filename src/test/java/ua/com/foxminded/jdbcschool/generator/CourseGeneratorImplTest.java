package ua.com.foxminded.jdbcschool.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.jdbcschool.domain.Course;

public class CourseGeneratorImplTest {
    private final CourseGenerator courseGenerator = new CourseGeneratorImpl();
    
    @Test
    void generateCoursesShouldReturnCoursesList() {
        List<String> courseData = new ArrayList<>();
        courseData.add("Math");
        courseData.add("Biology");
        courseData.add("Physics");

        List<Course> expectedCourses = new ArrayList<>();
        expectedCourses.add(Course.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Math")
                .withDescription("This is Math")
                .build());
        expectedCourses.add(Course.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Biology")
                .withDescription("This is Biology")
                .build());
        expectedCourses.add(Course.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Physics")
                .withDescription("This is Physics")
                .build());
        
        List<Course> actualCourses = courseGenerator.generateCourses(courseData);
        assertEquals(expectedCourses, actualCourses);
    }
}
