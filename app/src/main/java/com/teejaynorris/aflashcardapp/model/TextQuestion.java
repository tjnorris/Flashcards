package com.teejaynorris.aflashcardapp.model;

import java.io.Serializable;

/**
 * Created by tjnorris on 6/24/15.
 */
public class TextQuestion extends Question implements Serializable {

    private String _value;
    private static final long serialVersionUID = 0L;

    public TextQuestion(String value) {
        _questionType = QuestionType.TEXT;
        _value = value;
    }

    public String getValue() {
        return _value;
    }
}
