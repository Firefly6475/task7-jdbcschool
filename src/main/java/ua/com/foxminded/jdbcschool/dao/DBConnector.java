package ua.com.foxminded.jdbcschool.dao;

import java.sql.Connection;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBConnector {
    private final HikariConfig config;
    private final HikariDataSource ds;
    
    public DBConnector(String fileConfigFile) {
        config = new HikariConfig(fileConfigFile);
        ds = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
