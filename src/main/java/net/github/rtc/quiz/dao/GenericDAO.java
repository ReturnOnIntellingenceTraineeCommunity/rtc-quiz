package net.github.rtc.quiz.dao;

import java.util.List;

public interface GenericDAO<T> {
    List<T> findAll();
    void save(T item);
    void update(String id, T item);
    void delete(String id);
    T getById(String id);
    long getCount();
}
