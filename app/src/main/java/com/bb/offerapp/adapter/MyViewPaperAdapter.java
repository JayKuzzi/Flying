package com.bb.offerapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bb.offerapp.fragment.viewpaper.AFragment;
import com.bb.offerapp.fragment.viewpaper.BFragment;

/**
 * MyViewPaperAdapter
 *
 * Created by bb on 2017/3/7.
 */

public class MyViewPaperAdapter extends FragmentPagerAdapter {

    AFragment aFragment;
    BFragment bFragment;

    private String[] tabTittle;

    public MyViewPaperAdapter(FragmentManager fm, String[] tabTittle) {
        super(fm);
        this.tabTittle = tabTittle;
    }



    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
           return aFragment=new AFragment();
        }
        if (position == 1) {
            return bFragment=new BFragment();
        }
        return aFragment=new AFragment();
    }

    @Override
    public int getCount() {
        return tabTittle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTittle[position];
    }


    public AFragment getaFragment() {
        return aFragment;
    }
}
