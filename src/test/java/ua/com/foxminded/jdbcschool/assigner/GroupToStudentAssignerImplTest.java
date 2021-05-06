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
import ua.com.foxminded.jdbcschool.domain.Group;
import ua.com.foxminded.jdbcschool.domain.GroupCountRange;
import ua.com.foxminded.jdbcschool.domain.Student;

@ExtendWith(MockitoExtension.class)
public class GroupToStudentAssignerImplTest {

    @Mock
    private Random random;
    
    @InjectMocks
    private GroupToStudentAssignerImpl groupStudentAssigner;

    @Test
    void assignGroupToStudentsShouldAssignGroupToStudent() {
        int minimalGroupCount = 10;
        int maximumGroupCount = 30;
        GroupCountRange range = new GroupCountRange(minimalGroupCount, maximumGroupCount);
        
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
        

        List<Group> groups = new ArrayList<>();
        groups.add(Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("AB-12")
                .build());
        groups.add(Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("CD-34")
                .build());
        groups.add(Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("EF-56")
                .build());
        
        when(random.nextInt(studentBuilders.size())).thenReturn(2);
        when(random.nextInt(groups.size())).thenReturn(2);
        
        groupStudentAssigner.assignGroupToStudents(studentBuilders, groups, range);
        
        List<Student> students = studentBuilders.stream().map(builder -> builder.build()).collect(Collectors.toList());
                
        students.forEach(student -> assertTrue(
                groups.contains(student.getGroup()) || student.getGroup() == null));
    }
}
