package ua.com.foxminded.jdbcschool.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import ua.com.foxminded.jdbcschool.dao.DBConnector;
import ua.com.foxminded.jdbcschool.dao.StudentDao;
import ua.com.foxminded.jdbcschool.domain.Group;
import ua.com.foxminded.jdbcschool.domain.Student;

public class StudentDaoImpl extends AbstractCrudDaoImpl<Student> implements StudentDao {

    private static final String SAVE_QUERY =
            "INSERT INTO students (student_id, group_id, first_name, last_name) VALUES (?,?,?,?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM students WHERE student_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM students";
    private static final String FIND_ALL_PAGED_QUERY = "SELECT * FROM students LIMIT ? OFFSET ?";
    private static final String FIND_RELATION_QUERY =
            "SELECT * FROM students_courses WHERE student_id = ? AND course_id = ?";
    private static final String UPDATE_QUERY =
            "UPDATE students SET student_id = ?, group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM students WHERE student_id = ?";
    private static final String GET_STUDENTS_WITH_GIVEN_COURSE_QUERY =
            "SELECT students.student_id, students.group_id, students.first_name, students.last_name "
                    + "FROM students_courses JOIN students ON students_courses.student_id = students.student_id "
                    + "WHERE course_id = (SELECT course_id FROM courses WHERE course_name = ?)";
    private static final String SAVE_RELATION_QUERY =
            "INSERT INTO students_courses (student_id, course_id) VALUES (?,?)";
    private static final String DELETE_RELATION =
            "DELETE FROM students_courses WHERE student_id = ? AND course_id = ?";

    public StudentDaoImpl(DBConnector connector) {
        super(connector, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGED_QUERY,
                UPDATE_QUERY, DELETE_BY_ID_QUERY);
    }

    @Override
    public List<Student> findAllByCourseName(String courseName) {
        return findAllByStringParam(courseName, GET_STUDENTS_WITH_GIVEN_COURSE_QUERY);
    }

    @Override
    public void insertStudentCourses(List<Student> students) {
        Map<String, List<String>> studentIdToCoursesIds = new HashMap<>();
        students.forEach(student -> studentIdToCoursesIds.put(student.getId(), student.getCourses()
                .stream().map(course -> course.getId()).collect(Collectors.toList())));
        saveAllRelation(studentIdToCoursesIds, SAVE_RELATION_QUERY);
    }

    @Override
    public void saveRelation(String studentId, String courseId) {
        saveRelation(studentId, courseId, SAVE_RELATION_QUERY);
    }

    @Override
    public void deleteRelation(String studentId, String courseId) {
        deleteRelation(studentId, courseId, DELETE_RELATION);
    }

    @Override
    public List<String> findRelation(String studentId, String courseId) {
        return findRelation(studentId, courseId, FIND_RELATION_QUERY);

    }

    @Override
    protected Student mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return Student.builder().withId(resultSet.getString("student_id"))
                .withGroup(Group.builder().withId(resultSet.getString("group_id")).build())
                .withFirstName(resultSet.getString("first_name"))
                .withLastName(resultSet.getString("last_name")).build();
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, Student entity) throws SQLException {
        preparedStatement.setString(1, entity.getId());
        if (entity.getGroup() == null) {
            preparedStatement.setNull(2, java.sql.Types.INTEGER);
        } else {
            preparedStatement.setString(2, entity.getGroup().getId());
        }
        preparedStatement.setString(3, entity.getFirstName());
        preparedStatement.setString(4, entity.getLastName());
    }

    @Override
    protected void updateValues(PreparedStatement preparedStatement, Student entity)
            throws SQLException {
        insert(preparedStatement, entity);
        preparedStatement.setString(5, entity.getId());
    }
}
