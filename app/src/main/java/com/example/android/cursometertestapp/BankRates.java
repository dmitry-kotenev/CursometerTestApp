package com.example.android.cursometertestapp;

import java.util.ArrayList;

/**
 * Класс для объектов, предназначенных для хранения данных о всех курсах валют и настройках
 * пользователя для одного банка.
 */

public class BankRates {

    private String mBankName;
    private String mLastUpdateDateAndTime;
    private ArrayList<ExchangeRate> mExRates;
    private boolean mIsBankOn = false;

    public BankRates(String bankName, ArrayList<ExchangeRate> exRates) {
        mBankName = bankName;
        mExRates = exRates;
        mLastUpdateDateAndTime = "1 января 00:00";
    }

    public String getBankName() {
        return mBankName;
    }

    public ArrayList<ExchangeRate> getExRates() {
        return mExRates;
    }

    public void setExRates(ArrayList<ExchangeRate> exRates) {
        mExRates = exRates;
    }

    public boolean isBankOn() {
        return mIsBankOn;
    }

    public void setBankOn(boolean isBankOn) {
        mIsBankOn = isBankOn;
    }

    public void setLastUpdateDateAndTime(String dateAndTime) {
        mLastUpdateDateAndTime = dateAndTime;
    }

    public String getLastUpdateDateAndTime() {
        return mLastUpdateDateAndTime;
    }
}