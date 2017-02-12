package com.escapeestudios.hermes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Profile extends AppCompatActivity {

    public static final String UID = "UID";
    public static final String USERS = "users";
    public static final String FRIENDSHIP = "friendship";

    private static final String SEND_REQUEST = "Send Friend Request";
    private static final String SENT_REQUEST = "Friend Request Sent";
    private static final String ACCEPT_REQUEST = "Accept Friend Request";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserDatabase;
    private DatabaseReference mFriendshipDatabase;

    private TextView currentlyFriends;
    private TextView profile_name;
    private TextView profile_email;
    private Button profile_button;

    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserDatabase = mFirebaseDatabase.getReference().child(USERS);
        mFriendshipDatabase = mFirebaseDatabase.getReference().child(FRIENDSHIP);

        profile_name = (TextView)findViewById(R.id.profile_name);
        profile_email = (TextView)findViewById(R.id.profile_email);
        currentlyFriends = (TextView)findViewById(R.id.profile_friends);
        profile_button = (Button)findViewById(R.id.profile_button);


        Intent intent = getIntent();
        uid = intent.getStringExtra(UID);

        Query queryUsers = mUserDatabase.orderByChild("uid").equalTo(uid);

        queryUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                    Toast.makeText(CheckIn.this, dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    User dat = snap.getValue(User.class);
                    profile_name.setText(dat.getName());
                    profile_email.setText(dat.getEmail());

                }
//                    Toast.makeText(CheckIn.this, "AT", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        Query queryCreator = mFriendshipDatabase.orderByChild("creator").equalTo(MainActivity.currentUser.getUid());
        Query queryAcceptor = mFriendshipDatabase.orderByChild("acceptor").equalTo(MainActivity.currentUser.getUid());

        profile_button.setVisibility(View.VISIBLE);
        currentlyFriends.setVisibility(View.GONE);
        profile_button.setText(SEND_REQUEST);
        profile_button.setEnabled(true);

        queryCreator.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                    Toast.makeText(CheckIn.this, dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Friendship dat = snap.getValue(Friendship.class);
                    if(dat.getAcceptor().equals(uid))
                    {
                        if(dat.getStatus().equals("accepted"))
                        {
                            profile_button.setVisibility(View.GONE);
                            currentlyFriends.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            profile_button.setVisibility(View.VISIBLE);
                            profile_button.setText(SENT_REQUEST);
                            profile_button.setEnabled(false);
                        }
                    }
                }
//                    Toast.makeText(CheckIn.this, "AT", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        queryAcceptor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                    Toast.makeText(CheckIn.this, dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Friendship dat = snap.getValue(Friendship.class);
                    if(dat.getCreator().equals(uid))
                    {
                        if(dat.getStatus().equals("accepted"))
                        {
                            profile_button.setVisibility(View.GONE);
                            currentlyFriends.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            profile_button.setVisibility(View.VISIBLE);
                            profile_button.setText(ACCEPT_REQUEST);
                            profile_button.setEnabled(true);
                        }
                    }
                }
//                    Toast.makeText(CheckIn.this, "AT", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (profile_button.getText().toString())
                {
                    case SEND_REQUEST:
                        mFriendshipDatabase.push().setValue(new Friendship(MainActivity.currentUser.getUid(),uid,"sent"));
                        profile_button.setText(SENT_REQUEST);
                        profile_button.setEnabled(false);
                        break;
                    case ACCEPT_REQUEST:
                        Query query = mFriendshipDatabase.orderByChild("acceptor").equalTo(MainActivity.currentUser.getUid());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
//                    Toast.makeText(CheckIn.this, dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                    Friendship dat = snap.getValue(Friendship.class);
                                    if(dat.getCreator().equals(uid)) {
                                        dat.setStatus("accepted");
                                        snap.getRef().setValue(dat);
                                        break;
                                    }
                                }
//                    Toast.makeText(CheckIn.this, "AT", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }


                        });

                        profile_button.setVisibility(View.GONE);
                        currentlyFriends.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
