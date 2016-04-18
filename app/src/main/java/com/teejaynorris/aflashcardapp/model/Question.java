package com.teejaynorris.aflashcardapp.model;

/**
 * Created by tjnorris on 6/22/15.
 */
public abstract class Question {

    QuestionType _questionType;

    public QuestionType getQuestionType() {
        return _questionType;
    }

    public abstract Object getValue();
}