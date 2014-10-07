package net.github.rtc.quiz.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Answer {

    private String text;

    private boolean right;

    public Answer() {
    }

    public Answer(final String text, final boolean right) {
        this.text = text;
        this.right = right;
    }


    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(final boolean isRight) {
        this.right = isRight;
    }
}
