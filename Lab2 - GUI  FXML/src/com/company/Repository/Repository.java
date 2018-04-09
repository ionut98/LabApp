package com.company.Repository;
import com.company.Errors.RepositoryError;

import java.util.List;

public interface Repository<E>{

    int size();
    void add(E entity) throws RepositoryError;
    void update(E entity1, E entity2) throws RepositoryError;
    void delete(E entity) throws RepositoryError;
    boolean exists(E entity);
    List<E> getAll();

}
