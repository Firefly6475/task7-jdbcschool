package ua.com.foxminded.jdbcschool.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.jdbcschool.dao.CourseDao;
import ua.com.foxminded.jdbcschool.dao.DBConnector;
import ua.com.foxminded.jdbcschool.dao.StudentDao;
import ua.com.foxminded.jdbcschool.dao.TableCreator;
import ua.com.foxminded.jdbcschool.domain.Course;
import ua.com.foxminded.jdbcschool.domain.Group;
import ua.com.foxminded.jdbcschool.domain.Student;

public class StudentDaoImplTest {
    private final DBConnector connector = new DBConnector("src/test/resources/db-test.properties");
    private final TableCreator tableCreator = new TableCreator(connector);
    private final StudentDao studentDao = new StudentDaoImpl(connector);
    private final CourseDao courseDao = new CourseDaoImpl(connector);
    
    @BeforeEach
    void createTables() {
        tableCreator.createTables("src/test/resources/tableCreation.sql");
    }
    
    @Test
    void findStudentsWithGivenCourseShouldReturnStudentWithGivenCourse() {
        Group group = Group.builder()
                .withId(null)
                .withName(null)
                .build();
        Student firstStudent = Student.builder()
                .withId(UUID.randomUUID().toString())
                .withGroup(group)
                .withFirstName("Hello")
                .withLastName("World")
                .build();
        Student secondStudent = Student.builder()
                .withId(UUID.randomUUID().toString())
                .withGroup(group)
                .withFirstName("Hi")
                .withLastName("World")
                .build();
        Student thirdStudent = Student.builder()
                .withId(UUID.randomUUID().toString())
                .withGroup(group)
                .withFirstName("Hi")
                .withLastName("World")
                .build();
        Course firstCourse = Course.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Math")
                .withDescription("This is Hello")
                .build();
        Course secondCourse = Course.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Physics")
                .withDescription("This is World")
                .build();
        
        studentDao.save(firstStudent);
        studentDao.save(secondStudent);
        studentDao.save(thirdStudent);
        
        courseDao.save(firstCourse);
        courseDao.save(secondCourse);
        
        studentDao.saveRelation(firstStudent.getId(), secondCourse.getId());
        studentDao.saveRelation(secondStudent.getId(), firstCourse.getId());
        studentDao.saveRelation(thirdStudent.getId(), secondCourse.getId());
        
        List<Student> expectedStudents = new ArrayList<>();
        expectedStudents.add(secondStudent);
        
        String givenCourseName = "Math";
        List<Student> actualStudents = studentDao.findAllByCourseName(givenCourseName);
        
        assertEquals(expectedStudents, actualStudents);
    }
    
    @Test
    void insertStudentCoursesShouldInsertRelation() {
        Course firstCourse = Course.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Math")
                .withDescription("This is Hello")
                .build();
        Course secondCourse = Course.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Physics")
                .withDescription("This is Hello")
                .build();
        courseDao.save(firstCourse);
        courseDao.save(secondCourse);
        List<Course> courses = new ArrayList<>();
        courses.add(firstCourse);
        courses.add(secondCourse);
        
        Student student = Student.builder()
                .withId(UUID.randomUUID().toString())
                .withCourses(courses)
                .withFirstName("Hello")
                .withLastName("World")
                .build();
        studentDao.save(student);
        
        List<Student> students = new ArrayList<>();
        students.add(student);

        studentDao.insertStudentCourses(students);
        List<String> expectedList = new ArrayList<>();
        expectedList.add(student.getId());
        expectedList.add(firstCourse.getId());
        List<String> actualList = studentDao.findRelation(student.getId(), firstCourse.getId());
        assertEquals(expectedList, actualList);
        
        expectedList.remove(1);
        expectedList.add(secondCourse.getId());
        actualList = studentDao.findRelation(student.getId(), secondCourse.getId());
        assertEquals(expectedList, actualList);
    }
    
    @Test
    void saveRelationShouldInsertRelation() {
        Student student = Student.builder()
                .withId(UUID.randomUUID().toString())
                .withFirstName("Hello")
                .withLastName("World")
                .build();
        Course course = Course.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Math")
                .withDescription("This is Hello")
                .build();
        studentDao.save(student);
        courseDao.save(course);
        
        studentDao.saveRelation(student.getId(), course.getId());
        List<String> expectedList = new ArrayList<>();
        expectedList.add(student.getId());
        expectedList.add(course.getId());
        List<String> actualList = studentDao.findRelation(student.getId(), course.getId());
        
        assertEquals(expectedList, actualList);
    }
    
    @Test
    void deleteRelationShouldRemoveRelation() {
        Student student = Student.builder()
                .withId(UUID.randomUUID().toString())
                .withFirstName("Hello")
                .withLastName("World")
                .build();
        Course course = Course.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Math")
                .withDescription("This is Hello")
                .build();
        studentDao.save(student);
        courseDao.save(course);
        
        studentDao.saveRelation(student.getId(), course.getId());
        studentDao.deleteRelation(student.getId(), course.getId());
        List<String> list = studentDao.findRelation(student.getId(), course.getId());
        assertTrue(list.isEmpty());
    }
    
    @Test
    void updateShouldChangeStudentFields() {
        Group group = Group.builder()
                .withId(null)
                .withName(null)
                .build();
        Student student = Student.builder()
                .withId(UUID.randomUUID().toString())
                .withFirstName("Hello")
                .withLastName("World")
                .withGroup(group)
                .build();
        studentDao.save(student);
        Student expectedStudent = Student.builder()
                .withId(student.getId())
                .withFirstName("Hi")
                .withLastName("Friend")
                .withGroup(group)
                .build();
        studentDao.update(expectedStudent);
        Student actualStudent = studentDao.findById(student.getId()).get();
        assertEquals(expectedStudent, actualStudent);
    }
}
