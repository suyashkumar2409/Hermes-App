package com.escapeestudios.hermes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import layout.TransactionsFrag;

/**
 * Created by SUYASH KUMAR on 2/10/2017.
 */

public class AppPagerAdapter extends FragmentStatePagerAdapter {

    public AppPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment;

        switch(position) {

            case 0:
                fragment = new FriendsFrag();
                break;
            case 1:
                fragment = new ChatsFrag();
                break;
            case 2:
                fragment = new TransactionsFrag();
                break;
            default:
                fragment = null;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
