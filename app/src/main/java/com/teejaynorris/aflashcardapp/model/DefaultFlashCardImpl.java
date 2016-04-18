package com.teejaynorris.aflashcardapp.model;

/**
 * Created by tjnorris on 6/19/15.
 */
public class DefaultFlashCardImpl extends FlashCard {

    private static final long serialVersionUID = 0L;

    @Override
    public boolean validateAnswer(String userAnswer) {
        return _answer.equalsIgnoreCase(userAnswer);
    }
}
