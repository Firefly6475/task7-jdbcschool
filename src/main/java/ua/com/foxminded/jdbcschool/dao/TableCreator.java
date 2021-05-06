package ua.com.foxminded.jdbcschool.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;
import ua.com.foxminded.jdbcschool.dao.exception.DBRuntimeException;

public class TableCreator {
    private final DBConnector dbConnector;

    public TableCreator(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public void createTables(String tablesCreationScriptFilepath) {
        try (Connection connection = dbConnector.getConnection();
             Statement statement = connection.createStatement()) {
            List <String> scripts = Files.lines(Paths.get(tablesCreationScriptFilepath)).collect(Collectors.toList());
            for (String query : scripts) {
                statement.execute(query);
            }
        } catch (SQLException | IOException e) {
            throw new DBRuntimeException("Tables creation failed", e);
        }
    }
}
