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

    CurrenciesFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        CurrenciesFragment resultFragment = new CurrenciesFragment();
        resultFragment.setParams(position);
        return resultFragment;
    }

    @Override
    public int getCount() {
        AppData appData = MainActivity.getApplicationData();
        if (appData == null) {
            return 0;
        }
//        if (appData.getSubscribedData() == null){ // Unnecessary check.
//            return 0;
//        }
        return appData.getSubscribedData().size();
    }
}