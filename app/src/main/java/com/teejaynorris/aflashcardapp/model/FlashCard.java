package com.teejaynorris.aflashcardapp.model;

import java.io.Serializable;

/**
 * Created by tjnorris on 6/19/15.
 */
public abstract class FlashCard implements Serializable {

    private static final long serialVersionUID = 0L;

    protected Question _question;
    protected String _answer;

    public Question getQuestion() {
        return _question;
    }

    public void setQuestion(Question question) {
        _question = question;
    }

    public String getAnswer() {
        return _answer;
    }

    public void setAnswer(String answer) {
        _answer = answer;
    }

    public abstract boolean validateAnswer(String userAnswer);
}