package com.escapeestudios.hermes;

import java.util.ArrayList;

/**
 * Created by SUYASH KUMAR on 2/11/2017.
 */

public class User {
    private String uid;
    private String name;
    private String email;
    public User(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
    }

    public User(){}
    public User(User user)
    {
        this.uid = user.uid;
        this.name= user.name;
        this.email = user.email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String toString() { return name; }
}
