package com.escapeestudios.hermes;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    private static boolean populated;
    public static ArrayList<UserExtra> friends = new ArrayList<>();
    private View rootView;
    private ArrayAdapter adapter;
    public static final String FRIENDSHIP= "friendship";
    public static final String USERS = "users";
    public static final String CHECKIN = "checkin";

    private ValueEventListener friendListener;
    public FriendsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView =  inflater.inflate(R.layout.fragment_friends, container, false);
        populated = false;

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




        adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1, friends);
        ListView listView =  (ListView)rootView.findViewById(R.id.friends_list);
        listView.setAdapter(adapter);
        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewMessageActivity.class);
                UserExtra extra = friends.get(position);
                intent.putExtra(NewMessageActivity.UID, extra.getUid());
                intent.putExtra(NewMessageActivity.NAME, extra.getName());

                startActivity(intent);
            }
        });


        return rootView;
    }

////    public void onResume() {
//        super.onResume();
//        friends.clear();
////        if (populated == false) {
//            populateFriends();
////        }
//
//
//    }

    private void refreshLook()
    {
//        Toast.makeText(getContext(),"hello",Toast.LENGTH_SHORT).show();
        if(friends.size()==0)
        {
//            Toast.makeText(getContext(),"hi",Toast.LENGTH_SHORT).show();

            ((TextView)rootView.findViewById(R.id.friends_no_friends)).setVisibility(View.VISIBLE);
            ((ListView)rootView.findViewById(R.id.friends_list)).setVisibility(View.GONE);
        }
        else
        {
            ((TextView)rootView.findViewById(R.id.friends_no_friends)).setVisibility(View.GONE);
            ((ListView)rootView.findViewById(R.id.friends_list)).setVisibility(View.VISIBLE);
        }

    }

    public void onPause()
    {
        super.onPause();
        mUsersDatabase.child(MainActivity.currentUser.getUid()).child("friends").removeEventListener(friendListener);
    }

    public void onResume()
    {
        super.onResume();
        if(friendListener==null)
            createListener();
        friends.clear();
        mUsersDatabase.child(MainActivity.currentUser.getUid()).child("friends").addValueEventListener(friendListener);
    }
    private void createListener()
    {
        friendListener = new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(final DataSnapshot snap:dataSnapshot.getChildren())
                {
                    mFirebaseDatabase.getReference().child("friendship").child(snap.getValue().toString())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Query querytemp;
                                    Log.e("Prob",dataSnapshot.toString());
                                    Friendship temp = dataSnapshot.getValue(Friendship.class);
                                    if (temp.getStatus().equals("accepted")) {
                                        if (temp.getCreator().equals(MainActivity.currentUser.getUid())) {
                                            querytemp = mUsersDatabase.child(temp.getAcceptor());
                                        } else {
                                            querytemp = mUsersDatabase.child(temp.getCreator());
                                        }

                                        querytemp.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                final UserExtra userExtra;
                                                User temp = dataSnapshot.getValue(User.class);

                                                userExtra = new UserExtra(temp);
                                                mFirebaseDatabase.getReference().child("checkin").orderByChild("uid").equalTo(temp.getUid())
                                                        .addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                for (DataSnapshot checkinsnap : dataSnapshot.getChildren()) {
                                                                    CheckInData checkInData = checkinsnap.getValue(CheckInData.class);
                                                                    userExtra.setCheckInPlace(checkInData.getPlace());
//                                                                    Toast.makeText(getContext(),"here",Toast.LENGTH_SHORT).show();
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });

                                                if(!friends.contains(userExtra))
                                                friends.add(userExtra);
                                                refreshLook();
                                                populated = true;
                                                adapter.notifyDataSetChanged();
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }
//
//    private void populateFriends()
//    {
////        Toast.makeText(getContext(),"happ",Toast.LENGTH_SHORT).show();
//        mUsersDatabase.child(MainActivity.currentUser.getUid()).child("friends")
//        .addValueEventListener(
//
//        );
//
//    }
}
