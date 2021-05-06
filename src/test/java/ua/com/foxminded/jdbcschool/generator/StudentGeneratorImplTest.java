package ua.com.foxminded.jdbcschool.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.jdbcschool.domain.Student;

@ExtendWith(MockitoExtension.class)
public class StudentGeneratorImplTest {

    @Mock
    private Random random;
    
    @InjectMocks
    private StudentGeneratorImpl studentGenerator;

    @Test
    void generateStudentsShouldReturn200RandomlyGeneratedStudents() {
        int amountOfStudents = 200;

        List<String> firstNames = new ArrayList<>();
        firstNames.add("James");
        firstNames.add("Lisa");
        firstNames.add("John");

        List<String> lastNames = new ArrayList<>();
        lastNames.add("Thompson");
        lastNames.add("White");
        lastNames.add("Smith");

        List<Student.Builder> studentBuilders =
                studentGenerator.generateStudents(firstNames, lastNames, amountOfStudents);
        List<Student> students = studentBuilders.stream().map(builder -> builder.build()).collect(Collectors.toList());
        assertEquals(200, studentBuilders.size());
        students.forEach(student -> assertTrue(firstNames.contains(student.getFirstName())
                && lastNames.contains(student.getLastName())));
    }
}
