package com.teejaynorris.aflashcardapp.model;

/**
 * Created by tjnorris on 10/12/15.
 */
public final class FlashCardConstants {

    // Ensure NO ONE can instantiate this
    private FlashCardConstants() {
        throw new AssertionError("Constructor should never be called for this class");
    }

    // Keys used to store or access objects stored in key-value data structures.

    public final static String KEY_TEST_STATS = "TEST_STATS";
    public final static String KEY_PACK_ID = "PACK_ID";


    public final static String KEY_CARD_VALUE = "CARD_VALUE";
}
