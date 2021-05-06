package ua.com.foxminded.jdbcschool.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.jdbcschool.assigner.CoursesToStudentAssigner;
import ua.com.foxminded.jdbcschool.assigner.GroupToStudentAssigner;
import ua.com.foxminded.jdbcschool.dao.CourseDao;
import ua.com.foxminded.jdbcschool.dao.GroupDao;
import ua.com.foxminded.jdbcschool.dao.StudentDao;
import ua.com.foxminded.jdbcschool.dao.TableCreator;
import ua.com.foxminded.jdbcschool.dao.exception.DBRuntimeException;
import ua.com.foxminded.jdbcschool.domain.Course;
import ua.com.foxminded.jdbcschool.domain.Group;
import ua.com.foxminded.jdbcschool.domain.GroupCountRange;
import ua.com.foxminded.jdbcschool.domain.Student;
import ua.com.foxminded.jdbcschool.generator.CourseGenerator;
import ua.com.foxminded.jdbcschool.generator.GroupGenerator;
import ua.com.foxminded.jdbcschool.generator.StudentGenerator;
import ua.com.foxminded.jdbcschool.reader.FileReader;
import ua.com.foxminded.jdbcschool.validator.FileValidator;
import ua.com.foxminded.jdbcschool.view.View;

@ExtendWith(MockitoExtension.class)
public class ControllerTest {

    @Mock
    private View view;

    @Mock
    private TableCreator tableCreator;

    @Mock
    private GroupGenerator groupGenerator;

    @Mock
    private CourseGenerator courseGenerator;

    @Mock
    private StudentGenerator studentGenerator;

    @Mock
    private CoursesToStudentAssigner coursesToStudent;

    @Mock
    private GroupToStudentAssigner groupToStudent;

    @Mock
    private CourseDao courseDao;

    @Mock
    private GroupDao groupDao;

    @Mock
    private StudentDao studentDao;

    @Mock
    private FileValidator fileValidator;

    @Mock
    private FileReader fileReader;

    @InjectMocks
    @Spy
    private Controller controller;

    @Test
    void runShouldPopulateDBAndShowGroupsWithLessOrEqualsCount() {
        String tablesCreationScriptFilepath = "src/test/resources/tableCreation.sql";
        String coursesDataFilepath = "src/test/resources/data/coursesDataTest.txt";
        String firstNameDataFilepath = "src/test/resources/data/firstNameDataTest.txt";
        String lastNameDataFilepath = "src/test/resources/data/lastNameDataTest.txt";

        List<String> courseData = new ArrayList<>();
        List<String> firstNames = new ArrayList<>();
        List<String> lastNames = new ArrayList<>();

        int amountOfGroups = 10;
        int amountOfStudents = 200;
        int maximumCoursesPerStudent = 3;
        int queryNumber = 1;
        int studentCount = 6;
        GroupCountRange range = new GroupCountRange(10, 30);

        List<Group> groups = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        List<Student.Builder> builders = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        List<Group> foundGroups = new ArrayList<>();

        doNothing().when(tableCreator).createTables(tablesCreationScriptFilepath);
        when(groupGenerator.generateGroups(amountOfGroups)).thenReturn(groups);
        doNothing().when(fileValidator).validate(coursesDataFilepath);
        when(fileReader.readFile(coursesDataFilepath)).thenReturn(courseData);
        when(courseGenerator.generateCourses(courseData)).thenReturn(courses);
        doNothing().when(fileValidator).validate(firstNameDataFilepath);
        doNothing().when(fileValidator).validate(lastNameDataFilepath);
        when(fileReader.readFile(firstNameDataFilepath)).thenReturn(firstNames);
        when(fileReader.readFile(lastNameDataFilepath)).thenReturn(lastNames);
        when(studentGenerator.generateStudents(firstNames, lastNames, amountOfStudents))
        .thenReturn(builders);
        doNothing().when(groupToStudent).assignGroupToStudents(builders, groups, range);
        doNothing().when(coursesToStudent).assignCoursesToStudent(builders, courses,
                maximumCoursesPerStudent);
        doNothing().when(courseDao).saveAll(courses);
        doNothing().when(groupDao).saveAll(groups);
        doNothing().when(studentDao).saveAll(students);
        doNothing().when(studentDao).insertStudentCourses(students);
        when(view.getQueryNumber()).thenReturn(queryNumber);
        when(view.getInteger()).thenReturn(studentCount);
        when(groupDao.findAllWithLessOrEqualsStudentCount(studentCount)).thenReturn(foundGroups);

        controller.run(tablesCreationScriptFilepath, coursesDataFilepath, firstNameDataFilepath,
                lastNameDataFilepath, amountOfGroups, amountOfStudents, maximumCoursesPerStudent,
                range);

        verify(tableCreator).createTables(tablesCreationScriptFilepath);
        verify(groupGenerator).generateGroups(amountOfGroups);
        verify(fileValidator).validate(coursesDataFilepath);
        verify(fileReader).readFile(coursesDataFilepath);
        verify(courseGenerator).generateCourses(courseData);
        verify(fileValidator).validate(firstNameDataFilepath);
        verify(fileValidator).validate(lastNameDataFilepath);
        verify(fileReader).readFile(firstNameDataFilepath);
        verify(fileReader).readFile(lastNameDataFilepath);
        verify(studentGenerator).generateStudents(firstNames, lastNames, amountOfStudents);
        verify(groupToStudent).assignGroupToStudents(builders, groups, range);
        verify(coursesToStudent).assignCoursesToStudent(builders, courses,
                maximumCoursesPerStudent);
        verify(courseDao).saveAll(courses);
        verify(groupDao).saveAll(groups);
        verify(studentDao).saveAll(students);
        verify(studentDao).insertStudentCourses(students);
        verify(view).getQueryNumber();
        verify(view).getInteger();
        verify(groupDao).findAllWithLessOrEqualsStudentCount(studentCount);
    }

