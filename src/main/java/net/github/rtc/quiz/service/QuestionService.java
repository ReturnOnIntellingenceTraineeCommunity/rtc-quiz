package net.github.rtc.quiz.service;


import net.github.rtc.quiz.model.Question;

import java.util.List;

public interface QuestionService {
    List<Question> findAll();
    void insert(Question question);
    void update(Question question);
    void delete(String id);
    String getRightAnswerText(String id);
    long getCount();
    Question getById(String id);
}
