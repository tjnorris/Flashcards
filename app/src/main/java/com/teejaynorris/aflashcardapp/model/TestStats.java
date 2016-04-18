package com.teejaynorris.aflashcardapp.model;

import java.io.Serializable;

/**
 * Created by tjnorris on 10/11/15.
 */
public class TestStats implements Serializable {

    private static final long serialVersionUID = 0L;

    private int _numCorrect = 0;
    private int _numIncorrect = 0;
    private int _numSkipped = 0;
    private int _totalQuestions = 0;

    public void answeredCorrectly() {
        _numCorrect++;
        _totalQuestions++;
    }
    public void answeredIncorrectly() {
        _numIncorrect++;
        _totalQuestions++;
    }

    public void answerSkipped() {
        _numSkipped++;
        _totalQuestions++;
    }


    public void resetStats() {
        _numCorrect=0;
        _numSkipped=0;
        _numIncorrect=0;
        _totalQuestions=0;
    }

    public int getNumCorrect() {
        return _numCorrect;
    }

    public String getPercentageCorrect() {
        if (_totalQuestions == 0) {
            return "0%";
        }

        Double d = 100 * ((1.0 * _numCorrect) / _totalQuestions);

        int percentage = d.intValue();

        return percentage + "%";
    }

    public int getNumIncorrect() {
        return _numIncorrect;
    }

    public String getPercentageIncorrect() {
        if (_totalQuestions == 0) {
            return "0%";
        }

        Double d = 100 * ((1.0 * _numIncorrect) / _totalQuestions);
        int percentage = d.intValue();

        return percentage + "%";
    }

    public int getNumSkipped() {
        return _numSkipped;
    }

    public int getTotalQuestions() {
        return _totalQuestions;
    }
}
