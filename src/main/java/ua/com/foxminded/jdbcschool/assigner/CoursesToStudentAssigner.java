package ua.com.foxminded.jdbcschool.assigner;

import java.util.List;
import ua.com.foxminded.jdbcschool.domain.Course;
import ua.com.foxminded.jdbcschool.domain.Student;

public interface CoursesToStudentAssigner {
    void assignCoursesToStudent(List<Student.Builder> studentBuilders, List<Course> courses,
            int maximumCoursesPerStudent);
}
