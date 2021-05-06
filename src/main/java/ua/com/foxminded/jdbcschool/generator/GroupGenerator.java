package ua.com.foxminded.jdbcschool.generator;

import java.util.List;
import ua.com.foxminded.jdbcschool.domain.Group;

public interface GroupGenerator {
    List<Group> generateGroups(int amountOfGroups);
}
