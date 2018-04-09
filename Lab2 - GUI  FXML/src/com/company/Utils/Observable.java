package com.company.Utils;

public interface Observable<E> {

    void addObserver(Observer<E> o);

    void removeObserver(Observer<E> o);

    void notifyObservers(ListEvent<E> event);
}
