package net.github.rtc.quiz.dao.impl;

import net.github.rtc.quiz.dao.AbstractMongoDAO;
import net.github.rtc.quiz.model.Question;
import org.springframework.stereotype.Repository;


@Repository
public class QuestionDAO extends AbstractMongoDAO<Question> {
    @Override
    protected Class<Question> getEntityClass() {
        return Question.class;
    }
}
