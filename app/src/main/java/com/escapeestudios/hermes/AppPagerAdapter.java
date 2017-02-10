package com.escapeestudios.hermes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by SUYASH KUMAR on 2/10/2017.
 */

public class AppPagerAdapter extends FragmentStatePagerAdapter {

    public AppPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new FriendsFrag();
        Bundle args = new Bundle();
        // Our object is just an integer :-P
        args.putInt(FriendsFrag.ARG_OBJECT, position + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
