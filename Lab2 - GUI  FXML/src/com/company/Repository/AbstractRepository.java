package com.company.Repository;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractRepository <E> implements Repository<E>{
    protected List<E> entities = new LinkedList<>();


    @Override
    public List<E> getAll() {
        return entities;
    }
}
