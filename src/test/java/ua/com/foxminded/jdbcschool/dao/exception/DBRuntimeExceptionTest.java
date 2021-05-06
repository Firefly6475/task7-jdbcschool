package ua.com.foxminded.jdbcschool.dao.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import java.sql.SQLException;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.jdbcschool.dao.DBConnector;
import ua.com.foxminded.jdbcschool.dao.impl.StudentDaoImpl;
import ua.com.foxminded.jdbcschool.domain.Student;

@ExtendWith(MockitoExtension.class)
public class DBRuntimeExceptionTest {

    @Mock
    private DBConnector connector;

    @InjectMocks
    private StudentDaoImpl studentDao;

    @Test
    void findAllStudentsShouldThrowDBRuntimeExceptionIfConnectionError() throws SQLException {     
        when(connector.getConnection()).thenThrow(new SQLException());

        Exception exception = assertThrows(DBRuntimeException.class, () -> {
            studentDao.findAll();
        });

        String expectedMessage = "Find all failed";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    void updateStudentShouldThrowDBRuntimeExceptionIfConnectionError() throws SQLException {
        Student student = Student.builder().build();
        
        when(connector.getConnection()).thenThrow(new SQLException());

        Exception exception = assertThrows(DBRuntimeException.class, () -> {
            studentDao.update(student);
        });

        String expectedMessage = "Update failed";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    void deleteStudentByIdShouldThrowDBRuntimeExceptionIfConnectionError() throws SQLException {
        when(connector.getConnection()).thenThrow(new SQLException());

        Exception exception = assertThrows(DBRuntimeException.class, () -> {
            studentDao.deleteById(UUID.randomUUID().toString());
        });

        String expectedMessage = "Delete by id failed";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    void saveRelationShouldThrowDBRuntimeExceptionIfConnectionError() throws SQLException {
        when(connector.getConnection()).thenThrow(new SQLException());

        Exception exception = assertThrows(DBRuntimeException.class, () -> {
            studentDao.saveRelation(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        });

        String expectedMessage = "Relation insert failed";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    void findRelationShouldThrowDBRuntimeExceptionIfConnectionError() throws SQLException {
        when(connector.getConnection()).thenThrow(new SQLException());

        Exception exception = assertThrows(DBRuntimeException.class, () -> {
            studentDao.findRelation(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        });

        String expectedMessage = "Find relation failed";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    void deleteRelationShouldThrowDBRuntimeExceptionIfConnectionError() throws SQLException {
        when(connector.getConnection()).thenThrow(new SQLException());

        Exception exception = assertThrows(DBRuntimeException.class, () -> {
            studentDao.deleteRelation(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        });

        String expectedMessage = "Delete relation failed";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
