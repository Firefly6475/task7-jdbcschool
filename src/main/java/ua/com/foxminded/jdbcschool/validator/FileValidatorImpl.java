package ua.com.foxminded.jdbcschool.validator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

public class FileValidatorImpl implements FileValidator {
    @Override
    public void validate (String dataFilePath) {
        if (isNull(dataFilePath)) {
            throw new IllegalArgumentException("One or more file paths are null");
        }
        if (isEmpty(dataFilePath)) {
            throw new IllegalArgumentException("One or more file paths are empty");
        }
        
        Path dataFile = Paths.get(dataFilePath);
        
        if (isDirectory(dataFile)) {
            throw new IllegalArgumentException("One or more paths are directories");
        }
        if (isNotExists(dataFile)) {
            throw new IllegalArgumentException("One or more files are not exist");
        }
    }
    
    private boolean isNull(String... filePaths) {
        return Arrays.stream(filePaths).anyMatch(Objects::isNull);
    }
    
    private boolean isEmpty(String... filePaths) {
        return Arrays.stream(filePaths).anyMatch(String::isEmpty);
    }
    
    private boolean isDirectory(Path... filePaths) {
        return Arrays.stream(filePaths).anyMatch(Files::isDirectory);
    }
    
    private boolean isNotExists(Path... filePaths) {
        return Arrays.stream(filePaths).anyMatch(Files::notExists);
    }
}
