package com.example.android.cursometertestapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Адаптер для ViewPager в котором показыватся курсы валют.
 *
 * Adapter for ViewPager quotations are showed in.
 */

class CurrenciesFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private MainActivity mainActivityInstance;


    CurrenciesFragmentPagerAdapter(FragmentManager fm, MainActivity mainActivity) {
        super(fm);
        mainActivityInstance = mainActivity;
    }

    @Override
    public Fragment getItem(int position) {
        CurrenciesFragment resultFragment = new CurrenciesFragment();
        resultFragment.setParams(position);
        return resultFragment;
    }

    @Override
    public int getCount() {
       SubscribedData currData = mainActivityInstance.getApplicationCurrentSubscribedData();
        if (currData == null) {
            return 0;
        }
        return currData.size();
    }
}