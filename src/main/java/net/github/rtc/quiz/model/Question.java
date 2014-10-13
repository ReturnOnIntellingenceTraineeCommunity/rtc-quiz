package net.github.rtc.quiz.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Question {

    @Id
    private String _id;

    private String text;

    private QuestionType type = QuestionType.JAVA;

    private  Difficulty difficulty = Difficulty.EASY;

    private List<Answer> answers;

    public Question(){
    }
    public Question(String text){
        this.text = text;
    }

    public void set_id(final String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }


    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(final List<Answer> answers) {
        this.answers = answers;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(final QuestionType type) {
        this.type = type;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(final Difficulty difficulty) {
        this.difficulty = difficulty;
    }


}
