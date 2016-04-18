package com.teejaynorris.aflashcardapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.teejaynorris.aflashcardapp.model.FlashCardConstants;
import com.teejaynorris.aflashcardapp.model.TestStats;

public class TestResultsActivity extends ToolbarAppCompatActivity {

    private final String _TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_results);

        Log.d(_TAG, "In on create, about to add toolbar");

        Toolbar toolbar = (Toolbar)findViewById(R.id.standardToolbar);
        toolbar.setSubtitle("Remote FlashCard Packs");

        setToolbar(toolbar, true);

        Log.d(_TAG, "Getting stats and adding them to the text views");

        TestStats stats = (TestStats)getIntent().getExtras().get(FlashCardConstants.KEY_TEST_STATS);

        String correctStr = String.format("%s (%d of %d)", stats.getPercentageCorrect(), stats.getNumCorrect(), stats.getTotalQuestions());
        String incorrectStr = String.format("%s (%d of %d)", stats.getPercentageIncorrect(), stats.getNumIncorrect(), stats.getTotalQuestions());
        String skippedStr = String.valueOf(stats.getNumSkipped());

        ((TextView)findViewById(R.id.correct_answer_stats)).setText(correctStr);
        ((TextView)findViewById(R.id.incorrect_answer_stats)).setText(incorrectStr);
        ((TextView)findViewById(R.id.skipped_answers_stats)).setText(skippedStr);

    }

    public void goHome(View v) {
        Intent i = new Intent(TestResultsActivity.this, FlashcardPackActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
    }
}
