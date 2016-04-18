package com.teejaynorris.aflashcardapp;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teejaynorris.aflashcardapp.model.FlashCard;
import com.teejaynorris.aflashcardapp.model.FlashCardConstants;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SingleCardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SingleCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleCardFragment extends Fragment {

    private final String _TAG = getClass().getSimpleName();
    private static final String SHOW_ANSWER_FLAG = "SHOW_ANSWER_FLAG";

    private OnFragmentInteractionListener mListener;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment SingleCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SingleCardFragment newInstance() {
        SingleCardFragment fragment = new SingleCardFragment();

        return fragment;
    }

    public SingleCardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_single_card, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            String cardText = getArguments().getString(FlashCardConstants.KEY_CARD_VALUE);

            Log.d(_TAG, "CardText from getArguments. Key:"+FlashCardConstants.KEY_CARD_VALUE+" Value:"+cardText);

            if (cardText != null) {

                // Set the question and answer text views to correct current values
                TextView cardTextView = (TextView) getView().findViewById(R.id.card_text);

                if (cardTextView != null) {
                    cardTextView.setText(cardText);
                }
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    public void updateText(String cardText) {
        Log.d(_TAG, "Updating text to: "+cardText);

        if (cardText != null) {

            // Set the question and answer text views to correct current values
            TextView cardTextView = (TextView) getView().findViewById(R.id.card_text);

            if (cardTextView != null) {
                cardTextView.setText(cardText);
            }
        }
    }

}
