package ua.com.foxminded.jdbcschool.reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileReaderImpl implements FileReader {
    @Override
    public List<String> readFile(String path) {
        Path filePath = Paths.get(path);
        try (Stream<String> lineStream = Files.lines(filePath)) {
            return lineStream.collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalArgumentException("File read error");
        }
    }
}
