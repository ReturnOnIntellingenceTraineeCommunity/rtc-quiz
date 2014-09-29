package net.github.rtc.quiz.dao;

import net.github.rtc.quiz.model.Question;

public interface QuestionDAO extends GenericDAO<Question> {
    String getRightAnswerText(String id);
}
