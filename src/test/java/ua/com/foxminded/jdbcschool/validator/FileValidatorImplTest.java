package ua.com.foxminded.jdbcschool.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class FileValidatorImplTest {
    private final FileValidator fileValidator = new FileValidatorImpl();

    @Test
    void validateShouldThrowIllegalArgumentExceptionIfFilePathIsNull() {
        String fileName = null;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            fileValidator.validate(fileName);
        });

        String expectedMessage = "One or more file paths are null";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateShouldThrowIllegalArgumentExceptionIfFilePathIsEmpty() {
        String fileName = "";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            fileValidator.validate(fileName);
        });

        String expectedMessage = "One or more file paths are empty";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateShouldThrowIllegalArgumentExceptionIfFilePathIsDirectory() {
        String fileName = "src/test/resources/data";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            fileValidator.validate(fileName);
        });

        String expectedMessage = "One or more paths are directories";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateShouldThrowIllegalArgumentExceptionIfFileIsNotExists() {
        String fileName = "src/test/data/hello.txt";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            fileValidator.validate(fileName);
        });

        String expectedMessage = "One or more files are not exist";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateShouldNotThrowIllegalArgumentException() {
        String fileName = "src/test/resources/data/lastNameDataTest.txt";

        assertDoesNotThrow(() -> fileValidator.validate(fileName));
    }
}
