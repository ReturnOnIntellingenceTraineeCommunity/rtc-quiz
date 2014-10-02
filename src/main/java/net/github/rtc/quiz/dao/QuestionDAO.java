package net.github.rtc.quiz.dao;

import net.github.rtc.quiz.model.Answer;
import net.github.rtc.quiz.model.Question;

import java.util.List;

public interface QuestionDAO extends GenericDAO<Question> {
    List<Answer> getRightAnswers(String id);
}
