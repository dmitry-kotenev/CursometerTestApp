package com.example.android.cursometertestapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import java.util.ArrayList;

/**
 * Адаптер для ViewPager в котором показыватся курсы валют.
 */

public class CurrenciesFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private MainActivity mainActivityInstance;

    public CurrenciesFragmentPagerAdapter(FragmentManager fm, MainActivity mainActivity) {
        super(fm);
        mainActivityInstance = mainActivity;
    }

    @Override
    public Fragment getItem(int position) {
        CurrenciesFragment resultFragment = new CurrenciesFragment();
        resultFragment.setParams(position);
        return resultFragment;
    }

    // Стандартный метод getPageTitle не используется, т. к. его использование приводит к появлению
    // заголовков страниц в TabLayout.
    public String getFragmentShortTitle(int position) {
        return mainActivityInstance.getApplicationCurrentData().get(position).
                getCurrenciesShortName();
    }

    @Override
    public int getCount() {
        ArrayList<CurrenciesRates> currData = mainActivityInstance.getApplicationCurrentData();
        return currData.size();
    }

    // https://stackoverflow.com/questions/28116710/pageradapter-notifydatasetchanged-does-not-refresh-fragments
    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }
}