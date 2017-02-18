package com.escapeestudios.hermes;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SUYASH KUMAR on 2/18/2017.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "HERMESDATABASE";
    public static final int DB_VERSION = 1;

    public static final String chatTable = "CHATTABLE";
    public static final String messageTable = "MESSAGETABLE";

    private static final String FRIENDUID = "FRIENDUID";
    private static final String FRIENDNAME = "FRIENDNAME";
    private static final String LASTMESSAGE = "LASTMESSAGE";
    private static final String LASTMESSAGETIME = "LASTMESSAGETIME";
    private static final String MESSAGE = "MESSAGE";
    private static final String SENDERSELF = "SENDERSELF";
    private static final String MESSAGETIME = "MESSAGETIME";

    public ChatDatabaseHelper(Context ctx){
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if(oldVersion < 1)
        {
            db.execSQL("CREATE TABLE "+ chatTable + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FRIENDUID +" TEXT, "
            + FRIENDNAME + " TEXT, "
            + LASTMESSAGE + " TEXT, "
            + LASTMESSAGETIME + " TEXT);");

            db.execSQL("CREATE TABLE "+ messageTable + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + FRIENDUID + " TEXT, "
                    + FRIENDNAME + " TEXT, "
                    + MESSAGE + " TEXT, "
                    + SENDERSELF + " INTEGER, "
                    + MESSAGETIME+ " TEXT);");

        }
    }

    public static void insertChat(SQLiteDatabase db, ChatData chat)
    {
        ContentValues chatValue = new ContentValues();
        chatValue.put(FRIENDUID, chat.getFriendUID());
        chatValue.put(FRIENDNAME, chat.getFriendName());
        chatValue.put(LASTMESSAGE, chat.getLastMessage());
        chatValue.put(LASTMESSAGETIME, chat.getLastMessageTime());
        db.insert(chatTable, null, chatValue);
    }

    public static void insertMessage(SQLiteDatabase db, MessageData messageData)
    {
        ContentValues messageValue = new ContentValues();
        messageValue.put(FRIENDUID, messageData.getFriendUID());
        messageValue.put(FRIENDNAME, messageData.getFriendName());
        messageValue.put(MESSAGE, messageData.getMessage());
        messageValue.put(SENDERSELF, messageData.getSenderSelf());
        messageValue.put(MESSAGETIME, messageData.getMessageTime());
        db.insert(messageTable, null, messageValue);
    }
}

class MessageData{
    private String FriendUID;
    private String FriendName;
    private String message;
    private int senderSelf;
    private String messageTime;

    public void setFriendUID(String friendUID) {
        FriendUID = friendUID;
    }

    public void setFriendName(String friendName) {
        FriendName = friendName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSenderSelf(int senderSelf) {
        this.senderSelf = senderSelf;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getFriendUID() {
        return FriendUID;
    }

    public String getFriendName() {
        return FriendName;
    }

    public String getMessage() {
        return message;
    }

    public int getSenderSelf() {
        return senderSelf;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public MessageData(String friendUID, String friendName, String message, int senderSelf, String messageTime) {
        FriendUID = friendUID;
        FriendName = friendName;
        this.message = message;
        this.senderSelf = senderSelf;
        this.messageTime = messageTime;
    }
}
class ChatData{
    private String FriendUID;
    private String FriendName;
    private String lastMessage;
    private String lastMessageTime;

    public ChatData(String friendUID, String friendName, String lastMessage, String lastMessageTime) {
        FriendUID = friendUID;
        FriendName = friendName;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }

    public void setFriendUID(String friendUID) {
        FriendUID = friendUID;
    }

    public void setFriendName(String friendName) {
        FriendName = friendName;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getFriendUID() {
        return FriendUID;
    }

    public String getFriendName() {
        return FriendName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }
}
