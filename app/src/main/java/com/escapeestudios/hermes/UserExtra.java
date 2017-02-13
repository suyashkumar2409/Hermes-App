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
    private String checkInPlace;

    public String getCheckInPlace() {
        return checkInPlace;
    }

    public void setCheckInPlace(String checkInPlace) {
        this.checkInPlace = checkInPlace;
    }


}
