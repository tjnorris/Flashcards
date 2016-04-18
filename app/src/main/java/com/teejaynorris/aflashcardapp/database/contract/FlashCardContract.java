package com.teejaynorris.aflashcardapp.database.contract;

import android.provider.BaseColumns;

/**
 * Created by tjnorris on 6/30/15.
 */
public class FlashCardContract {

    /**
     * This does nothing...but whatevs
     */
    public FlashCardContract() {}

    public static abstract class FlashCardEntry implements BaseColumns {
        // Bunch of constants associated with table.

        public static final String TABLE_NAME = "flash_cards";
        public static final String COLUMN_NAME_FLASH_CARD_PACK_ID = "pack_id";
        public static final String COLUMN_NAME_FLASH_CARD_QUESTION = "question";
        public static final String COLUMN_NAME_FLASH_CARD_ANSWER_TEXT = "answer_text";
        public static final String COLUMN_NAME_FLASH_CARD_ANSWER_DATA = "answer_data";

    }

    public static abstract class FlashCardPackEntry implements BaseColumns {

        public static final String TABLE_NAME = "flash_card_packs";
        public static final String COLUMN_NAME_FLASH_CARD_PACK_NAME = "pack_name";

        /*
        Am not guaranteeing that the id on the device will be the same as in the api,
          so will need to have a separate column for the api id
         */
        public static final String COLUMN_NAME_FLASH_CARD_API_ID = "api_id";
        public static final String COLUMN_NAME_LAST_MODIFIED_DATE = "last_modified_date";
    }

}
