package ua.com.foxminded.jdbcschool.assigner;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.jdbcschool.domain.Course;
import ua.com.foxminded.jdbcschool.domain.Student;

@ExtendWith(MockitoExtension.class)
public class CoursesToStudentAssignerImplTest {

    @Mock
    private Random random;
    
    @InjectMocks
    private CoursesToStudentAssignerImpl courseStudentAssigner;
    
    @Test
    void assignCoursesToStudentShouldAssignCoursesToStudent() {
        int maximumCoursesPerStudent = 3;
        List<Student.Builder> studentBuilders = new ArrayList<>();
        studentBuilders.add(Student.builder()
                .withId(UUID.randomUUID().toString())
                .withFirstName("Jonh")
                .withLastName("White"));
        studentBuilders.add(Student.builder()
                .withId(UUID.randomUUID().toString())
                .withFirstName("Lisa")
                .withLastName("Johnson"));
        studentBuilders.add(Student.builder()
                .withId(UUID.randomUUID().toString())
                .withFirstName("Eric")
                .withLastName("Carlson"));
        
        List<Course> courses = new ArrayList<>();
        courses.add(Course.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Math")
                .withDescription("This is Math")
                .build());
        courses.add(Course.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Physics")
                .withDescription("This is Physics")
                .build());
        courses.add(Course.builder()
                .withId(UUID.randomUUID().toString())
                .withName("Chemistry")
                .withDescription("This is Chemistry")
                .build());
        
        when(random.nextInt(maximumCoursesPerStudent)).thenReturn(2);
        when(random.nextInt(courses.size())).thenReturn(2);
        
        courseStudentAssigner.assignCoursesToStudent(studentBuilders, courses, maximumCoursesPerStudent);
        List<Student> students = studentBuilders.stream().map(builder -> builder.build()).collect(Collectors.toList());
        
        students.forEach(student -> assertTrue(courses.containsAll(student.getCourses())));
        students.forEach(student -> assertTrue(student.getCourses().size() <= 3));
    }
}
