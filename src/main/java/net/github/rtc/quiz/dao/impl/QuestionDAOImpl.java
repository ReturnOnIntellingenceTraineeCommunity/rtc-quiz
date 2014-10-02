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
    public List<Answer> getRightAnswers(final String id) {
        List<Answer> result = this.collection.aggregate("{$match:{_id : #, \"answers.right\": true}}", new ObjectId(id))
          .and("{$unwind: '$answers'}").and("{$match:{\"answers.right\":true}}")
          .and("{$project:{_id:0, right:\"$answers.right\", text:\"$answers.text\"}}").as(Answer.class);
        return result;
    }
}
