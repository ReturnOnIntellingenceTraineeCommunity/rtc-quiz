package net.github.rtc.quiz.service.impl;

import net.github.rtc.quiz.dao.QuestionDAO;
import net.github.rtc.quiz.model.Answer;
import net.github.rtc.quiz.model.Question;
import net.github.rtc.quiz.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionDAO questionDAO;

    @Override
    @Transactional(readOnly = true)
    public List<Question> findAll() {
        return questionDAO.findAll();
    }

    @Override
    @Transactional
    public Question save(final Question question) {
        return questionDAO.save(question);
    }

    @Override
    @Transactional
    public void delete(final String id) {
        questionDAO.delete(id);
    }

    @Override
    @Transactional
    public List<Answer> findRightAnswers(final String id) {
        return questionDAO.getRightAnswers(id);
    }

    @Override
    @Transactional
    public long getCount() {
        return questionDAO.count();
    }

    @Override
    @Transactional
    public Question findById(final String id) {
        Question question = questionDAO.findOne(id);
        if(question == null){
            throw new IllegalArgumentException("There is no question with id:"+id);
        }
        return questionDAO.findOne(id);
    }

    @Override
    @Transactional
    public Page<Question> findPagenateQuestion(int offset, int limit) {
        if(offset > questionDAO.count() || limit <= 0){
            throw new IllegalArgumentException("Wrong limit or offset param");
        }
        PageRequest request = new PageRequest(offset/limit, limit);
        return questionDAO.findAll(request);
    }

    @Override
    public void deleteAll() {
        questionDAO.deleteAll();
    }
}
