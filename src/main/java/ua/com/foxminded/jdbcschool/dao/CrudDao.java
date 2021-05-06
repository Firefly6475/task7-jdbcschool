package ua.com.foxminded.jdbcschool.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<E> {
    void save(E entity);
    
    void saveAll(List<E> entities);
    
    Optional<E> findById(String id);
    
    List<E> findAll();
    
    List<E> findAll(Page page);
    
    void update(E entity);
    
    void deleteById(String id);
}
