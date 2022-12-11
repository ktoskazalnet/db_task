package org.ktoskazalnet.repository;

import java.util.List;
import java.util.Optional;


public interface CrudRepository <T, ID> {
    T save(T entity);

    Optional<T> findById(ID id);

    boolean existsById(ID id);

    List<T> findAll();

    void deleteById(ID id);

    void delete(T entity);
}

