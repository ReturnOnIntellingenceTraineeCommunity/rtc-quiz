package net.github.rtc.quiz.service.impl;

import net.github.rtc.quiz.dao.QuestionDAO;
import net.github.rtc.quiz.model.Answer;
import net.github.rtc.quiz.model.Question;
import net.github.rtc.quiz.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionDAO questionDAO;

    @Override
    public List<Question> findAll() {
        return questionDAO.findAll();
    }

    @Override
    public void insert(final Question question) {
        questionDAO.save(question);
    }

    @Override
    public void update(final Question question) {
        questionDAO.save(question);
    }

    @Override
    public void delete(final String id) {
        questionDAO.delete(id);
    }

    @Override
    public List<Answer> getRightAnswers(final String id) {
        return questionDAO.getRightAnswers(id);
    }

    @Override
    public long getCount() {
        return questionDAO.count();
    }

    @Override
    public Question getById(final String id) {
        return questionDAO.findOne(id);
    }
}
