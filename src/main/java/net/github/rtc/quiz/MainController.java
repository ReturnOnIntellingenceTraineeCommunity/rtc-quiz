package net.github.rtc.quiz;

import net.github.rtc.quiz.model.Answer;
import net.github.rtc.quiz.model.Difficulty;
import net.github.rtc.quiz.model.Question;
import net.github.rtc.quiz.model.QuestionType;
import net.github.rtc.quiz.service.QuestionService;
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
            questionService.insert(question);
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
        questionService.insert(question);
        return "redirect: //";
    }

    @RequestMapping(value = "/question/edit/{id}", method = RequestMethod.GET)
    public ModelAndView editQuestion(@PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView("updateQuestion");
        modelAndView.addObject("question", questionService.getById(id));
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

            int rightQuestionCount = 0;
            Map<String, List<String>> questionsAndAnswers = new HashMap<>();
            for(String answer: answers){
                String[] idAndAnswer = answer.split("#");
                if(!questionsAndAnswers.containsKey(idAndAnswer[0])){
                    questionsAndAnswers.put(idAndAnswer[0], new ArrayList<String>());
                }
                questionsAndAnswers.get(idAndAnswer[0]).add(idAndAnswer[1]);
            }

            for(String questionId: questionsAndAnswers.keySet()){
                List<Answer> rightAnswers =   questionService.getRightAnswers(questionId);
                if(rightAnswers.size() == questionsAndAnswers.get(questionId).size()){
                    boolean passed = true;
                    for(Answer answer: rightAnswers){
                        if(!questionsAndAnswers.get(questionId).contains(answer.getText())){
                            passed = false;break;
                        }
                    }
                    if(passed){
                        rightQuestionCount++;
                    }
                }
            }

            ModelAndView modelAndView = new ModelAndView("quizResults");
            modelAndView.addObject("right",rightQuestionCount);
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
    public List<Difficulty> getQuestionDifficulty(){
        return Arrays.asList(Difficulty.values());
    }
}
