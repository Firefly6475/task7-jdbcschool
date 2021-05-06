package ua.com.foxminded.jdbcschool.dao;

import java.util.List;
import ua.com.foxminded.jdbcschool.domain.Group;

public interface GroupDao extends CrudDao<Group> {    
    List<Group> findAllWithLessOrEqualsStudentCount(Integer studentCount);
}
