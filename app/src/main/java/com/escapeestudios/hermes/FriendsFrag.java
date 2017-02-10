package com.escapeestudios.hermes;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFrag extends Fragment {

    public static final String ARG_OBJECT = "ARG_OBJECT";

    public FriendsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_friends, container, false);

        Bundle args = getArguments();
        ((TextView) rootView.findViewById(R.id.friends_test)).setText(
                Integer.toString(args.getInt(ARG_OBJECT)));
        return rootView;
    }

}
