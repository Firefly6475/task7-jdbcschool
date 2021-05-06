package ua.com.foxminded.jdbcschool.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import ua.com.foxminded.jdbcschool.assigner.CoursesToStudentAssigner;
import ua.com.foxminded.jdbcschool.assigner.GroupToStudentAssigner;
import ua.com.foxminded.jdbcschool.dao.CourseDao;
import ua.com.foxminded.jdbcschool.dao.GroupDao;
import ua.com.foxminded.jdbcschool.dao.StudentDao;
import ua.com.foxminded.jdbcschool.dao.TableCreator;
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

public class Controller {
    private final View view;

    private final TableCreator tableCreator;
    private final GroupGenerator groupGenerator;
    private final CourseGenerator courseGenerator;
    private final StudentGenerator studentGenerator;
    private final CoursesToStudentAssigner coursesToStudent;
    private final GroupToStudentAssigner groupsToStudent;

    private final CourseDao courseDao;
    private final GroupDao groupDao;
    private final StudentDao studentDao;

    private final FileValidator fileValidator;
    private final FileReader fileReader;

    public Controller(View view, TableCreator tableCreator, GroupGenerator groupGenerator,
            CourseGenerator courseGenerator, StudentGenerator studentGenerator,
            CoursesToStudentAssigner coursesToStudent, GroupToStudentAssigner groupsToStudent,
            CourseDao courseDao, GroupDao groupDao, StudentDao studentDao,
            FileValidator fileValidator, FileReader fileReader) {
        this.view = view;
        this.tableCreator = tableCreator;
        this.groupGenerator = groupGenerator;
        this.courseGenerator = courseGenerator;
        this.studentGenerator = studentGenerator;
        this.coursesToStudent = coursesToStudent;
        this.groupsToStudent = groupsToStudent;
        this.courseDao = courseDao;
        this.groupDao = groupDao;
        this.studentDao = studentDao;
        this.fileValidator = fileValidator;
        this.fileReader = fileReader;
    }

    public void run(String tablesCreationScriptFilepath, String coursesDataFilepath,
            String firstNameDataFilepath, String lastNameDataFilepath, int amountOfGroups, int amountOfStudents,
            int maximumCoursesPerStudent, GroupCountRange range) {

        tableCreator.createTables(tablesCreationScriptFilepath);
        List<Group> groups = groupGenerator.generateGroups(amountOfGroups);
        List<Course> courses = generateCourses(coursesDataFilepath);
        List<Student.Builder> students =
                generateStudents(firstNameDataFilepath, lastNameDataFilepath, amountOfStudents);

        groupsToStudent.assignGroupToStudents(students, groups, range);
        coursesToStudent.assignCoursesToStudent(students, courses, maximumCoursesPerStudent);

        List<Student> studentsList = students.stream().map(builder -> builder.build()).collect(Collectors.toList());
        
        courseDao.saveAll(courses);
        groupDao.saveAll(groups);
        studentDao.saveAll(studentsList);
        studentDao.insertStudentCourses(studentsList);

        showMenu();
        switch (getQueryNumber()) {
            case 1: {
                showAllGroupsWithLessOrEqualsStudentCount();
                break;
            }
            case 2: {
                showAllStudentsRelatedToCourse();
                break;
            }
            case 3: {
                addNewStudent();
                break;
            }
            case 4: {
                deleteStudentById();
                break;
            }
            case 5: {
                addStudentToTheCourse();
                break;
            }
            case 6: {
                removeStudentFromCourse();
                break;
            }
            case 7: {
                System.exit(0);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Wrong query input");
            }
        }
    }

    private List<Course> generateCourses(String coursesDataFilepath) {
        fileValidator.validate(coursesDataFilepath);
        List<String> coursesData = fileReader.readFile(coursesDataFilepath);

        return courseGenerator.generateCourses(coursesData);
    }

    private List<Student.Builder> generateStudents(String firstNameDataFilepath,
            String lastNameDataFilepath, int amount) {
        fileValidator.validate(firstNameDataFilepath);
        fileValidator.validate(lastNameDataFilepath);

        List<String> firstNames = fileReader.readFile(firstNameDataFilepath);
        List<String> lastNames = fileReader.readFile(lastNameDataFilepath);

        return studentGenerator.generateStudents(firstNames, lastNames, amount);
    }

    private void showMenu() {
        view.showMenu();
    }

    private int getQueryNumber() {
        return view.getQueryNumber();
    }

    private void showAllGroupsWithLessOrEqualsStudentCount() {
        int studentCount = view.getInteger();
        groupDao.findAllWithLessOrEqualsStudentCount(studentCount)
                .forEach(group -> view.printGroup(group));
    }

    private void showAllStudentsRelatedToCourse() {
        String courseName = view.getString();
        studentDao.findAllByCourseName(courseName).forEach(student -> view.printStudent(student));
    }

    protected void addNewStudent() {
        String firstName = view.getFirstName();
        String lastName = view.getLastName();
        Student student = Student.builder()
                .withId(generateId())
                .withGroup(null)
                .withFirstName(firstName)
                .withLastName(lastName)
                .build();
        studentDao.save(student);
    }
    
    protected String generateId() {
        return UUID.randomUUID().toString();
    }

    private void deleteStudentById() {
        String id = view.getId();
        studentDao.deleteById(id);
    }

    private void addStudentToTheCourse() {
        String studentId = view.getId();
        String courseId = view.getId();
        studentDao.saveRelation(studentId, courseId);
    }

    private void removeStudentFromCourse() {
        String studentId = view.getId();
        String courseId = view.getId();
        studentDao.deleteRelation(studentId, courseId);
    }
}
