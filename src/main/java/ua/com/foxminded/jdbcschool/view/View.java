package ua.com.foxminded.jdbcschool.view;

import ua.com.foxminded.jdbcschool.domain.Course;
import ua.com.foxminded.jdbcschool.domain.Group;
import ua.com.foxminded.jdbcschool.domain.Student;

public interface View {
    void showMenu();
    
    int getQueryNumber();
    
    int getInteger();
    
    String getString();
    
    void printGroup(Group group);
    
    void printStudent(Student student);
    
    void printCourse(Course course);
    
    String getFirstName();
    
    String getLastName();
    
    String getId();
}
