package ua.com.foxminded.jdbcschool.domain;

import java.util.Objects;

public class Course {
    private final String id;
    private final String name;
    private final String description;

    private Course(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
    }
       
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static Builder builder() {
        return new Builder();
    }
    
    public static Builder builder (Course course) {
        return new Builder();
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Course other = (Course) obj;
        return Objects.equals(description, other.description)
                && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return "Course [id=" + id + ", name=" + name + ", description=" + description + "]";
    }

    public static class Builder {
        private String id;
        private String name;
        private String description;
        
        private Builder() {
            
        }
        
        private Builder (Course course) {
            this.id = course.id;
            this.name = course.name;
            this.description = course.description;
        }
        
        public Builder withId(String id) {
            this.id = id;
            return this;
        }
        
        public Builder withName(String name) {
            this.name = name;
            return this;
        }
        
        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }
        
        public Course build() {
            return new Course(this);
        }
    }
}
