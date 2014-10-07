package net.github.rtc.quiz.dao;

import net.github.rtc.quiz.model.Answer;

import java.util.List;

public interface QuestionDAOCustom {
    List<Answer> getRightAnswers(String id);
}
