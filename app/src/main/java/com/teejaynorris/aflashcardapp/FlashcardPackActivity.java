package com.teejaynorris.aflashcardapp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.teejaynorris.aflashcardapp.database.FlashcardDatabase;
import com.teejaynorris.aflashcardapp.model.FlashCard;
import com.teejaynorris.aflashcardapp.model.FlashCardConstants;
import com.teejaynorris.aflashcardapp.model.TestStats;

import java.util.Iterator;

public class FlashcardPackActivity extends ToolbarAppCompatActivity implements SingleCardFragment.OnFragmentInteractionListener, UserAnswerFragment.OnFragmentInteractionListener {

    private final String _TAG = getClass().getSimpleName();

    private Iterator<FlashCard> _flashCardsIter;
    private FlashCard _currentCard = null;
    private TestStats _testStats = null;

    private int _currentIndex = 0;

    private static Mode _currentMode;

    private SingleCardFragment _questionCardFrag;
    private SingleCardFragment _answerCardFrag;
    private UserAnswerFragment _userAnswerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        start();

        setToolbarWithSubtitle();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        recreate();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void nextCard() {

        // Go to next card unless this is last card, then go to stats page
        if (_flashCardsIter.hasNext()) {
            _currentCard = _flashCardsIter.next();

            // Each time, make sure answer is hidden
            View view = findViewById(R.id.answer_frag_container);
            view.setVisibility(View.INVISIBLE);

            // Uncheck the check box
            View checkBox = findViewById(R.id.checkBox);
            if (checkBox != null) {
                ((CheckBox)checkBox).setChecked(false);
            }

            Bundle questionBundle = new Bundle();
            questionBundle.putString(FlashCardConstants.KEY_CARD_VALUE, _currentCard.getQuestion().getValue().toString());

            Bundle answerBundle = new Bundle();
            answerBundle.putString(FlashCardConstants.KEY_CARD_VALUE, _currentCard.getAnswer());

            _questionCardFrag.updateText(_currentCard.getQuestion().getValue().toString());

            _answerCardFrag.updateText(_currentCard.getAnswer());


        }
        else {
            if (Mode.TEST.equals(_currentMode)) {
                Intent i = new Intent(getApplicationContext(), TestResultsActivity.class);
                i.putExtra(FlashCardConstants.KEY_TEST_STATS, _testStats);
                startActivity(i);
            }
            else {
                recreate();
            }
        }
    }

    public void skipCard(View view) {
        if (Mode.TEST.equals(_currentMode)) {
            _testStats.answerSkipped();
        }

        nextCard();
    }

    public void choosePath(View view) {

        if (R.id.practiceButton == view.getId()) {
            _currentMode = Mode.PRACTICE;
            setContentView(R.layout.flashcard_pack_main);

        } else if (R.id.testButton == view.getId()) {
            _currentMode = Mode.TEST;
            _testStats = new TestStats();
            setContentView(R.layout.flashcard_pack_test);
        } else {
            Log.w(_TAG, "Um should never be 'optionless' in choosePath method, something went wrong");
            setContentView(R.layout.flashcard_pack_main);
        }

        setToolbarWithSubtitle();


        String name = getIntent().getExtras().getString("PACK_NAME");


        if (_flashCardsIter.hasNext()) {
            _currentCard = _flashCardsIter.next();

        }

        // Initial state is to show the front of the card
        if (_questionCardFrag == null) {

            Bundle questionBundle = new Bundle();
            questionBundle.putString(FlashCardConstants.KEY_CARD_VALUE, _currentCard.getQuestion().getValue().toString());

            Bundle answerBundle = new Bundle();
            answerBundle.putString(FlashCardConstants.KEY_CARD_VALUE, _currentCard.getAnswer());


            _questionCardFrag = SingleCardFragment.newInstance();
            _questionCardFrag.setArguments(questionBundle);

            _answerCardFrag = SingleCardFragment.newInstance();
            _answerCardFrag.setArguments(answerBundle);

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.question_frag_container, _questionCardFrag);
            fragmentTransaction.add(R.id.answer_frag_container, _answerCardFrag);
            fragmentTransaction.commit();
        }


        // This better contain the fragment container
        if (Mode.TEST.equals(_currentMode)) {


            if (findViewById(R.id.user_answer_frag_container) != null) {
                _userAnswerFragment = new UserAnswerFragment();

                _userAnswerFragment.setArguments(getIntent().getExtras());

                // Add the fragment to the 'fragment_container' FrameLayout
                getFragmentManager().beginTransaction()
                        .replace(R.id.user_answer_frag_container, _userAnswerFragment).commit();
            }

        }
    }

    public void toggleShowAnswer(View view) {
        CheckBox cBox = (CheckBox) view;

        View answerContainer = findViewById(R.id.answer_frag_container);


        if (cBox.isChecked()) {
            answerContainer.setVisibility(View.VISIBLE);
        }
        else {
            answerContainer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserAnswerInteraction(String answer) {
        Toast result;
        if (_currentCard.getAnswer().equalsIgnoreCase(answer)) {
            result = Toast.makeText(getApplicationContext(), "Correct!!", Toast.LENGTH_SHORT);
            _testStats.answeredCorrectly();

        }
        else {
            result = Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT);
            _testStats.answeredIncorrectly();
        }
        result.show();


        try {
            Thread.sleep(500);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }


        if (Mode.TEST.equals(_currentMode)) {
            _userAnswerFragment.clearText();
        }

        nextCard();
    }

    public void onFragmentInteraction(Uri uri) {

    }

    public enum Mode {
        PRACTICE,
        TEST
    }

    private void start() {
        setContentView(R.layout.flashcard_pack_landing_page);


        Long id = getIntent().getExtras().getLong("PACK_ID");

        if (id == null) {
            throw new RuntimeException("PACK ID SHOULD NEVER BE NULL");
        }

        FlashcardDatabase dbHelper = new FlashcardDatabase(this);

        _flashCardsIter = dbHelper.getAllCardsForDeckById(id).iterator();
    }

    private void setToolbarWithSubtitle() {
        String name = getIntent().getExtras().getString("PACK_NAME");

        Toolbar toolbar = (Toolbar)findViewById(R.id.standardToolbar);
        toolbar.setSubtitle(name);

        setToolbar(toolbar, true);
    }
}
