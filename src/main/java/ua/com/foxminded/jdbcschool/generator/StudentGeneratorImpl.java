package ua.com.foxminded.jdbcschool.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import ua.com.foxminded.jdbcschool.domain.Student;

public class StudentGeneratorImpl implements StudentGenerator {
    private final Random random;

    public StudentGeneratorImpl(Random random) {
        this.random = random;
    }

    @Override
    public List<Student.Builder> generateStudents(List<String> firstNames, List<String> lastNames,
            int amountOfStudents) {
        List<Student.Builder> students = new ArrayList<>();
        for (int i = 0; i < amountOfStudents; i++) {
            String firstName = firstNames.get(random.nextInt(firstNames.size()));
            String lastName = lastNames.get(random.nextInt(lastNames.size()));
            students.add(Student.builder()
                    .withId(UUID.randomUUID().toString())
                    .withFirstName(firstName)
                    .withLastName(lastName));
        }
        return students;
    }
}
