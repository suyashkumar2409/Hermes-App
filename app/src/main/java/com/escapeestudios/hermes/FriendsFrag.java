package com.escapeestudios.hermes;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFrag extends Fragment {
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mFriendshipDatabase;
    DatabaseReference mUsersDatabase;
    DatabaseReference mCheckinDatabase;

    private ArrayList<UserExtra> friends;
    private ArrayAdapter adapter;
    public static final String FRIENDSHIP= "friendship";
    public static final String USERS = "users";
    public static final String CHECKIN = "checkin";
    public FriendsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView =  inflater.inflate(R.layout.fragment_friends, container, false);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFriendshipDatabase = mFirebaseDatabase.getReference().child(FRIENDSHIP);
        mUsersDatabase = mFirebaseDatabase.getReference().child(USERS);
        mCheckinDatabase = mFirebaseDatabase.getReference().child(CHECKIN);

        Button searchFriends = (Button)rootView.findViewById(R.id.friends_search_friends);
        searchFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchFriends.class);
                getActivity().startActivity(intent);
            }
        });


        ((TextView)rootView.findViewById(R.id.friends_no_friends)).setVisibility(View.VISIBLE);

        adapter = new ArrayAdapter(getContext(), (ListView)rootView.findViewById(R.id.f))
        return rootView;
    }

    public void onResume()
    {
        super.onResume();
        if(MainActivity.currentUser!=null)
        {
            populateFriends();
        }

    }

    private void populateFriends()
    {
        mUsersDatabase.child(MainActivity.currentUser.getUid()).child("friends")
        .addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snap:dataSnapshot.getChildren())
                {
                    mFirebaseDatabase.getReference().child("friendship").child(snap.getValue().toString())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot: dataSnapshot.getChildren())
                            {
                                final UserExtra userExtra;
                                User temp = snapshot.getValue(User.class);

                                userExtra = new UserExtra(temp);
                                mFirebaseDatabase.getReference().child("checkin").orderByChild("uid").equalTo(temp.getUid())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for(DataSnapshot checkinsnap:dataSnapshot.getChildren())
                                                {
                                                    CheckInData checkInData = checkinsnap.getValue(CheckInData.class);
                                                    userExtra.setCheckInPlace(checkInData.getPlace());
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                friends.add(userExtra);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
