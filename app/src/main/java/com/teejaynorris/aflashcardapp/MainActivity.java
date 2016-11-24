package com.teejaynorris.aflashcardapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.teejaynorris.aflashcardapp.database.FlashcardDatabase;
import com.teejaynorris.aflashcardapp.model.FlashCardDeck;

import java.util.List;


public class MainActivity extends ToolbarAppCompatActivity {

    private final String _TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar)findViewById(R.id.standardToolbar);
        setToolbar(toolbar, false);


        Log.d(_TAG, "In onCreate");

        // Check database
        FlashcardDatabase dbHelper = new FlashcardDatabase(this);

        Log.d(_TAG, "Calling dbHelper to get existing packs");

        List<FlashCardDeck> packs = dbHelper.getPacks();


        final ListView listView = (ListView) findViewById(R.id.the_list);

        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, packs);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                FlashCardDeck value = (FlashCardDeck) adapter.getItemAtPosition(position);
                System.out.println(value.getId());
                Intent i = new Intent(getApplicationContext(), FlashcardPackActivity.class);
                i.putExtra("PACK_ID", value.getId());
                i.putExtra("PACK_NAME", value.getName());
                startActivity(i);
            }
        });

        listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, packs) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);

                textView.setTextColor(getResources().getColor(R.color.label_text));

                return textView;
            }
        });
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

    public void goToRemoteActivity(View view) {
        Toast.makeText(MainActivity.this, "Not yet implemented", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(MainActivity.this, GetRemotePacksActivity.class);
//        MainActivity.this.startActivity(intent);
    }
}