    @Test
    void runShouldPopulateDBAndShowAllStudentsRelatedToCourse() {
        String tablesCreationScriptFilepath = "src/test/resources/tableCreation.sql";
        String coursesDataFilepath = "src/test/resources/data/coursesDataTest.txt";
        String firstNameDataFilepath = "src/test/resources/data/firstNameDataTest.txt";
        String lastNameDataFilepath = "src/test/resources/data/lastNameDataTest.txt";

        List<String> courseData = new ArrayList<>();
        List<String> firstNames = new ArrayList<>();
        List<String> lastNames = new ArrayList<>();

        int amountOfGroups = 10;
        int amountOfStudents = 200;
        int maximumCoursesPerStudent = 3;
        int queryNumber = 2;
        String courseName = "Math";
        GroupCountRange range = new GroupCountRange(10, 30);

        List<Group> groups = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        List<Student.Builder> builders = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        List<Student> foundStudents = new ArrayList<>();

        doNothing().when(tableCreator).createTables(tablesCreationScriptFilepath);
        when(groupGenerator.generateGroups(amountOfGroups)).thenReturn(groups);
        doNothing().when(fileValidator).validate(coursesDataFilepath);
        when(fileReader.readFile(coursesDataFilepath)).thenReturn(courseData);
        when(courseGenerator.generateCourses(courseData)).thenReturn(courses);
        doNothing().when(fileValidator).validate(firstNameDataFilepath);
        doNothing().when(fileValidator).validate(lastNameDataFilepath);
        when(fileReader.readFile(firstNameDataFilepath)).thenReturn(firstNames);
        when(fileReader.readFile(lastNameDataFilepath)).thenReturn(lastNames);
        when(studentGenerator.generateStudents(firstNames, lastNames, amountOfStudents))
        .thenReturn(builders);
        doNothing().when(groupToStudent).assignGroupToStudents(builders, groups, range);
        doNothing().when(coursesToStudent).assignCoursesToStudent(builders, courses,
                maximumCoursesPerStudent);
        doNothing().when(courseDao).saveAll(courses);
        doNothing().when(groupDao).saveAll(groups);
        doNothing().when(studentDao).saveAll(students);
        doNothing().when(studentDao).insertStudentCourses(students);
        when(view.getQueryNumber()).thenReturn(queryNumber);
        when(view.getString()).thenReturn(courseName);
        when(studentDao.findAllByCourseName(courseName)).thenReturn(foundStudents);

        controller.run(tablesCreationScriptFilepath, coursesDataFilepath, firstNameDataFilepath,
                lastNameDataFilepath, amountOfGroups, amountOfStudents, maximumCoursesPerStudent,
                range);

        verify(tableCreator).createTables(tablesCreationScriptFilepath);
        verify(groupGenerator).generateGroups(amountOfGroups);
        verify(fileValidator).validate(coursesDataFilepath);
        verify(fileReader).readFile(coursesDataFilepath);
        verify(courseGenerator).generateCourses(courseData);
        verify(fileValidator).validate(firstNameDataFilepath);
        verify(fileValidator).validate(lastNameDataFilepath);
        verify(fileReader).readFile(firstNameDataFilepath);
        verify(fileReader).readFile(lastNameDataFilepath);
        verify(studentGenerator).generateStudents(firstNames, lastNames, amountOfStudents);
        verify(groupToStudent).assignGroupToStudents(builders, groups, range);
        verify(coursesToStudent).assignCoursesToStudent(builders, courses,
                maximumCoursesPerStudent);
        verify(courseDao).saveAll(courses);
        verify(groupDao).saveAll(groups);
        verify(studentDao).saveAll(students);
        verify(studentDao).insertStudentCourses(students);
        verify(view).getQueryNumber();
        verify(view).getString();
        verify(studentDao).findAllByCourseName(courseName);
    }

