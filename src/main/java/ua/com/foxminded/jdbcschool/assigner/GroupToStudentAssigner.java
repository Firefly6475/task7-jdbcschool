package ua.com.foxminded.jdbcschool.assigner;

import java.util.List;
import ua.com.foxminded.jdbcschool.domain.Group;
import ua.com.foxminded.jdbcschool.domain.GroupCountRange;
import ua.com.foxminded.jdbcschool.domain.Student;

public interface GroupToStudentAssigner {
    void assignGroupToStudents(List<Student.Builder> studentBuilders, List<Group> groups, GroupCountRange range);
}
