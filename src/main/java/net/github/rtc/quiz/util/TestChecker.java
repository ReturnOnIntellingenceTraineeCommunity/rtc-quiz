package net.github.rtc.quiz.util;

import net.github.rtc.quiz.model.Answer;
import net.github.rtc.quiz.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TestChecker {

    @Autowired
    private QuestionService questionService;

    public long checkTest(List<String> answers){

        int rightQuestionCount = 0;
        Map<String, List<String>> questionsAndAnswers = new HashMap<>();
        for(String answer: answers){
            String[] idAndAnswer = answer.split("#");
            if(!questionsAndAnswers.containsKey(idAndAnswer[0])){
                questionsAndAnswers.put(idAndAnswer[0], new ArrayList<String>());
            }
            questionsAndAnswers.get(idAndAnswer[0]).add(idAndAnswer[1]);
        }

        for(String questionId: questionsAndAnswers.keySet()) {
            List<Answer> rightAnswers =   questionService.findRightAnswers(questionId);
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
        return rightQuestionCount;
    }
}