    @Test
    void runShouldPopulateDbAndAddNewStudent() {
        String tablesCreationScriptFilepath = "src/test/resources/tableCreation.sql";
        String coursesDataFilepath = "src/test/resources/data/coursesDataTest.txt";
        String firstNameDataFilepath = "src/test/resources/data/firstNameDataTest.txt";
        String lastNameDataFilepath = "src/test/resources/data/lastNameDataTest.txt";

        List<String> courseData = new ArrayList<>();
        List<String> firstNames = new ArrayList<>();
        List<String> lastNames = new ArrayList<>();
        
        int amountOfGroups = 10;
        int amountOfStudents = 200;
        int maximumCoursesPerStudent = 3;
        int queryNumber = 3;
        GroupCountRange range = new GroupCountRange(10, 30);
        List<Group> groups = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        List<Student.Builder> builders = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        
        String firstName = "hello";
        String lastName = "world";
        String id = UUID.randomUUID().toString();
        Student student = Student.builder()
                .withId(id)
                .withFirstName(firstName)
                .withLastName(lastName)
                .withGroup(null)
                .build();

        doNothing().when(tableCreator).createTables(tablesCreationScriptFilepath);
        when(groupGenerator.generateGroups(amountOfGroups)).thenReturn(groups);
        doNothing().when(fileValidator).validate(coursesDataFilepath);
        when(fileReader.readFile(coursesDataFilepath)).thenReturn(courseData);
        when(courseGenerator.generateCourses(courseData)).thenReturn(courses);
        doNothing().when(fileValidator).validate(firstNameDataFilepath);
        doNothing().when(fileValidator).validate(lastNameDataFilepath);
        when(fileReader.readFile(firstNameDataFilepath)).thenReturn(firstNames);
        when(fileReader.readFile(lastNameDataFilepath)).thenReturn(lastNames);
        when(studentGenerator.generateStudents(firstNames, lastNames, amountOfStudents))
                .thenReturn(builders);
        doNothing().when(groupToStudent).assignGroupToStudents(builders, groups, range);
        doNothing().when(coursesToStudent).assignCoursesToStudent(builders, courses,
                maximumCoursesPerStudent);
        doNothing().when(courseDao).saveAll(courses);
        doNothing().when(groupDao).saveAll(groups);
        doNothing().when(studentDao).saveAll(students);
        doNothing().when(studentDao).insertStudentCourses(students);
        when(view.getQueryNumber()).thenReturn(queryNumber);
        when(view.getFirstName()).thenReturn(firstName);
        when(view.getLastName()).thenReturn(lastName);
        when(controller.generateId()).thenReturn(id);
        doNothing().when(studentDao).save(student);

        controller.run(tablesCreationScriptFilepath, coursesDataFilepath, firstNameDataFilepath,
                lastNameDataFilepath, amountOfGroups, amountOfStudents, maximumCoursesPerStudent,
                range);

        verify(tableCreator).createTables(tablesCreationScriptFilepath);
        verify(groupGenerator).generateGroups(amountOfGroups);
        verify(fileValidator).validate(coursesDataFilepath);
        verify(fileReader).readFile(coursesDataFilepath);
        verify(courseGenerator).generateCourses(courseData);
        verify(fileValidator).validate(firstNameDataFilepath);
        verify(fileValidator).validate(lastNameDataFilepath);
        verify(fileReader).readFile(firstNameDataFilepath);
        verify(fileReader).readFile(lastNameDataFilepath);
        verify(studentGenerator).generateStudents(firstNames, lastNames, amountOfStudents);
        verify(groupToStudent).assignGroupToStudents(builders, groups, range);
        verify(coursesToStudent).assignCoursesToStudent(builders, courses,
                maximumCoursesPerStudent);
        verify(courseDao).saveAll(courses);
        verify(groupDao).saveAll(groups);
        verify(studentDao).saveAll(students);
        verify(studentDao).insertStudentCourses(students);
        verify(view).getQueryNumber();
        verify(view).getFirstName();
        verify(view).getLastName();
        verify(controller).generateId();
        verify(studentDao).save(student);
    }

