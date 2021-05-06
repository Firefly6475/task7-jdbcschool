package ua.com.foxminded.jdbcschool.domain;

import java.util.List;
import java.util.Objects;

public class Student {
    private final String id;
    private final Group group;
    private final List<Course> courses;
    private final String firstName;
    private final String lastName;

    private Student(Builder builder) {
        this.id = builder.id;
        this.group = builder.group;
        this.courses = builder.courses;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
    }
       
    public String getId() {
        return id;
    }

    public Group getGroup() {
        return group;
    }
    
    public List<Course> getCourses() {
        return courses;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public static Builder builder() {
        return new Builder();
    }
    
    public static Builder builder (Student student) {
        return new Builder();
    }

    @Override
    public int hashCode() {
        return Objects.hash(courses, firstName, group, id, lastName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Student other = (Student) obj;
        return Objects.equals(courses, other.courses)
                && Objects.equals(firstName, other.firstName)
                && Objects.equals(group, other.group) && id == other.id
                && Objects.equals(lastName, other.lastName);
    }

    @Override
    public String toString() {
        return "Student [id=" + id + ", group=" + group + ", firstName=" + firstName 
                + ", lastName=" + lastName + "]";
    }

    public static class Builder {
        private String id;
        private Group group;
        private List<Course> courses;
        private String firstName;
        private String lastName;
        
        private Builder() {
            
        }
        
        private Builder (Student student) {
            this.id = student.id;
            this.group = student.group;
            this.courses = student.courses;
            this.firstName = student.firstName;
            this.lastName = student.lastName;
        }
        
        public Builder withId(String id) {
            this.id = id;
            return this;
        }
        
        public Builder withGroup(Group group) {
            this.group = group;
            return this;
        }
        
        public Builder withCourses(List<Course> courses) {
            this.courses = courses;
            return this;
        }
        
        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        
        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        
        public Student build() {
            return new Student(this);
        }
    }
}
