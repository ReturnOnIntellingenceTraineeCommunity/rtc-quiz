package net.github.rtc.quiz.dao.impl;

import net.github.rtc.quiz.dao.AbstractMongoDAO;
import net.github.rtc.quiz.dao.QuestionDAO;
import net.github.rtc.quiz.model.Answer;
import net.github.rtc.quiz.model.Question;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class QuestionDAOImpl extends AbstractMongoDAO<Question> implements QuestionDAO {
    @Override
    protected Class<Question> getEntityClass() {
        return Question.class;
    }

    @Override
    public String getRightAnswerText(final String id) {
        List<Answer> result = this.collection.findOne(new ObjectId(id)).projection("{_id : 0," +
          "answers:{$elemMatch:{right:true}}}").as(Question.class).getAnswers();
        return result.get(0).getText();
    }
}