    @Test
    void runShouldPopulateDbAndDeleteStudentById() {
        String tablesCreationScriptFilepath = "src/test/resources/tableCreation.sql";
        String coursesDataFilepath = "src/test/resources/data/coursesDataTest.txt";
        String firstNameDataFilepath = "src/test/resources/data/firstNameDataTest.txt";
        String lastNameDataFilepath = "src/test/resources/data/lastNameDataTest.txt";

        List<String> courseData = new ArrayList<>();
        List<String> firstNames = new ArrayList<>();
        List<String> lastNames = new ArrayList<>();

        int amountOfGroups = 10;
        int amountOfStudents = 200;
        int maximumCoursesPerStudent = 3;
        int queryNumber = 4;
        GroupCountRange range = new GroupCountRange(10, 30);
        String id = UUID.randomUUID().toString();

        List<Group> groups = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        List<Student.Builder> builders = new ArrayList<>();
        List<Student> students = new ArrayList<>();

        doNothing().when(tableCreator).createTables(tablesCreationScriptFilepath);
        when(groupGenerator.generateGroups(amountOfGroups)).thenReturn(groups);
        doNothing().when(fileValidator).validate(coursesDataFilepath);
        when(fileReader.readFile(coursesDataFilepath)).thenReturn(courseData);
        when(courseGenerator.generateCourses(courseData)).thenReturn(courses);
        doNothing().when(fileValidator).validate(firstNameDataFilepath);
        doNothing().when(fileValidator).validate(lastNameDataFilepath);
        when(fileReader.readFile(firstNameDataFilepath)).thenReturn(firstNames);
        when(fileReader.readFile(lastNameDataFilepath)).thenReturn(lastNames);
        when(studentGenerator.generateStudents(firstNames, lastNames, amountOfStudents))
        .thenReturn(builders);
        doNothing().when(groupToStudent).assignGroupToStudents(builders, groups, range);
        doNothing().when(coursesToStudent).assignCoursesToStudent(builders, courses,
                maximumCoursesPerStudent);
        doNothing().when(courseDao).saveAll(courses);
        doNothing().when(groupDao).saveAll(groups);
        doNothing().when(studentDao).saveAll(students);
        doNothing().when(studentDao).insertStudentCourses(students);
        when(view.getQueryNumber()).thenReturn(queryNumber);
        when(view.getId()).thenReturn(id);
        doNothing().when(studentDao).deleteById(id);

        controller.run(tablesCreationScriptFilepath, coursesDataFilepath, firstNameDataFilepath,
                lastNameDataFilepath, amountOfGroups, amountOfStudents, maximumCoursesPerStudent,
                range);

        verify(tableCreator).createTables(tablesCreationScriptFilepath);
        verify(groupGenerator).generateGroups(amountOfGroups);
        verify(fileValidator).validate(coursesDataFilepath);
        verify(fileReader).readFile(coursesDataFilepath);
        verify(courseGenerator).generateCourses(courseData);
        verify(fileValidator).validate(firstNameDataFilepath);
        verify(fileValidator).validate(lastNameDataFilepath);
        verify(fileReader).readFile(firstNameDataFilepath);
        verify(fileReader).readFile(lastNameDataFilepath);
        verify(studentGenerator).generateStudents(firstNames, lastNames, amountOfStudents);
        verify(groupToStudent).assignGroupToStudents(builders, groups, range);
        verify(coursesToStudent).assignCoursesToStudent(builders, courses,
                maximumCoursesPerStudent);
        verify(courseDao).saveAll(courses);
        verify(groupDao).saveAll(groups);
        verify(studentDao).saveAll(students);
        verify(studentDao).insertStudentCourses(students);
        verify(view).getQueryNumber();
        verify(view).getId();
        verify(studentDao).deleteById(id);
    }

