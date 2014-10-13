package net.github.rtc.quiz.service;


import net.github.rtc.quiz.model.Answer;
import net.github.rtc.quiz.model.Question;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QuestionService {
    List<Question> findAll();
    Question save(Question question);
    void delete(String id);
    List<Answer> findRightAnswers(String id);
    long getCount();
    Question findById(String id);
    Page<Question> findQuestionPage(int offset, int limit);
    void deleteAll();
}
