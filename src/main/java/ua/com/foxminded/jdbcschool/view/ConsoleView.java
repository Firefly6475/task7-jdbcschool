package ua.com.foxminded.jdbcschool.view;

import java.util.Scanner;
import ua.com.foxminded.jdbcschool.domain.Course;
import ua.com.foxminded.jdbcschool.domain.Group;
import ua.com.foxminded.jdbcschool.domain.Student;

public class ConsoleView implements View {
    Scanner input = new Scanner(System.in);
    
    public void showMenu() {
        System.out.println("// Application Menu //");
        System.out.println("Please, choose desirable query");
        System.out.println("1. Find all groups with less or equals student count");
        System.out.println("2. Find all students related to course with given name");
        System.out.println("3. Add new student");
        System.out.println("4. Delete student by STUDENT_ID");
        System.out.println("5. Add student to the course from a list");
        System.out.println("6. Remove the student from one of his or her courses");
        System.out.println("7. Exit");
    }
    
    @Override
    public int getQueryNumber() {
        System.out.println("Specify query number");
        return input.nextInt();
    }
    
    @Override
    public int getInteger() {
        System.out.println("Specify integer");
        return input.nextInt();
    }
    
    @Override
    public String getString() {
        System.out.println("Specify string");
        return input.nextLine();
    }

    @Override
    public void printGroup(Group group) {
        System.out.println(group);        
    }

    @Override
    public void printStudent(Student student) {
        System.out.println(student);
    }
    
    @Override
    public String getFirstName() {
        System.out.println("Specify first name");
        return input.nextLine();
    }
    
    @Override
    public String getLastName() {
        System.out.println("Specify last name");
        return input.nextLine();
    }
    
    @Override
    public void printCourse(Course course) {
        System.out.println(course);
    }
    
    @Override
    public String getId() {
        System.out.println("Specify id");
        return input.nextLine();
    }
}
