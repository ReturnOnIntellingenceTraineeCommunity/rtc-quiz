package net.github.rtc.quiz.dao;

import net.github.rtc.quiz.model.Question;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface QuestionDAO extends MongoRepository<Question, String >, QuestionDAOCustom {

}
