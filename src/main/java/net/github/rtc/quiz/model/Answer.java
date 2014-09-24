package net.github.rtc.quiz.model;

import org.jongo.marshall.jackson.oid.ObjectId;

public class Answer {

    @ObjectId
    private String _id;

    private String text;

    private boolean isRight;

    public Answer() {
    }

    public Answer(final String text, final boolean isRight) {
        this.text = text;
        this.isRight = isRight;
    }

    public String get_id() {
        return _id;
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

    public boolean isRight() {
        return isRight;
    }

    public void setRight(final boolean isRight) {
        this.isRight = isRight;
    }
}
