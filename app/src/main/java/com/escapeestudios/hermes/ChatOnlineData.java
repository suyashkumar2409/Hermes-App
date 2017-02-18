package com.escapeestudios.hermes;

/**
 * Created by SUYASH KUMAR on 2/18/2017.
 */

public class ChatOnlineData {
    private String senderUID;
    private String receiverUID;
    private String message;
    private String messageTime;
    private String senderEmail;
    private String receiverEmail;


    public String getSenderEmail() {
        return senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public ChatOnlineData(String senderUID, String receiverUID, String message, String messageTime, String senderEmail, String receiverEmail) {
        this.senderUID = senderUID;
        this.receiverUID = receiverUID;
        this.message = message;
        this.messageTime = messageTime;
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
    }

    public ChatOnlineData() {
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getSenderUID() {
        return senderUID;
    }

    public String getReceiverUID() {
        return receiverUID;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageTime() {
        return messageTime;
    }
}