    @Test
    void runShouldPopulateDbAndAddStudentToTheCourse() {
        String tablesCreationScriptFilepath = "src/test/resources/tableCreation.sql";
        String coursesDataFilepath = "src/test/resources/data/coursesDataTest.txt";
        String firstNameDataFilepath = "src/test/resources/data/firstNameDataTest.txt";
        String lastNameDataFilepath = "src/test/resources/data/lastNameDataTest.txt";

        List<String> courseData = new ArrayList<>();
        List<String> firstNames = new ArrayList<>();
        List<String> lastNames = new ArrayList<>();

        int amountOfGroups = 10;
        int amountOfStudents = 200;
        int maximumCoursesPerStudent = 3;
        int queryNumber = 5;
        GroupCountRange range = new GroupCountRange(10, 30);
        String studentId = "hello";
        String courseId = "world";

        List<Group> groups = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        List<Student.Builder> builders = new ArrayList<>();
        List<Student> students = new ArrayList<>();

        doNothing().when(tableCreator).createTables(tablesCreationScriptFilepath);
        when(groupGenerator.generateGroups(amountOfGroups)).thenReturn(groups);
        doNothing().when(fileValidator).validate(coursesDataFilepath);
        when(fileReader.readFile(coursesDataFilepath)).thenReturn(courseData);
        when(courseGenerator.generateCourses(courseData)).thenReturn(courses);
        doNothing().when(fileValidator).validate(firstNameDataFilepath);
        doNothing().when(fileValidator).validate(lastNameDataFilepath);
        when(fileReader.readFile(firstNameDataFilepath)).thenReturn(firstNames);
        when(fileReader.readFile(lastNameDataFilepath)).thenReturn(lastNames);
        when(studentGenerator.generateStudents(firstNames, lastNames, amountOfStudents))
        .thenReturn(builders);
        doNothing().when(groupToStudent).assignGroupToStudents(builders, groups, range);
        doNothing().when(coursesToStudent).assignCoursesToStudent(builders, courses,
                maximumCoursesPerStudent);
        doNothing().when(courseDao).saveAll(courses);
        doNothing().when(groupDao).saveAll(groups);
        doNothing().when(studentDao).saveAll(students);
        doNothing().when(studentDao).insertStudentCourses(students);
        when(view.getQueryNumber()).thenReturn(queryNumber);
        when(view.getId()).thenReturn(studentId, courseId);
        doNothing().when(studentDao).saveRelation(studentId, courseId);

        controller.run(tablesCreationScriptFilepath, coursesDataFilepath, firstNameDataFilepath,
                lastNameDataFilepath, amountOfGroups, amountOfStudents, maximumCoursesPerStudent,
                range);

        verify(tableCreator).createTables(tablesCreationScriptFilepath);
        verify(groupGenerator).generateGroups(amountOfGroups);
        verify(fileValidator).validate(coursesDataFilepath);
        verify(fileReader).readFile(coursesDataFilepath);
        verify(courseGenerator).generateCourses(courseData);
        verify(fileValidator).validate(firstNameDataFilepath);
        verify(fileValidator).validate(lastNameDataFilepath);
        verify(fileReader).readFile(firstNameDataFilepath);
        verify(fileReader).readFile(lastNameDataFilepath);
        verify(studentGenerator).generateStudents(firstNames, lastNames, amountOfStudents);
        verify(groupToStudent).assignGroupToStudents(builders, groups, range);
        verify(coursesToStudent).assignCoursesToStudent(builders, courses,
                maximumCoursesPerStudent);
        verify(courseDao).saveAll(courses);
        verify(groupDao).saveAll(groups);
        verify(studentDao).saveAll(students);
        verify(studentDao).insertStudentCourses(students);
        verify(view).getQueryNumber();
        verify(view, times(2)).getId();
        verify(studentDao).saveRelation(studentId, courseId);
    }

