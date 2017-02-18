package com.escapeestudios.hermes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class NewMessageActivity extends AppCompatActivity {

    public static final String UID = "UID";
    public static final String NAME = "NAME";

    public static boolean databaseSyncOn = false;

    private String friendUID;
    private String friendName;

    private SQLiteOpenHelper chatDatabaseHelper;
    private SQLiteDatabase dbChat;

    private Cursor cursorMessages;

    private MessageDisplayAdapter adapter;
    private EditText messageText;
    private ListView listViewChats;

    private ImageButton sendButton;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mChatDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mChatDatabase = mFirebaseDatabase.getReference("chats");

        attachDatabaseListener();


        Intent intent = getIntent();
        friendUID = intent.getExtras().getString(UID);
        friendName = intent.getExtras().getString(NAME);

        listViewChats = (ListView)findViewById(R.id.messageListView);

        messageText = (EditText)findViewById(R.id.chat_write_message);
        sendButton = (ImageButton)findViewById(R.id.chat_send_message);

        messageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.toString().trim().length() > 0) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void attachDatabaseListener()
    {
        if(!databaseSyncOn)
        {
            mChatDatabase.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ChatOnlineData data = dataSnapshot.getValue(ChatOnlineData.class);
                    int senderSelf;
                    MessageData messageData;
                    ChatData chatData;
                    if(data.getSenderUID().equals(MainActivity.currentUser.getUid())) {
                        senderSelf = 1;
                        messageData = new MessageData(data.getReceiverUID(), data.getReceiverEmail(), data.getMessage(), senderSelf,
                                "0");
                        chatData = new ChatData(data.getReceiverUID(), data.getReceiverEmail(), data.getMessage(),
                                "0");

                    }
                    else {
                        senderSelf = 0;
                        messageData = new MessageData(data.getSenderUID(), data.getSenderEmail(), data.getMessage(), senderSelf,
                                "0");
                        chatData = new ChatData(data.getSenderUID(), data.getSenderEmail(), data.getMessage(),
                                "0");

                    }

                    ChatDatabaseHelper.insertMessage(dbChat, messageData);
                    ContentValues chatValue = new ContentValues();

                    chatValue.put(ChatDatabaseHelper.FRIENDUID, friendUID);
                    chatValue.put(ChatDatabaseHelper.FRIENDNAME, friendName);
                    chatValue.put(ChatDatabaseHelper.LASTMESSAGE, data.getMessage());
                    chatValue.put(ChatDatabaseHelper.LASTMESSAGETIME, data.getMessageTime());

                    dbChat.replace(ChatDatabaseHelper.chatTable, null, chatValue);

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            databaseSyncOn = true;
        }
    }

    private void sendMessage()
    {
        Toast.makeText(this,"clicked",Toast.LENGTH_SHORT).show();
        String message = messageText.getText().toString();
        EditText editText = (EditText)findViewById(R.id.chat_write_message);
        editText.setText("");
        pushToOnlineDatabase(message);
        updateChatTable(message);
        updateMessagesTable(message);

    }

    private void pushToOnlineDatabase(String message)
    {
        Query query = mChatDatabase.push();
        ChatOnlineData onlineData = new ChatOnlineData(MainActivity.currentUser.getUid(),
                friendUID,
                message,
                "0",
                MainActivity.currentUser.getEmail(),
                friendName);

        query.getRef().setValue(onlineData);
    }

    private void updateMessagesTable(String message)
    {
        MessageData messageData;
        messageData = new MessageData(friendUID, friendName, message, 1,
                "0");
        ChatDatabaseHelper.insertMessage(dbChat, messageData);
    }


    private void updateChatTable(String message)
    {
        ChatData chatData = new ChatData(friendUID, friendName, message,
                "0");

        ContentValues chatValue = new ContentValues();

        chatValue.put(ChatDatabaseHelper.FRIENDUID, friendUID);
        chatValue.put(ChatDatabaseHelper.FRIENDNAME, friendName);
        chatValue.put(ChatDatabaseHelper.LASTMESSAGE, message);
        chatValue.put(ChatDatabaseHelper.LASTMESSAGETIME, "0");

        dbChat.replace(ChatDatabaseHelper.chatTable, null, chatValue);

    }

    public void onResume()
    {
        super.onResume();
        createCursorAndAdapter();
    }

    private void createCursorAndAdapter()
    {

        try{
            chatDatabaseHelper = new ChatDatabaseHelper(this);
            dbChat = chatDatabaseHelper.getWritableDatabase();


            cursorMessages = dbChat.query(
                    ChatDatabaseHelper.messageTable,
                    new String[] {"_id",ChatDatabaseHelper.MESSAGE, ChatDatabaseHelper.SENDERSELF, ChatDatabaseHelper.MESSAGETIME},
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
        if(dbChat !=null)
            dbChat.close();
    }
}
