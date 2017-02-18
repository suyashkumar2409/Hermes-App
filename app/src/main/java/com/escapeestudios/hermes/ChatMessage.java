package com.escapeestudios.hermes;

/**
 * Created by SUYASH KUMAR on 2/18/2017.
 */

public class ChatMessage {

    private String text;
    private String senderID;
    private String receiverID;
    private String time;

    public ChatMessage() {
    }

    public ChatMessage(String text, String senderID, String receiverID, String time) {
        this.text = text;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public String getTime() {
        return time;
    }
}
