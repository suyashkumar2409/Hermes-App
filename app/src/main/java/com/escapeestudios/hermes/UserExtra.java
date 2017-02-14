package com.escapeestudios.hermes;

/**
 * Created by SUYASH KUMAR on 2/13/2017.
 */

public class UserExtra extends User {
    public UserExtra()
    {
        super();
    }
    public UserExtra(User user)
    {
        super(user);
    }
    public String toString()
    {
        if(!checkInPlace.equals(""))
            return super.toString() + " checked in - " + checkInPlace;
        else
            return super.toString();
    }
    private String checkInPlace = "";

    public String getCheckInPlace() {
        return checkInPlace;
    }

    public void setCheckInPlace(String checkInPlace) {
        this.checkInPlace = checkInPlace;
    }


}
