package com.escapeestudios.hermes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

public class NewMessageActivity extends AppCompatActivity {

    public static final String UID = "UID";
    public static final String NAME = "NAME";

    private String friendUID;
    private String friendName;

    private SQLiteOpenHelper chatDatabaseHelper;
    private SQLiteDatabase dbMessages;
    private Cursor cursorMessages;

    private MessageDisplayAdapter adapter;
    private ListView listViewChats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        Intent intent = getIntent();
        friendUID = intent.getExtras().getString(UID);
        friendName = intent.getExtras().getString(NAME);

        listViewChats = (ListView)findViewById(R.id.messageListView);


    }

    public void onResume()
    {
        createCursorAndAdapter();
    }

    private void createCursorAndAdapter()
    {

        try{
            chatDatabaseHelper = new ChatDatabaseHelper(this);
            dbMessages = chatDatabaseHelper.getWritableDatabase();

            cursorMessages = dbMessages.query(
                    ChatDatabaseHelper.messageTable,
                    new String[] {ChatDatabaseHelper.MESSAGE, ChatDatabaseHelper.SENDERSELF, ChatDatabaseHelper.MESSAGETIME},
                    ChatDatabaseHelper.FRIENDUID + " = ?",
                    new String[]{friendUID},
                    null, null, null
            );

            if(cursorMessages.moveToFirst())
            {
                adapter = new MessageDisplayAdapter(getApplicationContext(), cursorMessages, 0);
                listViewChats.setAdapter(adapter);
            }

        } catch (SQLiteException e)
        {
            Toast.makeText(this, "Database Unavailable", Toast.LENGTH_SHORT).show();
        }

    }

    public void onDestroy(){
        super.onDestroy();
        if(cursorMessages!=null)
            cursorMessages.close();
        if(dbMessages!=null)
            dbMessages.close();
    }
}
