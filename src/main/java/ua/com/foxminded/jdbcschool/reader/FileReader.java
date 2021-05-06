package ua.com.foxminded.jdbcschool.reader;

import java.util.List;

public interface FileReader {
    List<String> readFile(String path);
}
