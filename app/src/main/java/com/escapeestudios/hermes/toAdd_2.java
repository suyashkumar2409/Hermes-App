package com.escapeestudios.hermes;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import layout.TransactionsFrag;

public class toAdd_2 extends Activity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    public static final String TRANSACTION = "transactions";

    private ArrayList<String> nameSuggestions;
    private ArrayAdapter<String> adapterSuggestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_add);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child(TRANSACTION);

        nameSuggestions = new ArrayList<>();
        for(UserExtra user: FriendsFrag.friends)
        {
            nameSuggestions.add(user.getName());
        }
        adapterSuggestion = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, nameSuggestions);

        AutoCompleteTextView textView = (AutoCompleteTextView)(findViewById(R.id.toAdd_toWhom_tag));
        textView.setAdapter(adapterSuggestion);


        Button button = (Button)findViewById(R.id.toAdd_submit_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView name_text = (AutoCompleteTextView) findViewById(R.id.toAdd_toWhom_tag);
                EditText description_text = (EditText)findViewById(R.id.toAdd_description_val);
                EditText price_text = (EditText)findViewById(R.id.toAdd_amount_val);
                TextView textView = (TextView)findViewById(R.id.toAdd_error_val);

                String name = name_text.getText().toString();
                if (name.equals("")) {
                    textView.setText("Please enter a Name");
                    return;
                }

                String desc = description_text.getText().toString();
                if (desc.equals("")) {
                    textView.setText("Enter a Description");
                    return;
                }

                String price = price_text.getText().toString();
                if (price.equals("")) {
                    textView.setText("Amount entry empty");
                    return;
                }

                Spinner s = (Spinner) findViewById(R.id.toAdd_owing_list);
                String choice = String.valueOf(s.getSelectedItem());

                Transaction trans = null;
                for(UserExtra user: FriendsFrag.friends)
                {
                    if(user.getName().equals(name))
                    {
                        trans = new Transaction(name, choice, desc, Integer.parseInt(price),
                                MainActivity.currentUser.getUid(), user.getUid());
                        break;
                    }
                }

                if(trans!=null) {
                    mDatabaseReference.push().setValue(trans);
//                    TransactionsFrag.arr.add(trans);
                }
                finish();
            }
        });
    }
}
