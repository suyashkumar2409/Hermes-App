package com.escapeestudios.hermes;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class Transaction {
    private String name;
    private String whoOwesWho;
    private String description;
    private int amount;

    public Transaction(int amount, String name, String whoOwesWho, String Description) {
        this.name = name;
        this.amount = amount;
        this.description = Description;
        this.whoOwesWho = whoOwesWho;
    }

    public String toString() {
        String out = description + " between you and " + name + "\n";
        if (whoOwesWho.equals("I owe him"))
            out += "You owe him " + amount;
        else if (whoOwesWho.equals("He owes me"))
            out += "He owes you " + amount;
        else
            out += "Equal split of " + amount;

        return out;
    }

    public int getAmount() {return amount;}

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getWhoOwesWho() {
        return whoOwesWho;
    }
}