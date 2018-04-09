package com.company.Repository;

import com.company.Errors.RepositoryError;

public class RepositoryInMemory<E> extends AbstractRepository<E> {

    @Override
    public int size() {
        return entities.size();
    }

    @Override
    public void add(E entity) throws RepositoryError {
        if(exists(entity))
            throw new RepositoryError("Element existent!\n");

        entities.add(entity);
    }

    @Override
    public void update(E entity1, E entity2) throws RepositoryError {
        if(!exists(entity1))
            throw new RepositoryError("Nu exista elementul de actualizat!\n");

        entities.remove(entity1);
        entities.add(entity2);

    }

    @Override
    public void delete(E entity) throws RepositoryError {
        if(!exists(entity))
            throw new RepositoryError("Nu exista elementul de sters!\n");

        entities.remove(entity);

    }

    @Override
    public boolean exists(E entity) {
        for(E ent : entities)
            if(ent.equals(entity))
                return true;
        return false;
    }

    public void clearAll(){
        entities.clear();
    }

}
