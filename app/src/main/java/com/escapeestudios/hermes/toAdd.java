package com.escapeestudios.hermes;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import layout.TransactionsFrag;

public class toAdd extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_add);

        Button button = (Button)findViewById(R.id.toAdd_submit_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText name_text = (EditText)findViewById(R.id.toAdd_toWhom_tag);
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

                TransactionsFrag.arr.add(new Transaction(Integer.parseInt(price), name, choice, desc));
                finish();
            }
        });
    }
}