    @Test
    void runShouldPopulateDbAndDeleteCourseFromStudent() {
        String tablesCreationScriptFilepath = "src/test/resources/tableCreation.sql";
        String coursesDataFilepath = "src/test/resources/data/coursesDataTest.txt";
        String firstNameDataFilepath = "src/test/resources/data/firstNameDataTest.txt";
        String lastNameDataFilepath = "src/test/resources/data/lastNameDataTest.txt";

        List<String> courseData = new ArrayList<>();
        List<String> firstNames = new ArrayList<>();
        List<String> lastNames = new ArrayList<>();

        int amountOfGroups = 10;
        int amountOfStudents = 200;
        int maximumCoursesPerStudent = 3;
        int queryNumber = 6;
        GroupCountRange range = new GroupCountRange(10, 30);
        String studentId = "hello";
        String courseId = "world";

        List<Group> groups = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        List<Student.Builder> builders = new ArrayList<>();
        List<Student> students = new ArrayList<>();

        doNothing().when(tableCreator).createTables(tablesCreationScriptFilepath);
        when(groupGenerator.generateGroups(amountOfGroups)).thenReturn(groups);
        doNothing().when(fileValidator).validate(coursesDataFilepath);
        when(fileReader.readFile(coursesDataFilepath)).thenReturn(courseData);
        when(courseGenerator.generateCourses(courseData)).thenReturn(courses);
        doNothing().when(fileValidator).validate(firstNameDataFilepath);
        doNothing().when(fileValidator).validate(lastNameDataFilepath);
        when(fileReader.readFile(firstNameDataFilepath)).thenReturn(firstNames);
        when(fileReader.readFile(lastNameDataFilepath)).thenReturn(lastNames);
        when(studentGenerator.generateStudents(firstNames, lastNames, amountOfStudents))
        .thenReturn(builders);
        doNothing().when(groupToStudent).assignGroupToStudents(builders, groups, range);
        doNothing().when(coursesToStudent).assignCoursesToStudent(builders, courses,
                maximumCoursesPerStudent);
        doNothing().when(courseDao).saveAll(courses);
        doNothing().when(groupDao).saveAll(groups);
        doNothing().when(studentDao).saveAll(students);
        doNothing().when(studentDao).insertStudentCourses(students);
        when(view.getQueryNumber()).thenReturn(queryNumber);
        when(view.getId()).thenReturn(studentId, courseId);
        doNothing().when(studentDao).deleteRelation(studentId, courseId);

        controller.run(tablesCreationScriptFilepath, coursesDataFilepath, firstNameDataFilepath,
                lastNameDataFilepath, amountOfGroups, amountOfStudents, maximumCoursesPerStudent,
                range);

        verify(tableCreator).createTables(tablesCreationScriptFilepath);
        verify(groupGenerator).generateGroups(amountOfGroups);
        verify(fileValidator).validate(coursesDataFilepath);
        verify(fileReader).readFile(coursesDataFilepath);
        verify(courseGenerator).generateCourses(courseData);
        verify(fileValidator).validate(firstNameDataFilepath);
        verify(fileValidator).validate(lastNameDataFilepath);
        verify(fileReader).readFile(firstNameDataFilepath);
        verify(fileReader).readFile(lastNameDataFilepath);
        verify(studentGenerator).generateStudents(firstNames, lastNames, amountOfStudents);
        verify(groupToStudent).assignGroupToStudents(builders, groups, range);
        verify(coursesToStudent).assignCoursesToStudent(builders, courses,
                maximumCoursesPerStudent);
        verify(courseDao).saveAll(courses);
        verify(groupDao).saveAll(groups);
        verify(studentDao).saveAll(students);
        verify(studentDao).insertStudentCourses(students);
        verify(view, times(2)).getId();
        verify(studentDao).deleteRelation(studentId, courseId);
    }

