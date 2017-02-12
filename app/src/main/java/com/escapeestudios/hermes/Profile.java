package com.escapeestudios.hermes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;


    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child(USERS);

        final TextView profile_name = (TextView)findViewById(R.id.profile_name);
        final TextView profile_email = (TextView)findViewById(R.id.profile_email);


        Intent intent = getIntent();
        uid = intent.getStringExtra(UID);

        Query query = mDatabaseReference.orderByChild("uid").equalTo(uid);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                    Toast.makeText(CheckIn.this, dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                for(DataSnapshot snap:dataSnapshot.getChildren())
                {
                    User dat = snap.getValue(User.class);
                    profile_name.setText(dat.getName());
                    profile_email.setText(dat.getEmail());
                    

                }
//                    Toast.makeText(CheckIn.this, "AT", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }





    }
}
