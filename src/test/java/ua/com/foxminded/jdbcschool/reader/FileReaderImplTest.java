package ua.com.foxminded.jdbcschool.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class FileReaderImplTest {
    private final FileReader fileReader = new FileReaderImpl();
  
    @Test
    void readFileShouldReturnLinesList() {
        String filepath = "src/test/resources/data/coursesDataTest.txt";
        
        List<String> expectedLines = new ArrayList<>();
        expectedLines.add("Math");
        expectedLines.add("Biology");
        expectedLines.add("Physics");
        
        List<String> actualLines = fileReader.readFile(filepath);
        assertEquals(expectedLines, actualLines);
    }
    
    @Test
    void readFileShouldThrowIOExceptionIfFileNotExists() {
        String filepath = "";
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            fileReader.readFile(filepath);
        });
        
        String expectedMessage = "File read error";
        String actualMessage = exception.getMessage();
        
        assertEquals(expectedMessage, actualMessage);
    }
}
