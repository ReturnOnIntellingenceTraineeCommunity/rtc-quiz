package net.github.rtc.quiz.dao.impl;

import net.github.rtc.quiz.dao.QuestionDAOCustom;
import net.github.rtc.quiz.model.Answer;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class QuestionDAOImpl implements QuestionDAOCustom {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Answer> getRightAnswers(final String id) {
        Aggregation aggregation = newAggregation(
          match(Criteria.where("_id").is(new ObjectId(id)).andOperator(Criteria.where("answers.right").is(true))),
          unwind("answers"),
          match(Criteria.where("answers.right").is(true)),
          project("answers.right","answers.text")
        );

        AggregationResults<Answer> results = mongoTemplate.aggregate(aggregation, "question", Answer.class);
        return results.getMappedResults();

    }
}
