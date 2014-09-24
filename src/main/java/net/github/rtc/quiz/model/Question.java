package net.github.rtc.quiz.model;

import org.jongo.marshall.jackson.oid.ObjectId;

import java.util.List;

public class Question {

    @ObjectId
    private String _id;

    private String text;

    private List<Answer> answers;


    public String get_id() {
        return _id;
    }

    public Question(String text){
        this.text = text;
    }

    public Question(){
    }

    public void set_id(final String _id) {
        this._id = _id;
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
}
