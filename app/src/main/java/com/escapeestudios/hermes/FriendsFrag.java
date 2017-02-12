package com.escapeestudios.hermes;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFrag extends Fragment {

    public FriendsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_friends, container, false);

        Button searchFriends = (Button)rootView.findViewById(R.id.friends_search_friends);
        searchFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchFriends.class);
                getActivity().startActivity(intent);
            }
        });

        ((TextView)rootView.findViewById(R.id.friends_no_friends)).setVisibility(View.VISIBLE);
        return rootView;
    }

}
