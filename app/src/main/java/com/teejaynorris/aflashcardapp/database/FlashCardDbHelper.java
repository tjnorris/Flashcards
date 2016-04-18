package com.teejaynorris.aflashcardapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.teejaynorris.aflashcardapp.R;
import com.teejaynorris.aflashcardapp.database.contract.FlashCardContract.FlashCardEntry;
import com.teejaynorris.aflashcardapp.database.contract.FlashCardContract.FlashCardPackEntry;
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
 * Created by tjnorris on 6/30/15.
 */
public class FlashCardDbHelper extends SQLiteOpenHelper {

    private final String _TAG = getClass().getSimpleName();

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "databases/flashcards.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String BLOB_TYPE = " BLOB";
    private static final String REFERENCES_CONSTRAINT = " REFERENCES ";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_PACKS =
            "CREATE TABLE " + FlashCardPackEntry.TABLE_NAME + " (" +
                    FlashCardPackEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
//                    FlashCardPackEntry.COLUMN_NAME_FLASH_CARD_API_ID + " INTEGER NOT NULL UNIQUE" + COMMA_SEP +
                    FlashCardPackEntry.COLUMN_NAME_FLASH_CARD_PACK_NAME + TEXT_TYPE + COMMA_SEP +
                    FlashCardPackEntry.COLUMN_NAME_LAST_MODIFIED_DATE + INT_TYPE +

            " )";

    private static final String SQL_CREATE_FLASH_CARDS =
            "CREATE TABLE " + FlashCardEntry.TABLE_NAME + " (" +
                    FlashCardEntry._ID + " INTEGER PRIMARY KEY," +
                    FlashCardEntry.COLUMN_NAME_FLASH_CARD_PACK_ID + REFERENCES_CONSTRAINT + FlashCardPackEntry.TABLE_NAME +
                        "("+ FlashCardPackEntry._ID+")" + COMMA_SEP +
                    FlashCardEntry.COLUMN_NAME_FLASH_CARD_QUESTION + TEXT_TYPE + COMMA_SEP +
                    FlashCardEntry.COLUMN_NAME_FLASH_CARD_ANSWER_TEXT + TEXT_TYPE + COMMA_SEP +
                    FlashCardEntry.COLUMN_NAME_FLASH_CARD_ANSWER_DATA + BLOB_TYPE +
                    " )";


    public FlashCardDbHelper(Context context) {
        super(context, DATABASE_NAME, null, context.getResources().getInteger(R.integer.database_version));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(_TAG, "I'm in onCreate!!!!");

        String path = db.getPath();

        // Setup tables
        db.execSQL(SQL_CREATE_PACKS);
        db.execSQL(SQL_CREATE_FLASH_CARDS);

        ContentValues packValues = new ContentValues();
        packValues.put(FlashCardPackEntry.COLUMN_NAME_FLASH_CARD_PACK_NAME, "Spanish -- Top 50 Nouns");
        packValues.put(FlashCardPackEntry.COLUMN_NAME_FLASH_CARD_API_ID, "-1");

        final long newPackId = db.insert(FlashCardPackEntry.TABLE_NAME, null, packValues);

        List<ContentValues> cardsToBeAdded = _getContentValuesForCards(newPackId);

        for(ContentValues cValue : cardsToBeAdded) {
            db.insert(FlashCardEntry.TABLE_NAME, null, cValue);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<FlashCardDeck> getPacks() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(FlashCardPackEntry.TABLE_NAME, new String[]{FlashCardPackEntry._ID, FlashCardPackEntry.COLUMN_NAME_FLASH_CARD_PACK_NAME},
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
        Cursor cursor = db.query(FlashCardPackEntry.TABLE_NAME, new String[]{FlashCardPackEntry.COLUMN_NAME_FLASH_CARD_API_ID},
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
        String[] cols = new String[]{FlashCardEntry.COLUMN_NAME_FLASH_CARD_QUESTION, FlashCardEntry.COLUMN_NAME_FLASH_CARD_ANSWER_TEXT};
        Cursor cursor = db.query(FlashCardEntry.TABLE_NAME, cols, null, null, null, null, null, null);

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
            db.insert(FlashCardEntry.TABLE_NAME, null, cValue);
        }

    }

    public void storePack(FlashCardDeck pack, Set<FlashCard> cards) {
        SQLiteDatabase db = getReadableDatabase();

        ContentValues packContentValues = new ContentValues();
        packContentValues.put(FlashCardPackEntry.COLUMN_NAME_FLASH_CARD_API_ID, pack.getApiId());
        packContentValues.put(FlashCardPackEntry.COLUMN_NAME_FLASH_CARD_PACK_NAME, pack.getName());
        packContentValues.put(FlashCardPackEntry.COLUMN_NAME_LAST_MODIFIED_DATE, (new Date().getTime()));


        final long newPackId = db.insert(FlashCardPackEntry.TABLE_NAME, null, packContentValues);

        storeFlashCards(newPackId, cards);


    }

    public FlashCardDeck getPackByApiId(Long apiID) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(FlashCardPackEntry.TABLE_NAME, null, FlashCardPackEntry.COLUMN_NAME_FLASH_CARD_API_ID + " = ?", new String[]{apiID.toString()}, null, null, null);

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
            card.put(FlashCardEntry.COLUMN_NAME_FLASH_CARD_PACK_ID, packId);
            card.put(FlashCardEntry.COLUMN_NAME_FLASH_CARD_QUESTION, questions[i]);
            card.put(FlashCardEntry.COLUMN_NAME_FLASH_CARD_ANSWER_TEXT, answers[i]);

            cards.add(card);
        }

        return cards;
    }

    private List<ContentValues> _getContentValuesForCards(final long packId, final Set<FlashCard> cards) {

        List<ContentValues> newContentValues = new ArrayList<>();

        for (FlashCard card : cards) {
            ContentValues cv = new ContentValues();
            cv.put(FlashCardEntry.COLUMN_NAME_FLASH_CARD_PACK_ID, packId);
            cv.put(FlashCardEntry.COLUMN_NAME_FLASH_CARD_QUESTION, card.getQuestion().getValue().toString());
            cv.put(FlashCardEntry.COLUMN_NAME_FLASH_CARD_ANSWER_TEXT, card.getAnswer());

            newContentValues.add(cv);
        }

        return newContentValues;
    }
}
