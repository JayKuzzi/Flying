package com.bb.offerapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bb.offerapp.fragment.viewpaper.AFragment;
import com.bb.offerapp.fragment.viewpaper.BFragment;
import com.bb.offerapp.fragment.viewpaper.OrderAFragment;
import com.bb.offerapp.fragment.viewpaper.OrderBFragment;
import com.bb.offerapp.fragment.viewpaper.OrderCFragment;
import com.bb.offerapp.fragment.viewpaper.OrderDFragment;
import com.bb.offerapp.fragment.viewpaper.OrderEFragment;

/**
 * MyViewPaperAdapter
 *
 * Created by bb on 2017/3/7.
 */

public class MyViewPaperAdapterInOrder extends FragmentPagerAdapter {


    OrderAFragment orderAFragment;
    OrderBFragment orderBFragment;
    OrderCFragment orderCFragment;
    OrderDFragment orderDFragment;
    OrderEFragment orderEFragment;
    private String[] tabTittle;

    public MyViewPaperAdapterInOrder(FragmentManager fm, String[] tabTittle) {
        super(fm);
        this.tabTittle = tabTittle;
    }



    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
           return orderAFragment=new OrderAFragment();
        }
        if (position == 1) {
            return orderBFragment=new OrderBFragment();
        }
        if (position == 2) {
            return orderCFragment=new OrderCFragment();
        }
        if (position == 3) {
            return orderDFragment=new OrderDFragment();
        }
        if (position == 4) {
            return orderEFragment=new OrderEFragment();
        }
        return orderAFragment=new OrderAFragment();
    }

    @Override
    public int getCount() {
        return tabTittle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTittle[position];
    }


    public OrderAFragment getOrderAFragment() {
        return orderAFragment;
    }

    public OrderBFragment getOrderBFragment() {
        return orderBFragment;
    }

    public OrderCFragment getOrderCFragment() {
        return orderCFragment;
    }

    public OrderDFragment getOrderDFragment() {
        return orderDFragment;
    }

    public OrderEFragment getOrderEFragment() {
        return orderEFragment;
    }
}