    @Test
    void runShouldThrowDBRuntimeExceptionIfTablesCreationFilepathIsEmpty() {
        String tablesCreationScriptFilepath = "";
        String coursesDataFilepath = "src/test/resources/data/coursesDataTest.txt";
        String firstNameDataFilepath = "src/test/resources/data/firstNameDataTest.txt";
        String lastNameDataFilepath = "src/test/resources/data/lastNameDataTest.txt";

        int amountOfGroups = 10;
        int amountOfStudents = 200;
        int maximumCoursesPerStudent = 3;
        GroupCountRange range = new GroupCountRange(10, 30);

        doThrow(new DBRuntimeException("Tables creation failed")).when(tableCreator)
        .createTables(tablesCreationScriptFilepath);

        Exception exception = assertThrows(DBRuntimeException.class, () -> {
            controller.run(tablesCreationScriptFilepath, coursesDataFilepath, firstNameDataFilepath,
                    lastNameDataFilepath, amountOfGroups, amountOfStudents,
                    maximumCoursesPerStudent, range);
        });

        String expectedMessage = "Tables creation failed";
        String actualMessage = exception.getMessage();

        verify(tableCreator).createTables(tablesCreationScriptFilepath);
        verifyNoInteractions(groupGenerator);
        verifyNoInteractions(fileValidator);
        verifyNoInteractions(fileReader);
        verifyNoInteractions(courseGenerator);
        verifyNoInteractions(studentGenerator);
        verifyNoInteractions(groupToStudent);
        verifyNoInteractions(coursesToStudent);
        verifyNoInteractions(courseDao);
        verifyNoInteractions(groupDao);
        verifyNoInteractions(studentDao);
        verifyNoInteractions(view);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void runShouldThrowIllegalArgumentExceptionIfCoursesDataFilepathIsNull() {
        String tablesCreationScriptFilepath = "src/test/resources/tableCreation.sql";
        String coursesDataFilepath = null;
        String firstNameDataFilepath = "src/test/resources/data/firstNameDataTest.txt";
        String lastNameDataFilepath = "src/test/resources/data/lastNameDataTest.txt";

        int amountOfGroups = 10;
        int amountOfStudents = 200;
        int maximumCoursesPerStudent = 3;
        GroupCountRange range = new GroupCountRange(10, 30);

        List<Group> groups = new ArrayList<>();

        doNothing().when(tableCreator).createTables(tablesCreationScriptFilepath);
        when(groupGenerator.generateGroups(amountOfGroups)).thenReturn(groups);
        doThrow(new IllegalArgumentException("One or more file paths are null")).when(fileValidator)
        .validate(coursesDataFilepath);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.run(tablesCreationScriptFilepath, coursesDataFilepath, firstNameDataFilepath,
                    lastNameDataFilepath, amountOfGroups, amountOfStudents,
                    maximumCoursesPerStudent, range);
        });

        String expectedMessage = "One or more file paths are null";
        String actualMessage = exception.getMessage();

