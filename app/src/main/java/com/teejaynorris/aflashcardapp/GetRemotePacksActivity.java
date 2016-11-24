package com.teejaynorris.aflashcardapp;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.teejaynorris.aflashcardapp.database.FlashcardDatabase;
import com.teejaynorris.aflashcardapp.model.DefaultFlashCardImpl;
import com.teejaynorris.aflashcardapp.model.FlashCard;
import com.teejaynorris.aflashcardapp.model.FlashCardDeck;
import com.teejaynorris.aflashcardapp.model.TextQuestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetRemotePacksActivity extends ToolbarAppCompatActivity {

    private final String _TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_remote_packs);

        Toolbar toolbar = (Toolbar)findViewById(R.id.standardToolbar);
        toolbar.setSubtitle("Remote FlashCard Packs");

        setToolbar(toolbar, true);

        final ListView listView = (ListView) findViewById(R.id.packList);

        getAllPacks(listView);
    }

    private void getAllPacks(final ListView listView) {

        if (listView == null) {
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.api_url) + "/packs";

        Log.d(_TAG, "getAllPacks url: "+url);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d(_TAG, "getAllPacks get a response!!");
                try {

                    // Get all the packs from the api
                    JSONArray jsonArray = response;

                    int count = jsonArray.length();
                    Log.d(_TAG, count + " packs found");

                    // Iterate over array and create FlashCardPacks
                    List<FlashCardDeck> packs = new ArrayList<>();
                    for (int i = 0; i < count; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        Long id = jsonObject.getLong("id");
                        packs.add(new FlashCardDeck(id, name));
                    }

                    findViewById(R.id.loadingText).setVisibility(View.INVISIBLE);
                    listView.setVisibility(View.VISIBLE);


                    /*final ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
                            android.R.layout.simple_list_item_1, packs);*/
                    final ImageAndTextList adapter = new ImageAndTextList(GetRemotePacksActivity.this, packs);

                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            FlashCardDeck pack = (FlashCardDeck) parent.getItemAtPosition(position);
                            if (_isPackAlreadyInstalled(pack)) {
                                Toast result = Toast.makeText(getApplicationContext(), "Already installed...dummy", Toast.LENGTH_SHORT);
                                result.show();
                            } else {
                                installPack(pack);
                            }
                        }
                    });

                } catch (JSONException e) {
                    Log.e(_TAG, "Uh oh, failed to get packs from API", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                ViewStub noConnectionStub = (ViewStub)findViewById(R.id.noConnectionViewStub);
                noConnectionStub.setLayoutResource(R.layout.no_connection);
                View inflatedView = noConnectionStub.inflate();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();


                String userPass = getString(R.string.api_user_string);
                String encodedUserPass = Base64.encodeToString(userPass.getBytes(), Base64.URL_SAFE);

                headers.put("Authorization", "BASIC " + encodedUserPass);
                headers.put("Accept", "application/json");


                return headers;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void installPack(final FlashCardDeck deck) {

        boolean isInstalled = _isPackAlreadyInstalled(deck);

        String api_url = (getString(R.string.api_url));
        String url =  String.format(api_url + "/packs/%d/cards", deck.getApiId());

        final FlashcardDatabase dbHelper = new FlashcardDatabase(getApplicationContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d(_TAG, "installPack got a response from the API!!!");

                try {
                    int count = response.length();

                    // Iterate over array and create FlashCardPacks
                    Set<FlashCard> cards = new HashSet<>();
                    for(int i=0; i<count; i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        FlashCard card = new DefaultFlashCardImpl();

                        String answer = jsonObject.getString("answer_text");
                        Long id = jsonObject.getLong("id");
                        String question = jsonObject.getString("question");

                        card.setAnswer(answer);
                        card.setQuestion(new TextQuestion(question));

                        cards.add(card);
                    }

                    dbHelper.storePack(deck, cards);

                } catch (JSONException e) {
                    Log.e(_TAG, "Uh oh, failed to installPacks from API", e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w(_TAG, "Unable to connect to url, can't install pack");
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();


                String userPass = "admin:P@ssw0rd1";
                String encodedUserPass = Base64.encodeToString(userPass.getBytes(), Base64.URL_SAFE);

                headers.put("Authorization", "BASIC " + encodedUserPass);
                headers.put("Accept", "application/json");


                return headers;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    private boolean _isPackAlreadyInstalled(FlashCardDeck pack) {
        boolean ans = false;

        final FlashcardDatabase dbHelper = new FlashcardDatabase(getApplicationContext());

        FlashCardDeck retrievedPack = dbHelper.getPackByApiId(pack.getApiId());

        if (retrievedPack != null) {
            ans = true;
        }

        return ans;
    }
}
