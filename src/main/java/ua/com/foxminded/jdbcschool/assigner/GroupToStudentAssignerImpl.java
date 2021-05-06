package ua.com.foxminded.jdbcschool.assigner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import ua.com.foxminded.jdbcschool.domain.Group;
import ua.com.foxminded.jdbcschool.domain.GroupCountRange;
import ua.com.foxminded.jdbcschool.domain.Student;

public class GroupToStudentAssignerImpl implements GroupToStudentAssigner {
    private final Random random;

    public GroupToStudentAssignerImpl(Random random) {
        this.random = random;
    }

    @Override
    public void assignGroupToStudents(List<Student.Builder> studentBuilders, List<Group> groups,
            GroupCountRange range) {
        int amountOfAssignings = random.nextInt(studentBuilders.size());
        int studentNumber = 0;
        Map<Group, Integer> groupToStudentCount = new HashMap<>();
        groups.forEach(group -> groupToStudentCount.put(group, 0));
        while (amountOfAssignings >= 1) {
            int groupNumber = random.nextInt(groups.size());
            Group group = groups.get(groupNumber);
            int groupStudentCount = groupToStudentCount.get(group);
            if (groupStudentCount < range.getMinimalGroupCount() || groupStudentCount < range.getMaximumGroupCount()) {
                studentBuilders.get(studentNumber).withGroup(groups.get(groupNumber));
                groupToStudentCount.put(group, groupStudentCount + 1);
                studentNumber++;
                amountOfAssignings--;
            }
        }
    }
}
