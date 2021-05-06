package ua.com.foxminded.jdbcschool.dao;

import java.util.List;
import ua.com.foxminded.jdbcschool.domain.Student;

public interface StudentDao extends CrudDao<Student> {
    List<Student> findAllByCourseName(String courseName);
    
    void insertStudentCourses(List<Student> students);
    
    void saveRelation(String studentId, String courseId);
    
    List<String> findRelation(String studentId, String courseId);
    
    void deleteRelation(String studentId, String courseId);
}
