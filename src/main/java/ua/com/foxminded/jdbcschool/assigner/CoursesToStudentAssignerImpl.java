package ua.com.foxminded.jdbcschool.assigner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import ua.com.foxminded.jdbcschool.domain.Course;
import ua.com.foxminded.jdbcschool.domain.Student;

public class CoursesToStudentAssignerImpl implements CoursesToStudentAssigner {
    private final Random random;

    public CoursesToStudentAssignerImpl(Random random) {
        this.random = random;
    }

    @Override
    public void assignCoursesToStudent(List<Student.Builder> studentBuilder, List<Course> courses,
            int maximumCoursesPerStudent) {
        studentBuilder.forEach(student -> {
            int amountOfCourses = random.nextInt(maximumCoursesPerStudent) + 1;
            List<Course> studentsCourses = new ArrayList<>();
            while (amountOfCourses > 0) {
                studentsCourses.add(courses.get(random.nextInt(courses.size())));
                amountOfCourses--;
            }
            student.withCourses(studentsCourses.stream().distinct().collect(Collectors.toList()));
        });
    }
}
