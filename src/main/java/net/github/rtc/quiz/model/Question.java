package net.github.rtc.quiz.model;

import org.jongo.marshall.jackson.oid.ObjectId;

/**
 * Created by Ivan Yatcuba on 9/24/14.
 */
public class Question {

    public Question(String text){
        this.text = text;
    }

    @ObjectId
    private String _id;

    private String text;

}
