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
import android.util.Log;
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

import java.util.Calendar;

public class NewMessageActivity extends AppCompatActivity {

    public static final String UID = "UID";
    public static final String NAME = "NAME";

    private String friendUID;
    private String friendName;
//
//    private SQLiteOpenHelper chatDatabaseHelper;
//    private SQLiteDatabase dbChat;

    private Cursor cursorMessages;

    private MessageDisplayAdapter adapter;
    private EditText messageText;
    private ListView listViewChats;

    private ImageButton sendButton;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mChatDatabase;
    private ChildEventListener chatChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);


//        Toast.makeText(this,String.valueOf(c.getTimeInMillis()),Toast.LENGTH_SHORT).show();


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mChatDatabase = mFirebaseDatabase.getReference("chats");



        Intent intent = getIntent();
        friendUID = intent.getExtras().getString(UID);
        friendName = intent.getExtras().getString(NAME);

        listViewChats = (ListView)findViewById(R.id.messageListView);

        messageText = (EditText)findViewById(R.id.chat_write_message);
        sendButton = (ImageButton)findViewById(R.id.chat_send_message);

//        messageText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                if (charSequence.toString().trim().length() > 0) {
//                    sendButton.setEnabled(true);
//                } else {
//                    sendButton.setEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

//        attachDatabaseListener();

    }

    public void onPause()
    {
        super.onPause();
        mChatDatabase.removeEventListener(chatChildEventListener);
    }

    private void attachDatabaseListener()
    {

            mChatDatabase.orderByChild("receiverUID").equalTo(MainActivity.currentUser.getUid())
                    .addChildEventListener(chatChildEventListener);

    }

    private void sendMessage()
    {
        Toast.makeText(this,"clicked",Toast.LENGTH_SHORT).show();
        String message = messageText.getText().toString();
        EditText editText = (EditText)findViewById(R.id.chat_write_message);
        editText.setText("");
        Calendar c = Calendar.getInstance();
        pushToOnlineDatabase(message,c);
//        updateChatTable(message,c);
//        updateMessagesTable(message,c);

    }

    private void pushToOnlineDatabase(String message, Calendar c)
    {
        Query query = mChatDatabase.push();
        ChatOnlineData onlineData = new ChatOnlineData(MainActivity.currentUser.getUid(),
                friendUID,
                message,
                String.valueOf(c.getTimeInMillis()),
                MainActivity.currentUser.getEmail(),
                friendName);

        query.getRef().setValue(onlineData);
    }

    private void updateMessagesTable(String message, Calendar c)
    {
        MessageData messageData;
        messageData = new MessageData(friendUID, friendName, message, 1,
                String.valueOf(c.getTimeInMillis()));
//        ChatDatabaseHelper.insertMessage(dbChat, messageData, adapter);
//        adapter.notifyDataSetChanged();
    }


    private void updateChatTable(String message, Calendar c)
    {
        ChatData chatData = new ChatData(friendUID, friendName, message,
                String.valueOf(c.getTimeInMillis()));

        ContentValues chatValue = new ContentValues();

        chatValue.put(ChatDatabaseHelper.FRIENDUID, friendUID);
        chatValue.put(ChatDatabaseHelper.FRIENDNAME, friendName);
        chatValue.put(ChatDatabaseHelper.LASTMESSAGE, message);
        chatValue.put(ChatDatabaseHelper.LASTMESSAGETIME, "0");

//        dbChat.replace(ChatDatabaseHelper.chatTable, null, chatValue);

    }

    public void onResume()
    {
        super.onResume();
        createCursorAndAdapter();

        if(chatChildEventListener == null)
        {
            chatChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ChatOnlineData data = dataSnapshot.getValue(ChatOnlineData.class);
                    int senderSelf;
                    MessageData messageData;
                    ChatData chatData;
                    Calendar c = Calendar.getInstance();

                    if(data.getSenderUID().equals(MainActivity.currentUser.getUid())) {
                        senderSelf = 1;
                        messageData = new MessageData(data.getReceiverUID(), data.getReceiverEmail(), data.getMessage(), senderSelf,
                                String.valueOf(c.getTimeInMillis()));
                        chatData = new ChatData(data.getReceiverUID(), data.getReceiverEmail(), data.getMessage(),
                                String.valueOf(c.getTimeInMillis()));

                    }
                    else {
                        senderSelf = 0;
                        messageData = new MessageData(data.getSenderUID(), data.getSenderEmail(), data.getMessage(), senderSelf,
                                String.valueOf(c.getTimeInMillis()));
                        chatData = new ChatData(data.getSenderUID(), data.getSenderEmail(), data.getMessage(),
                                String.valueOf(c.getTimeInMillis()));

                    }

//                    ChatDatabaseHelper.insertMessage(dbChat, messageData);
//                    ContentValues chatValue = new ContentValues();
//
//                    chatValue.put(ChatDatabaseHelper.FRIENDUID, friendUID);
//                    chatValue.put(ChatDatabaseHelper.FRIENDNAME, friendName);
//                    chatValue.put(ChatDatabaseHelper.LASTMESSAGE, data.getMessage());
//                    chatValue.put(ChatDatabaseHelper.LASTMESSAGETIME, data.getMessageTime());
//
//                    dbChat.replace(ChatDatabaseHelper.chatTable, null, chatValue);
//
////                    adapter.notifyDataSetChanged();
//                    dataSnapshot.getRef().removeValue();
//

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
            };

        }

        attachDatabaseListener();

    }

    private void createCursorAndAdapter()
    {

        try{
//            chatDatabaseHelper = new ChatDatabaseHelper(this);
//            dbChat = chatDatabaseHelper.getWritableDatabase();


//            cursorMessages = dbChat.query(
//                    ChatDatabaseHelper.messageTable,
//                    new String[] {"_id",ChatDatabaseHelper.MESSAGE, ChatDatabaseHelper.SENDERSELF, ChatDatabaseHelper.MESSAGETIME},
//                    ChatDatabaseHelper.FRIENDUID + " = ?",
//                    new String[]{friendUID},
//                    null, null, ChatDatabaseHelper.MESSAGETIME + " ASC"
//            );

//            if(cursorMessages.moveToFirst())
//            {
                adapter = new MessageDisplayAdapter(getApplicationContext(), cursorMessages, 0);
                listViewChats.setAdapter(adapter);
//            }

        } catch (SQLiteException e)
        {
            Toast.makeText(this, "Database Unavailable", Toast.LENGTH_SHORT).show();
            Log.e("BLA",e.toString());
        }
    }

    public void onDestroy(){
        super.onDestroy();
//        if(cursorMessages!=null)
//            cursorMessages.close();
//        if(dbChat !=null)
//            dbChat.close();
    }
}
