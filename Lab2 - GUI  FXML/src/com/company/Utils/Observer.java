package com.company.Utils;

public interface Observer<E> {
    void notifyEvent(ListEvent<E> e);
}
