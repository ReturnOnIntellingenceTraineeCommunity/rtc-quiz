package net.github.rtc.quiz.controller;

import net.github.rtc.quiz.model.Question;
import net.github.rtc.quiz.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class QuestionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionService questionService;

    /**
     * Retrieve all question from database
     * @return question collection
     */
    @RequestMapping(value = "/question", method = RequestMethod.GET)
    public @ResponseBody List<Question> findAllQuestion() {
        LOGGER.debug("Received request to find all questions");
        return questionService.findAll();
    }

    /**
     * Retrieve a page of questions
     * @param limit number of questions per page
     * @param offset how many questions to skip
     */
    @RequestMapping(value = "/question", params = { "limit", "offset" }, method = RequestMethod.GET)
    public Page<Question> findPaginateQuestion(@RequestParam("limit") int limit,
      @RequestParam( "offset" ) int offset) {
        LOGGER.debug("Received request to get page of questions from {0} to {1}", offset, offset+limit);
        return questionService.findPagenateQuestion(offset, limit);
    }

    /**
     * Create question
     * @param question a question to create
     * @return created question
     */
    @RequestMapping(value = "/question", method = RequestMethod.POST)
    public Question createQuestion(@RequestBody @Valid final Question question) {
        LOGGER.debug("Received request to create the {}", question);
        return questionService.save(question);
    }

    /**
     * Delete all questions from database
     */
    @RequestMapping(value = "/question", method = RequestMethod.DELETE)
    public void deleteQuestions() {
        LOGGER.debug("Received request to delete all questions");
        questionService.deleteAll();
    }

    /**
     *  Retrieve a question with specified id
     * @param id id of the question to retrieve
     * @return question with a mentioned id
     */
    @RequestMapping(value = "/question/{id}", method = RequestMethod.GET)
    public Question findQuestion(@PathVariable String id) {
        LOGGER.debug("Received request to retrieve question with id {}", id);
        return questionService.findById(id);
    }

    /**
     * Update a question
     * @param question question to update
     */
    @RequestMapping(value = "/question", method = RequestMethod.PUT)
    public void updateQuestion(@RequestBody @Valid final Question question) {
        LOGGER.debug("Received request to update question {}", question);
        questionService.save(question);
    }

    /**
     * Delete a question by id
     * @param id id of the question to delete
     */
    @RequestMapping(value = "/question/{id}", method = RequestMethod.DELETE)
    public void deleteQuestion(@PathVariable String id) {
        LOGGER.debug("Received request to delete question with id", id);
        questionService.delete(id);
    }
}
