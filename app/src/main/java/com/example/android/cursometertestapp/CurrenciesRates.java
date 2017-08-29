package com.example.android.cursometertestapp;

import java.util.ArrayList;

/**
 * Класс для объекта, предназначенного для хранения данных о курсах валют и настройках пользователя
 * для одной пары валшют, для всех банков.
 */

public class CurrenciesRates {

    private String mCurrenciesName;
    private String mCurrenciesShortName;
    private ArrayList<BankRates> mBankRates;
    private boolean mIsOn = false;

    public CurrenciesRates(String currenciesName, String currenciesShortName, ArrayList<BankRates> bankRates) {
        mCurrenciesName = currenciesName;
        mCurrenciesShortName = currenciesShortName;
        mBankRates = bankRates;
    }

    public String getCurrenciesName() {
        return mCurrenciesName;
    }

    public String getCurrenciesShortName() {
        return mCurrenciesShortName;
    }

    public ArrayList<BankRates> getBankRates() {
        return mBankRates;
    }

    public boolean isOn() {
        return mIsOn;
    }

    public void setOn(boolean onoff) {
        mIsOn = onoff;
    }
}
