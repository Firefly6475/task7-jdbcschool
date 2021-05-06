package ua.com.foxminded.jdbcschool.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.jdbcschool.dao.CourseDao;
import ua.com.foxminded.jdbcschool.dao.DBConnector;
import ua.com.foxminded.jdbcschool.dao.TableCreator;
import ua.com.foxminded.jdbcschool.dao.exception.DBRuntimeException;
import ua.com.foxminded.jdbcschool.domain.Course;

@ExtendWith(MockitoExtension.class)
public class CourseDaoImplTest {
    private final DBConnector connector = new DBConnector("src/test/resources/db-test.properties");
    private final TableCreator tableCreator = new TableCreator(connector);
    private final CourseDao courseDao = new CourseDaoImpl(connector);
    
    @BeforeEach
    void createTables() {
        tableCreator.createTables("src/test/resources/tableCreation.sql");
    }
    
    @Test
    void getCourseIdWithGivenNameShouldReturnCourseIdWithSpecifiedName() {
        Course firstCourse = Course.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Hello")
                .withDescription("This is Hello")
                .build();
        Course secondCourse = Course.builder()
                .withId(UUID.randomUUID().toString())
                .withName("World")
                .withDescription("This is World")
                .build();
        courseDao.save(firstCourse);
        courseDao.save(secondCourse);
        
        String expectedId = secondCourse.getId();
        String actualId = courseDao.findByName(secondCourse.getName()).getId();
        assertEquals(expectedId, actualId);
    }
    
    @Test
    void updateShouldChangeCourseFields() {
        Course course = Course.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Math")
                .withDescription("This is Math")
                .build();
        courseDao.save(course);
        Course expectedCourse = Course.builder()
                .withId(course.getId())
                .withName("Physics")
                .withDescription("This is Physics")
                .build();
        courseDao.update(expectedCourse);
        Course actualCourse = courseDao.findById(course.getId()).get();
        assertEquals(expectedCourse, actualCourse);
    }
    
    @Test
    void saveCourseShouldThrowDBRuntimeExceptionIfCoursesHaveSameId() {
        Course firstCourse = Course.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Math")
                .withDescription("This is Math")
                .build();
        Course secondCourse = Course.builder()
                .withId(firstCourse.getId())
                .withName("Physics")
                .withDescription("This is Physics")
                .build();
        courseDao.save(firstCourse);
        
        Exception exception = assertThrows(DBRuntimeException.class, () -> {
            courseDao.save(secondCourse);
        });
        
        String expectedMessage = "Insertion is failed";
        String actualMessage = exception.getMessage();
        
        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    void saveAllCoursesShouldThrowDBRuntimeExceptionIfCoursesHaveSameId() {
        Course firstCourse = Course.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Math")
                .withDescription("This is Math")
                .build();
        Course secondCourse = Course.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Physics")
                .withDescription("This is Physics")
                .build();
        courseDao.save(firstCourse);
        courseDao.save(secondCourse);
        
        List<Course> courses = new ArrayList<>();
        courses.add(firstCourse);
        courses.add(secondCourse);
        
        Exception exception = assertThrows(DBRuntimeException.class, () -> {
            courseDao.saveAll(courses);
        });
        
        String expectedMessage = "Save all failed";
        String actualMessage = exception.getMessage();
        
        assertEquals(expectedMessage, actualMessage);
    }
}
