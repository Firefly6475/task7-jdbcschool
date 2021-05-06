package ua.com.foxminded.jdbcschool;

import java.util.Random;
import ua.com.foxminded.jdbcschool.assigner.CoursesToStudentAssigner;
import ua.com.foxminded.jdbcschool.assigner.CoursesToStudentAssignerImpl;
import ua.com.foxminded.jdbcschool.assigner.GroupToStudentAssigner;
import ua.com.foxminded.jdbcschool.assigner.GroupToStudentAssignerImpl;
import ua.com.foxminded.jdbcschool.controller.Controller;
import ua.com.foxminded.jdbcschool.dao.CourseDao;
import ua.com.foxminded.jdbcschool.dao.DBConnector;
import ua.com.foxminded.jdbcschool.dao.GroupDao;
import ua.com.foxminded.jdbcschool.dao.StudentDao;
import ua.com.foxminded.jdbcschool.dao.TableCreator;
import ua.com.foxminded.jdbcschool.dao.impl.CourseDaoImpl;
import ua.com.foxminded.jdbcschool.dao.impl.GroupDaoImpl;
import ua.com.foxminded.jdbcschool.dao.impl.StudentDaoImpl;
import ua.com.foxminded.jdbcschool.domain.GroupCountRange;
import ua.com.foxminded.jdbcschool.generator.CourseGenerator;
import ua.com.foxminded.jdbcschool.generator.CourseGeneratorImpl;
import ua.com.foxminded.jdbcschool.generator.GroupGenerator;
import ua.com.foxminded.jdbcschool.generator.GroupGeneratorImpl;
import ua.com.foxminded.jdbcschool.generator.StudentGenerator;
import ua.com.foxminded.jdbcschool.generator.StudentGeneratorImpl;
import ua.com.foxminded.jdbcschool.reader.FileReader;
import ua.com.foxminded.jdbcschool.reader.FileReaderImpl;
import ua.com.foxminded.jdbcschool.validator.FileValidator;
import ua.com.foxminded.jdbcschool.validator.FileValidatorImpl;
import ua.com.foxminded.jdbcschool.view.ConsoleView;
import ua.com.foxminded.jdbcschool.view.View;

public class JdbcSchoolConsoleApplication {

    public static void main(String[] args) {
        Random random = new Random();
        DBConnector connector = new DBConnector("src/main/resources/db.properties");
        View view = new ConsoleView();
        TableCreator tableCreator = new TableCreator(connector);
        GroupGenerator groupGenerator = new GroupGeneratorImpl(random);
        CourseGenerator courseGenerator = new CourseGeneratorImpl();
        StudentGenerator studentGenerator = new StudentGeneratorImpl(random);
        CoursesToStudentAssigner coursesToStudent = new CoursesToStudentAssignerImpl(random);
        GroupToStudentAssigner groupsToStudent = new GroupToStudentAssignerImpl(random);
        CourseDao courseDao = new CourseDaoImpl(connector);
        GroupDao groupDao = new GroupDaoImpl(connector);
        StudentDao studentDao = new StudentDaoImpl(connector);
        FileValidator fileValidator = new FileValidatorImpl();
        FileReader fileReader = new FileReaderImpl();

        String tablesCreationScriptFilepath = "src/main/resources/tableCreation.sql";
        String coursesFilepath = "src/main/resources/data/coursesData.txt";
        String firstNameFilepath = "src/main/resources/data/firstNameData.txt";
        String lastNameFilepath = "src/main/resources/data/lastNameData.txt";
        int amountOfGroups = 10;
        int amountOfStudents = 200;
        int maximumCoursesPerStudent = 3;
        GroupCountRange range = new GroupCountRange(10, 30);

        Controller controller = new Controller(view, tableCreator, groupGenerator, courseGenerator,
                studentGenerator, coursesToStudent, groupsToStudent, courseDao, groupDao,
                studentDao, fileValidator, fileReader);

        controller.run(tablesCreationScriptFilepath, coursesFilepath, firstNameFilepath,
                lastNameFilepath, amountOfGroups, amountOfStudents, maximumCoursesPerStudent, range);
    }
}
