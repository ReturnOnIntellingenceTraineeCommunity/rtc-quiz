package net.github.rtc.quiz;

import net.github.rtc.quiz.dao.impl.QuestionDAO;
import net.github.rtc.quiz.model.Answer;
import net.github.rtc.quiz.model.Difficulty;
import net.github.rtc.quiz.model.Question;
import net.github.rtc.quiz.model.QuestionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class MainController {

    private static final int ANSWERS_COUNT = 4;

    @Autowired
    private QuestionDAO questionDAO;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView showAll() {
        ModelAndView modelAndView = new ModelAndView("list");
        modelAndView.addObject("questions", questionDAO.findAll());
        return modelAndView;
    }

    @RequestMapping(value = "/question/create", method = RequestMethod.GET)
    public ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("formQuestion");
        modelAndView.addObject("question", getQuestion());
        modelAndView.addObject("difficulties", getQuestionDifficulty());
        modelAndView.addObject("types", getQuestionTypes());
        return modelAndView;
    }

    @RequestMapping(value = "/question/save", method = RequestMethod.POST)
    public ModelAndView saveQuestion(@ModelAttribute("question") Question question) {
        questionDAO.insert(question);
        return showAll();
    }

    @RequestMapping(value = "/question/edit/{id}", method = RequestMethod.GET)
    public ModelAndView editQuestion(@PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView("formQuestion");
        // get question by id here
        modelAndView.addObject("question", getQuestion());
        modelAndView.addObject("difficulties", getQuestionDifficulty());
        modelAndView.addObject("types", getQuestionTypes());
        return modelAndView;
    }

    @RequestMapping(value = "/question/delete/{id}", method = RequestMethod.GET)
    public ModelAndView deleteQuestion(@PathVariable String id) {
        // remove question by id here
        return showAll();
    }

    @ModelAttribute("question")
    public Question getQuestion(){
        Question question = new Question();
        List<Answer> answers = new ArrayList<>();
        for(int i=0; i< ANSWERS_COUNT; i++){
            answers.add(new Answer());
        }
        question.setAnswers(answers);
        return question;
    }

    @ModelAttribute("types")
    public List<QuestionType> getQuestionTypes(){
        return Arrays.asList(QuestionType.values());
    }

    @ModelAttribute("difficulties")
    public List<Difficulty> getQuestionDifficulty(){
        return Arrays.asList(Difficulty.values());
    }
}
