package net.github.rtc.quiz;

import net.github.rtc.quiz.model.Answer;
import net.github.rtc.quiz.model.Level;
import net.github.rtc.quiz.model.Question;
import net.github.rtc.quiz.model.QuestionType;
import net.github.rtc.quiz.service.QuestionService;
import net.github.rtc.quiz.util.TestChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class MainController {


    private static final int ANSWERS_COUNT = 4;

    @Autowired
    private QuestionService questionService;
    @Autowired
    private TestChecker testChecker;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView showAll() {
        ModelAndView modelAndView = new ModelAndView("list");
        modelAndView.addObject("questions", questionService.findAll());
        return modelAndView;
    }

    @RequestMapping(value = "/question/create", method = RequestMethod.GET)
    public ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("createQuestion");
        modelAndView.addObject("question", getQuestion());
        modelAndView.addObject("difficulties", getQuestionDifficulty());
        modelAndView.addObject("types", getQuestionTypes());
        return modelAndView;
    }

    @RequestMapping(value = "/question/save", method = RequestMethod.POST)
    public ModelAndView saveQuestion(@ModelAttribute("question") Question question) {
        boolean rightFound = false;
        for(Answer answer: question.getAnswers()){
            if(answer.isRight()){
                rightFound = true;
            }
        }
        if(rightFound){
            questionService.save(question);
            return new ModelAndView("redirect: //");
        }else {
            ModelAndView modelAndView = new ModelAndView("createQuestion");
            modelAndView.addObject("question", question);
            modelAndView.addObject("difficulties", getQuestionDifficulty());
            modelAndView.addObject("types", getQuestionTypes());
            return modelAndView;
        }

    }

    @RequestMapping(value = "/question/update", method = RequestMethod.POST)
    public String updateQuestion(@ModelAttribute("question") Question question, @RequestParam String _id) {
        question.set_id(_id);
        questionService.save(question);
        return "redirect: //";
    }

    @RequestMapping(value = "/question/edit/{id}", method = RequestMethod.GET)
    public ModelAndView editQuestion(@PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView("updateQuestion");
        modelAndView.addObject("question", questionService.findById(id));
        modelAndView.addObject("difficulties", getQuestionDifficulty());
        modelAndView.addObject("types", getQuestionTypes());
        return modelAndView;
    }

    @RequestMapping(value = "/question/delete/{id}", method = RequestMethod.GET)
    public String deleteQuestion(@PathVariable String id) {
        questionService.delete(id);
        return "redirect: //";
    }

    @RequestMapping(value = "/question/answer", method = RequestMethod.GET)
    public ModelAndView beginQuiz() {
        ModelAndView modelAndView = new ModelAndView("quiz");
        modelAndView.addObject("questions", questionService.findAll());
        return modelAndView;
    }

    @RequestMapping(value = "/question/check", method = RequestMethod.POST)
    public ModelAndView checkQuiz(@RequestParam(required = false) List<String> answers) {
        if(answers != null) {
            ModelAndView modelAndView = new ModelAndView("quizResults");
            modelAndView.addObject("right", testChecker.checkTest(answers));
            modelAndView.addObject("total",questionService.getCount());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("quiz");
            modelAndView.addObject("questions", questionService.findAll());
            return modelAndView;
        }

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
    public List<Level> getQuestionDifficulty(){
        return Arrays.asList(Level.values());
    }
}
