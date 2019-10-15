package com.app.washmania.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.app.washmania.fragments.FragmentKids;
import com.app.washmania.fragments.FragmentMen;
import com.app.washmania.fragments.FragmentWomen;

public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                FragmentMen tab1 = new FragmentMen();
                return tab1;
            case 1:
                FragmentWomen tab2 = new FragmentWomen();
                return tab2;
            case 2:
                FragmentKids tab3 = new FragmentKids();
                return tab3;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
