package ua.com.foxminded.jdbcschool.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.jdbcschool.dao.exception.DBRuntimeException;

public class TableCreatorTest {
    DBConnector connector = new DBConnector("src/test/resources/db-test.properties");
    private final TableCreator tableCreator = new TableCreator(connector);
    
    @Test
    void createTableShouldThrowDBRuntimeExceptionIfFilepathIsEmpty() {
        String tablesCreationScriptFilepath = "";
        
        Exception exception = assertThrows(DBRuntimeException.class, () -> {
            tableCreator.createTables(tablesCreationScriptFilepath);
        });
        
        String expectedMessage = "Tables creation failed";
        String actualMessage = exception.getMessage();
        
        assertEquals(expectedMessage, actualMessage);
    }
}
