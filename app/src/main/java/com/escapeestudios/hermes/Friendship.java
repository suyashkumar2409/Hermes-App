package com.escapeestudios.hermes;

/**
 * Created by SUYASH KUMAR on 2/12/2017.
 */

public class Friendship {
    private String creator;
    private String acceptor;
    private String status;

    public Friendship() {
    }

    public Friendship(String creator, String acceptor, String status) {

        this.creator = creator;
        this.acceptor = acceptor;
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreator() {

        return creator;
    }

    public String getAcceptor() {
        return acceptor;
    }

    public String getStatus() {
        return status;
    }
}
