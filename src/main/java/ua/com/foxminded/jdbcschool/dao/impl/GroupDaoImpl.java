package ua.com.foxminded.jdbcschool.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import ua.com.foxminded.jdbcschool.dao.DBConnector;
import ua.com.foxminded.jdbcschool.dao.GroupDao;
import ua.com.foxminded.jdbcschool.domain.Group;

public class GroupDaoImpl extends AbstractCrudDaoImpl<Group> implements GroupDao {

    private static final String SAVE_QUERY = "INSERT INTO groups (group_id, group_name) VALUES (?,?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM groups WHERE group_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM groups";
    private static final String FIND_ALL_PAGED_QUERY = "SELECT * FROM groups LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE groups SET group_id = ?, group_name = ? WHERE group_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM groups WHERE group_id = ?";
    private static final String FIND_BY_STUDENT_COUNT_QUERY = "SELECT groups.group_id, group_name, COUNT(*) FROM groups JOIN students ON students.group_id = groups.group_id GROUP BY group_name, groups.group_id HAVING COUNT(*) <= ?";
    
    public GroupDaoImpl(DBConnector connector) {
        super(connector, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGED_QUERY, UPDATE_QUERY, DELETE_BY_ID_QUERY);
    }

    @Override
    public List<Group> findAllWithLessOrEqualsStudentCount(Integer studentCount) {
        return findAllByIntParam(studentCount, FIND_BY_STUDENT_COUNT_QUERY);
    }
   
    @Override
    protected void insert(PreparedStatement preparedStatement, Group entity) throws SQLException {
        preparedStatement.setString(1, entity.getId());
        preparedStatement.setString(2, entity.getName());
    }

    @Override
    protected Group mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return Group.builder()
                .withId(resultSet.getString("group_id"))
                .withName(resultSet.getString("group_name"))
                .build();
    }

    @Override
    protected void updateValues(PreparedStatement preparedStatement, Group entity)
            throws SQLException {
        insert(preparedStatement, entity);
        preparedStatement.setString(3, entity.getId());
    }
}
