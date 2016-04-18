package com.teejaynorris.aflashcardapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.teejaynorris.aflashcardapp.R;
import com.teejaynorris.aflashcardapp.database.contract.FlashCardContract;
import com.teejaynorris.aflashcardapp.model.DefaultFlashCardImpl;
import com.teejaynorris.aflashcardapp.model.FlashCard;
import com.teejaynorris.aflashcardapp.model.FlashCardDeck;
import com.teejaynorris.aflashcardapp.model.TextQuestion;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tjnorris on 4/18/16.
 */
public class FlashcardDatabase extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "flashcards.db";
    private static final int DATABASE_VERSION = 1;

    public FlashcardDatabase(Context context) {
        super(context, DATABASE_NAME, null, context.getResources().getInteger(R.integer.database_version));
    }

    public List<FlashCardDeck> getPacks() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(FlashCardContract.FlashCardPackEntry.TABLE_NAME, new String[]{FlashCardContract.FlashCardPackEntry._ID, FlashCardContract.FlashCardPackEntry.COLUMN_NAME_FLASH_CARD_PACK_NAME},
                null, null, null, null, null, null);

        cursor.moveToNext();

        List<FlashCardDeck> packs = new ArrayList<>();

        System.err.println("About to create all the existing packs" + cursor.getCount());

        while(!cursor.isAfterLast()) {
            System.out.println("Hello World");
            FlashCardDeck deck = new FlashCardDeck();
            deck.setId(cursor.getLong(0));
            deck.setName(cursor.getString(1));

            packs.add(deck);
            cursor.moveToNext();
        }

        return packs;
    }

    public Set<Long> getAllPackApiIds() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(FlashCardContract.FlashCardPackEntry.TABLE_NAME, new String[]{FlashCardContract.FlashCardPackEntry.COLUMN_NAME_FLASH_CARD_API_ID},
                null, null, null, null, null, null);

        cursor.moveToNext();

        Set<Long> apiIdSet = new HashSet<>();

        while(!cursor.isAfterLast()) {

            apiIdSet.add(cursor.getLong(0));

            cursor.moveToNext();
        }

        return apiIdSet;
    }

    public Set<FlashCard> getAllCardsForDeckById(long packId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] cols = new String[]{FlashCardContract.FlashCardEntry.COLUMN_NAME_FLASH_CARD_QUESTION, FlashCardContract.FlashCardEntry.COLUMN_NAME_FLASH_CARD_ANSWER_TEXT};
        Cursor cursor = db.query(FlashCardContract.FlashCardEntry.TABLE_NAME, cols, FlashCardContract.FlashCardEntry.COLUMN_NAME_FLASH_CARD_PACK_ID + " = ?", new String[]{String.valueOf(packId)}, null, null, null, null);

        Set<FlashCard> cards = new HashSet<>();

        cursor.moveToNext();
        while(!cursor.isAfterLast()) {
            FlashCard flashCard = new DefaultFlashCardImpl();
            flashCard.setQuestion(new TextQuestion(cursor.getString(0)));
            flashCard.setAnswer(cursor.getString(1));

            cards.add(flashCard);
            cursor.moveToNext();
        }

        return cards;
    }

    public void storeFlashCards (Long packId, Set<FlashCard> cards) {
        List<ContentValues> cardsToBeAdded = _getContentValuesForCards(packId, cards);

        SQLiteDatabase db = getReadableDatabase();

        for(ContentValues cValue : cardsToBeAdded) {
            db.insert(FlashCardContract.FlashCardEntry.TABLE_NAME, null, cValue);
        }

    }

    public void storePack(FlashCardDeck pack, Set<FlashCard> cards) {
        SQLiteDatabase db = getReadableDatabase();

        ContentValues packContentValues = new ContentValues();
        packContentValues.put(FlashCardContract.FlashCardPackEntry.COLUMN_NAME_FLASH_CARD_API_ID, pack.getApiId());
        packContentValues.put(FlashCardContract.FlashCardPackEntry.COLUMN_NAME_FLASH_CARD_PACK_NAME, pack.getName());
        packContentValues.put(FlashCardContract.FlashCardPackEntry.COLUMN_NAME_LAST_MODIFIED_DATE, (new Date().getTime()));


        final long newPackId = db.insert(FlashCardContract.FlashCardPackEntry.TABLE_NAME, null, packContentValues);

        storeFlashCards(newPackId, cards);


    }

    public FlashCardDeck getPackByApiId(Long apiID) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(FlashCardContract.FlashCardPackEntry.TABLE_NAME, null, FlashCardContract.FlashCardPackEntry.COLUMN_NAME_FLASH_CARD_API_ID + " = ?", new String[]{apiID.toString()}, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        cursor.moveToNext();

        FlashCardDeck pack = new FlashCardDeck();
        pack.setId(cursor.getLong(0));
        pack.setApiId(cursor.getLong(1));
        pack.setName(cursor.getString(2));

        return pack;
    }

    // PRIVATE METHODS!!!

    private List<ContentValues> _getContentValuesForCards(final long packId) {
        String[] questions = new String[] {"time (specific occurrence)", "year", "time (general)", "day", "thing", "man", "part", "life", "moment", "form", "house", "world", "woman", "case", "country", "place", "person", "hour", "work", "point", "hand", "way", "end", "type", "people", "example", "side", "son", "problem", "bill", "means", "word", "child", "embargo", "father", "change", "history", "idea", "water", "night", "city", "way", "name", "family", "reality", "work", "truth", "month", "reason", "group"};
        String[] answers = new String[] {"vez", "año", "tiempo", "día", "cosa", "hombre", "parte", "vida", "momento", "forma", "casa", "mundo", "mujer", "caso", "país", "lugar", "persona", "hora", "trabajo", "punto", "mano", "manera", "fin", "tipo", "gente", "ejemplo", "lado", "hijo", "problema", "cuenta", "medio", "palabra", "niño", "sin", "padre", "cambio", "historia", "idea", "agua", "noche", "ciudad", "modo", "nombre", "familia", "realidad", "obra", "verdad", "mes", "razón", "grupo"};

        List<ContentValues> cards = new ArrayList<>();

        for (int i=0; i<questions.length; i++) {
            ContentValues card = new ContentValues();
            card.put(FlashCardContract.FlashCardEntry.COLUMN_NAME_FLASH_CARD_PACK_ID, packId);
            card.put(FlashCardContract.FlashCardEntry.COLUMN_NAME_FLASH_CARD_QUESTION, questions[i]);
            card.put(FlashCardContract.FlashCardEntry.COLUMN_NAME_FLASH_CARD_ANSWER_TEXT, answers[i]);

            cards.add(card);
        }

        return cards;
    }

    private List<ContentValues> _getContentValuesForCards(final long packId, final Set<FlashCard> cards) {

        List<ContentValues> newContentValues = new ArrayList<>();

        for (FlashCard card : cards) {
            ContentValues cv = new ContentValues();
            cv.put(FlashCardContract.FlashCardEntry.COLUMN_NAME_FLASH_CARD_PACK_ID, packId);
            cv.put(FlashCardContract.FlashCardEntry.COLUMN_NAME_FLASH_CARD_QUESTION, card.getQuestion().getValue().toString());
            cv.put(FlashCardContract.FlashCardEntry.COLUMN_NAME_FLASH_CARD_ANSWER_TEXT, card.getAnswer());

            newContentValues.add(cv);
        }

        return newContentValues;
    }
}
