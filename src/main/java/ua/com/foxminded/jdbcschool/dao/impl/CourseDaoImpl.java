package ua.com.foxminded.jdbcschool.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ua.com.foxminded.jdbcschool.dao.CourseDao;
import ua.com.foxminded.jdbcschool.dao.DBConnector;
import ua.com.foxminded.jdbcschool.domain.Course;

public class CourseDaoImpl extends AbstractCrudDaoImpl<Course> implements CourseDao {

    private static final String SAVE_QUERY = "INSERT INTO courses (course_id, course_name, course_description) VALUES (?,?,?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM courses WHERE course_id = ?";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM courses WHERE course_name = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM courses";
    private static final String FIND_ALL_PAGED_QUERY = "SELECT * FROM courses LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE courses SET course_id = ?, course_name = ?, course_description = ? WHERE course_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM courses WHERE course_id = ?";

    public CourseDaoImpl(DBConnector connector) {
        super(connector, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGED_QUERY, UPDATE_QUERY, DELETE_BY_ID_QUERY);
    }

    @Override
    public Course findByName(String name) {
        return findByStringParam(name, FIND_BY_NAME_QUERY).get();
    }   

    @Override
    protected void insert(PreparedStatement preparedStatement, Course entity) throws SQLException {
        preparedStatement.setString(1, entity.getId());
        preparedStatement.setString(2, entity.getName());
        preparedStatement.setString(3, entity.getDescription());
    }

    @Override
    protected Course mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return Course.builder()
                .withId(resultSet.getString("course_id"))
                .withName(resultSet.getString("course_name"))
                .withDescription(resultSet.getString("course_description"))
                .build();
    }

    @Override
    protected void updateValues(PreparedStatement preparedStatement, Course entity)
            throws SQLException {
        insert(preparedStatement, entity);
        preparedStatement.setString(4, entity.getId());
    }
}
