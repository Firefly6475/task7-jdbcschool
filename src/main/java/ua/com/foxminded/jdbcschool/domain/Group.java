package ua.com.foxminded.jdbcschool.domain;

import java.util.Objects;

public class Group {
    private final String id;
    private final String name;

    private Group(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
    }
       
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static Builder builder (Group group) {
        return new Builder();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Group other = (Group) obj;
        return id == other.id 
                && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return "Group [id=" + id + ", name=" + name + "]";
    }

    public static class Builder {
        private String id;
        private String name;
        
        private Builder() {
            
        }
        
        private Builder (Group group) {
            this.id = group.id;
            this.name = group.name;
        }
        
        public Builder withId(String id) {
            this.id = id;
            return this;
        }
        
        public Builder withName(String name) {
            this.name = name;
            return this;
        }
        
        public Group build() {
            return new Group(this);
        }
    }
}
