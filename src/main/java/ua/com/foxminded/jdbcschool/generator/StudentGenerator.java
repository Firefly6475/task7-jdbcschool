package ua.com.foxminded.jdbcschool.generator;

import java.util.List;
import ua.com.foxminded.jdbcschool.domain.Student;

public interface StudentGenerator {
    List<Student.Builder> generateStudents(List<String> firstNames, List<String> lastNames,
            int amountOfStudents);
}
