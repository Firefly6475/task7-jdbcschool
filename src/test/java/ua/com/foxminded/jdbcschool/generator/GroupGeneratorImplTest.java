package ua.com.foxminded.jdbcschool.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.jdbcschool.domain.Group;

@ExtendWith(MockitoExtension.class)
public class GroupGeneratorImplTest {
    
    @Mock
    private Random random;
    
    @InjectMocks
    private GroupGeneratorImpl groupGenerator;
    
    @Test
    void generateGroupsShouldReturn10RandomlyGeneratedGroups() {
        String regex = "[A-Z]{2}-[0-9]{2}";
        int amountOfGroups = 10;
        List<Group> groups = groupGenerator.generateGroups(amountOfGroups);
        groups.forEach(group -> assertTrue(group.getName().matches(regex)));
        assertEquals(10, groups.size());
    }
}
