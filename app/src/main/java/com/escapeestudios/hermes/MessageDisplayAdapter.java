package com.escapeestudios.hermes;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by SUYASH KUMAR on 2/18/2017.
 */

public class MessageDisplayAdapter extends CursorAdapter {

    private String message;
    private int senderSelf;
    private String messageTime;

    private Context ctx;

    private LayoutInflater messageDisplayInflator;
    public MessageDisplayAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        messageDisplayInflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ctx = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = messageDisplayInflator.inflate(R.layout.single_message, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        message = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.MESSAGE));
        senderSelf = cursor.getInt(cursor.getColumnIndex(ChatDatabaseHelper.SENDERSELF));
        messageTime = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.MESSAGETIME));

        LinearLayout messageReceived = (LinearLayout)view.findViewById(R.id.message_received);
        LinearLayout messageSent = (LinearLayout)view.findViewById(R.id.message_sent);
        if(senderSelf == 1)
        {
            messageReceived.setVisibility(View.GONE);
            messageSent.setVisibility(View.VISIBLE);

            TextView sentTextView = (TextView)view.findViewById(R.id.sent_textView);
            sentTextView.setText(message);
        }
        else
        {
            messageReceived.setVisibility(View.VISIBLE);
            messageSent.setVisibility(View.GONE);

            TextView receivedTextView = (TextView)view.findViewById(R.id.received_textView);
            receivedTextView.setText(message);
        }
    }
}
