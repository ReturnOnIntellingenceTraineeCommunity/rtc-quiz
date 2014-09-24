package net.github.rtc.quiz.dao;

import java.util.List;

public interface GenericDAO<T> {
    List<T> findAll();
    void insert(T item);
    void update(T item);
    void delete(T item);
}
