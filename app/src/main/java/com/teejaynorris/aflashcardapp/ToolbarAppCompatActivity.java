package com.teejaynorris.aflashcardapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by tjnorris on 12/8/15.
 */
public class ToolbarAppCompatActivity extends AppCompatActivity {

    protected void setToolbar(Toolbar toolbar, boolean toolbarHomeAsUpEnabled) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.home);
            getSupportActionBar().setDisplayHomeAsUpEnabled(toolbarHomeAsUpEnabled);
        }
    }
}
