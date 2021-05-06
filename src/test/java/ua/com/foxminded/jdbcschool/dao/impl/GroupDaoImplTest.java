package ua.com.foxminded.jdbcschool.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.jdbcschool.dao.DBConnector;
import ua.com.foxminded.jdbcschool.dao.GroupDao;
import ua.com.foxminded.jdbcschool.dao.Page;
import ua.com.foxminded.jdbcschool.dao.StudentDao;
import ua.com.foxminded.jdbcschool.dao.TableCreator;
import ua.com.foxminded.jdbcschool.domain.Group;
import ua.com.foxminded.jdbcschool.domain.Student;

public class GroupDaoImplTest {
    private final DBConnector connector = new DBConnector("src/test/resources/db-test.properties");
    private final TableCreator tableCreator = new TableCreator(connector);
    private final GroupDao groupDao = new GroupDaoImpl(connector);
    private final StudentDao studentDao = new StudentDaoImpl(connector);
    
    @BeforeEach
    void createTables() {
        tableCreator.createTables("src/test/resources/tableCreation.sql");
    }
    
    @Test
    void saveAllShouldInsertListOfGroups() {
        Group firstGroup = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("AA-44")
                .build();
        Group secondGroup = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("BB-33")
                .build();
        List<Group> expectedGroups = new ArrayList<>();
        expectedGroups.add(firstGroup);
        expectedGroups.add(secondGroup);
        groupDao.saveAll(expectedGroups);
        
        List<Group> actualGroups = groupDao.findAll();
        assertEquals(expectedGroups, actualGroups);
    }
    
    @Test
    void getGroupsWithLessOrEqualsStudentCountShouldReturnGroupsWithLessOrEqualsStudentCount() {
        Group firstGroup = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("AA-33")
                .build();
        Group secondGroup = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("BB-44")
                .build();
        Group thirdGroup = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("CC-55")
                .build();
        Student firstStudent = Student.builder()
                .withId(UUID.randomUUID().toString())
                .withGroup(firstGroup)
                .withFirstName("Hello")
                .withLastName("World")
                .build();
        Student secondStudent = Student.builder()
                .withId(UUID.randomUUID().toString())
                .withGroup(firstGroup)
                .withFirstName("Hi")
                .withLastName("World")
                .build();
        Student thirdStudent = Student.builder()
                .withId(UUID.randomUUID().toString())
                .withGroup(secondGroup)
                .withFirstName("Greetings")
                .withLastName("World")
                .build();
        Student fourthStudent = Student.builder()
                .withId(UUID.randomUUID().toString())
                .withGroup(thirdGroup)
                .withFirstName("Greetings")
                .withLastName("World")
                .build();
        
        groupDao.save(firstGroup);
        groupDao.save(secondGroup);
        groupDao.save(thirdGroup);
        studentDao.save(firstStudent);
        studentDao.save(secondStudent);
        studentDao.save(thirdStudent);
        studentDao.save(fourthStudent);
        
        List<Group> expectedGroups = new ArrayList<>();
        expectedGroups.add(secondGroup);
        expectedGroups.add(thirdGroup);
        
        List<Group> actualGroups = groupDao.findAllWithLessOrEqualsStudentCount(1);
        assertEquals(expectedGroups, actualGroups);
    }
    
    @Test
    void updateShouldChangeGroupFields() {
        Group group = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("AA-11")
                .build();
        groupDao.save(group);
        Group expectedGroup = Group.builder()
                .withId(group.getId())
                .withName("BB-22")
                .build();
        groupDao.update(expectedGroup);
        Group actualGroup = groupDao.findById(group.getId()).get();
        assertEquals(expectedGroup, actualGroup);
    }
    
    @Test
    void findAllShouldReturnFirstPageWithThreeEntries() {
        Page page = new Page(1, 3);
        Group firstGroup = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("AA-11")
                .build();
        Group secondGroup = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("BB-22")
                .build();
        Group thirdGroup = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("CC-33")
                .build();
        Group fourthGroup = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("DD-44")
                .build();
        Group fivthGroup = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("EE-55")
                .build();
        groupDao.save(firstGroup);
        groupDao.save(secondGroup);
        groupDao.save(thirdGroup);
        groupDao.save(fourthGroup);
        groupDao.save(fivthGroup);
        
        List<Group> expectedGroups = new ArrayList<>();
        expectedGroups.add(firstGroup);
        expectedGroups.add(secondGroup);
        expectedGroups.add(thirdGroup);
        
        List<Group> actualGroups = groupDao.findAll(page);
        
        assertEquals(expectedGroups, actualGroups);
    }
    
    @Test
    void findAllShouldReturnSecondPageWithTwoEntries() {
        Page page = new Page(2, 3);
        Group firstGroup = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("AA-11")
                .build();
        Group secondGroup = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("BB-22")
                .build();
        Group thirdGroup = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("CC-33")
                .build();
        Group fourthGroup = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("DD-44")
                .build();
        Group fivthGroup = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("EE-55")
                .build();
        groupDao.save(firstGroup);
        groupDao.save(secondGroup);
        groupDao.save(thirdGroup);
        groupDao.save(fourthGroup);
        groupDao.save(fivthGroup);
        
        List<Group> expectedGroups = new ArrayList<>();
        expectedGroups.add(fourthGroup);
        expectedGroups.add(fivthGroup);
        
        List<Group> actualGroups = groupDao.findAll(page);
        
        assertEquals(expectedGroups, actualGroups);
    }
    
    @Test
    void deleteByIdShouldRemoveGroupWithSpecifiedId() {
        Group firstGroup = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("AA-11")
                .build();
        Group secondGroup = Group.builder()
                .withId(UUID.randomUUID().toString())
                .withName("AA-11")
                .build();
        groupDao.save(firstGroup);
        groupDao.save(secondGroup);
        
        groupDao.deleteById(firstGroup.getId());
        assertFalse(groupDao.findById(firstGroup.getId()).isPresent());
    }
}
