package com.escapeestudios.hermes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CheckIn extends AppCompatActivity {
    public static final String FUNCTIONALITY = "FUNCTIONALITY";
    public static final String CHECK_IN_FIREBASE = "checkin";
    public static final String UID = "UID";
    public static final int CHECK_IN = 31;
    public static final int CHECK_OUT = 32;

    private String uid;
    private String place;
    private int currentFunctionality;

    FirebaseDatabase Database;
    DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        Database = FirebaseDatabase.getInstance();
        mDatabaseReference = Database.getReference().child(CHECK_IN_FIREBASE);
        Intent intent = getIntent();
        uid = intent.getExtras().getString(UID);
        final Button submitButton = (Button)findViewById(R.id.check_in_submit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInButtonClicked();
            }
        });
        final EditText editPlace = (EditText)findViewById(R.id.check_in_place_name);

        final TextView currentPlace = (TextView)findViewById(R.id.check_in_current_place);


        currentFunctionality = intent.getExtras().getInt(FUNCTIONALITY);
        if(currentFunctionality == CHECK_IN)
        {


            editPlace.setVisibility(View.VISIBLE);
            submitButton.setText(R.string.check_in_submit);
            currentPlace.setVisibility(View.GONE);

            editPlace.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(editPlace.getText().toString().length() == 0)
                        submitButton.setEnabled(false);
                    else
                        submitButton.setEnabled(true);
                }
            });
        }
        else if(currentFunctionality == CHECK_OUT)
        {
            editPlace.setVisibility(View.GONE);
            submitButton.setText(R.string.check_out_submit);

            Query query = mDatabaseReference.orderByChild("UID").equalTo(uid);


            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        CheckInData data = dataSnapshot.getValue(CheckInData.class);
                        currentPlace.setText("Currently Checked in at "+data.getPlace());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            currentPlace.setVisibility(View.VISIBLE);
        }
    }

    public void checkInButtonClicked()
    {
        if(currentFunctionality==CHECK_IN) {
            place = ((EditText) findViewById(R.id.check_in_place_name)).getText().toString();
            CheckInData data = new CheckInData(uid, place);
            mDatabaseReference.push().setValue(data);
            finish();
        }
        else if(currentFunctionality == CHECK_OUT)
        {
            Query query = mDatabaseReference.orderByChild("UID").equalTo(uid);
            query.getRef().removeValue();
            finish();
        }
    }
}

class CheckInData{
    private String UID;
    private String place;

    public CheckInData(String UID, String place) {
        this.UID = UID;
        this.place = place;
    }

    public String getUID() {
        return UID;
    }

    public String getPlace() {
        return place;
    }
}
