package com.teejaynorris.aflashcardapp.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by tjnorris on 6/19/15.
 */
public class FlashCardDeck {
    public static final FlashCardDeck DOES_NOT_EXIST = new FlashCardDeck();

    private Long _id;
    private Long _apiId;
    private String _name;
    private Collection<FlashCard> _flashCards = new ArrayList<FlashCard>();

    public FlashCardDeck() {

    }

    public FlashCardDeck(Long apiId, String name) {
        _apiId = apiId;
        _name = name;
    }

    public Long getId() {
        return _id;
    }

    public void setId(Long id) {
        _id = id;
    }

    public Long getApiId() {
        return _apiId;
    }

    public void setApiId(Long apiId) {
        _apiId = apiId;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public Collection<FlashCard> getFlashCards() {
        return _flashCards;
    }

    public void setFlashCards(Collection<FlashCard> flashCards) {
        _flashCards = flashCards;
    }


    @Override
    public String toString() {
        return getName();
    }
}