        verify(tableCreator).createTables(tablesCreationScriptFilepath);
        verify(groupGenerator).generateGroups(amountOfGroups);
        verify(fileValidator).validate(coursesDataFilepath);
        verifyNoInteractions(fileReader);
        verifyNoInteractions(courseGenerator);
        verifyNoInteractions(studentGenerator);
        verifyNoInteractions(groupToStudent);
        verifyNoInteractions(coursesToStudent);
        verifyNoInteractions(courseDao);
        verifyNoInteractions(groupDao);
        verifyNoInteractions(studentDao);
        verifyNoInteractions(view);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void runShouldThrowIOExceptionIfFileReadFailed() {
        String tablesCreationScriptFilepath = "src/test/resources/tableCreation.sql";
        String coursesDataFilepath = "src/test/resources/data/coursesDataTest.txt";
        String firstNameDataFilepath = "src/test/resources/data/firstNameDataTest.txt";
        String lastNameDataFilepath = "src/test/resources/data/lastNameDataTest.txt";

        int amountOfGroups = 10;
        int amountOfStudents = 200;
        int maximumCoursesPerStudent = 3;
        GroupCountRange range = new GroupCountRange(10, 30);

        List<Group> groups = new ArrayList<>();

        doNothing().when(tableCreator).createTables(tablesCreationScriptFilepath);
        when(groupGenerator.generateGroups(amountOfGroups)).thenReturn(groups);
        doNothing().when(fileValidator).validate(coursesDataFilepath);
        doThrow(new IllegalArgumentException("File read error")).when(fileReader)
        .readFile(coursesDataFilepath);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.run(tablesCreationScriptFilepath, coursesDataFilepath, firstNameDataFilepath,
                    lastNameDataFilepath, amountOfGroups, amountOfStudents,
                    maximumCoursesPerStudent, range);
        });

        String expectedMessage = "File read error";
        String actualMessage = exception.getMessage();

        verify(tableCreator).createTables(tablesCreationScriptFilepath);
        verify(groupGenerator).generateGroups(amountOfGroups);
        verify(fileValidator).validate(coursesDataFilepath);
        verify(fileReader).readFile(coursesDataFilepath);
        verifyNoInteractions(courseGenerator);
        verifyNoInteractions(studentGenerator);
        verifyNoInteractions(groupToStudent);
        verifyNoInteractions(coursesToStudent);
        verifyNoInteractions(courseDao);
        verifyNoInteractions(groupDao);
        verifyNoInteractions(studentDao);
        verifyNoInteractions(view);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void runShouldThrowUnsupportedOperationExceptionIfQueryNumberIsNotImplemented() {
        String tablesCreationScriptFilepath = "src/test/resources/tableCreation.sql";
        String coursesDataFilepath = "src/test/resources/data/coursesDataTest.txt";
        String firstNameDataFilepath = "src/test/resources/data/firstNameDataTest.txt";
        String lastNameDataFilepath = "src/test/resources/data/lastNameDataTest.txt";

        List<String> courseData = new ArrayList<>();
        List<String> firstNames = new ArrayList<>();
        List<String> lastNames = new ArrayList<>();

        int amountOfGroups = 10;
        int amountOfStudents = 200;
        int maximumCoursesPerStudent = 3;
        int queryNumber = 10;
        GroupCountRange range = new GroupCountRange(10, 30);

        List<Group> groups = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        List<Student.Builder> builders = new ArrayList<>();
        List<Student> students = new ArrayList<>();

        doNothing().when(tableCreator).createTables(tablesCreationScriptFilepath);
        when(groupGenerator.generateGroups(amountOfGroups)).thenReturn(groups);
        doNothing().when(fileValidator).validate(coursesDataFilepath);
        when(fileReader.readFile(coursesDataFilepath)).thenReturn(courseData);
        when(courseGenerator.generateCourses(courseData)).thenReturn(courses);
        doNothing().when(fileValidator).validate(firstNameDataFilepath);
        doNothing().when(fileValidator).validate(lastNameDataFilepath);
        when(fileReader.readFile(firstNameDataFilepath)).thenReturn(firstNames);
        when(fileReader.readFile(lastNameDataFilepath)).thenReturn(lastNames);
        when(studentGenerator.generateStudents(firstNames, lastNames, amountOfStudents))
        .thenReturn(builders);
        doNothing().when(groupToStudent).assignGroupToStudents(builders, groups, range);
        doNothing().when(coursesToStudent).assignCoursesToStudent(builders, courses,
                maximumCoursesPerStudent);
        doNothing().when(courseDao).saveAll(courses);
        doNothing().when(groupDao).saveAll(groups);
        doNothing().when(studentDao).saveAll(students);
        doNothing().when(studentDao).insertStudentCourses(students);
        when(view.getQueryNumber()).thenReturn(queryNumber);

        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            controller.run(tablesCreationScriptFilepath, coursesDataFilepath, firstNameDataFilepath,
                    lastNameDataFilepath, amountOfGroups, amountOfStudents,
                    maximumCoursesPerStudent, range);
        });

        String expectedMessage = "Wrong query input";
        String actualMessage = exception.getMessage();

        verify(tableCreator).createTables(tablesCreationScriptFilepath);
        verify(groupGenerator).generateGroups(amountOfGroups);
        verify(fileValidator).validate(coursesDataFilepath);
        verify(fileReader).readFile(coursesDataFilepath);
        verify(courseGenerator).generateCourses(courseData);
        verify(fileValidator).validate(firstNameDataFilepath);
        verify(fileValidator).validate(lastNameDataFilepath);
        verify(fileReader).readFile(firstNameDataFilepath);
        verify(fileReader).readFile(lastNameDataFilepath);
        verify(studentGenerator).generateStudents(firstNames, lastNames, amountOfStudents);
        verify(groupToStudent).assignGroupToStudents(builders, groups, range);
        verify(coursesToStudent).assignCoursesToStudent(builders, courses,
                maximumCoursesPerStudent);
        verify(courseDao).saveAll(courses);
        verify(groupDao).saveAll(groups);
        verify(studentDao).saveAll(students);
        verify(studentDao).insertStudentCourses(students);
        verify(view).getQueryNumber();

        assertEquals(expectedMessage, actualMessage);
    }
}
