package net.github.rtc.quiz.dao.impl;

import net.github.rtc.quiz.dao.AbstractMongoDAO;
import net.github.rtc.quiz.model.Question;
import org.springframework.stereotype.Repository;

/**
 * Created by Ivan Yatcuba on 9/24/14.
 */
@Repository
public class QuestionDAO extends AbstractMongoDAO<Question> {
    @Override
    protected Class<Question> getEntityClass() {
        return Question.class;
    }

    public void insert(Question question) {
        this.collection.save(question);
    }
}
