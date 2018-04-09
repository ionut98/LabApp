package com.company.Services;

import com.company.Errors.RepositoryError;
import com.company.Validation.ValidationException;

public interface Service <E> {

    int size();
    void add (E entity) throws RepositoryError, ValidationException;
    void update(E entity1, E entity2) throws RepositoryError, ValidationException;
    void delete (E entity) throws RepositoryError;
    boolean exists (E entity);
    Iterable<E> getAll();
}
