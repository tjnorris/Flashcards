package com.teejaynorris.aflashcardapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.teejaynorris.aflashcardapp.database.FlashcardDatabase;
import com.teejaynorris.aflashcardapp.model.FlashCardDeck;

import java.util.List;
import java.util.Set;

/**
 * Created by tjnorris on 10/5/15.
 */
public class ImageAndTextList extends ArrayAdapter<FlashCardDeck> {

    private final Activity context;
    private final FlashCardDeck[] web;
    private final Integer[] imageId;

    public ImageAndTextList(Activity context, FlashCardDeck[] web, Integer[] imageId) {

        super(context, R.layout.image_right_list_single, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;

    }
    public ImageAndTextList(Activity context, List<FlashCardDeck> packs) {

        super(context, R.layout.image_right_list_single, packs.toArray(new FlashCardDeck[packs.size()]));
        this.context = context;
        this.web = packs.toArray(new FlashCardDeck[packs.size()]);

        imageId = new Integer[packs.size()];

        // Get all existing pack ids
        final FlashcardDatabase dbHelperMain = new FlashcardDatabase(context);

        Set<Long> apiIdSet = dbHelperMain.getAllPackApiIds();

        for (int i=0; i<packs.size(); i++) {
            // If we already have the api id in our system, indicate that and don't allow for download
            FlashCardDeck pack = packs.get(i);
            if (apiIdSet.contains(pack.getApiId())) {
                imageId[i] = R.drawable.ic_download_done;
            }
            else {
                imageId[i] = R.drawable.ic_add_pack;
            }
        }

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.image_right_list_single, null, true);

        TextView rowText = (TextView) rowView.findViewById(R.id.list_text);
        ImageView imgView = (ImageView) rowView.findViewById(R.id.list_img);

        rowText.setText(web[position].getName());

        imgView.setImageResource(imageId[position]);

        return rowView;
    }

}
