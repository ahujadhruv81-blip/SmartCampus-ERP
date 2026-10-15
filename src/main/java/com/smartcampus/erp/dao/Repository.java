package com.smartcampus.erp.dao;

import java.util.List;
import java.util.Optional;

// Rubric: Collections & Generics (Generic Interface), Database operation classes
public interface Repository<T, ID> {
    T insert(T entity);
    boolean update(T entity);
    boolean delete(ID id);
    Optional<T> findById(ID id);
    List<T> findAll();
}
