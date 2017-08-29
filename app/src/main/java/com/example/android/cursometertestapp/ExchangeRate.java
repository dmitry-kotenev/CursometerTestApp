package com.example.android.cursometertestapp;

/**
 * Класс для объета, в котором хранятся данные об одном обменном курсе для одной пары валют для
 * одного банка
 */

public class ExchangeRate {

    private int mMinimumAmount = 1;
    private float mBuyPrice;
    private float mSalePrice;

    private float mBuyUpMargin;
    private float mBuyLowMargin;
    private float mSaleUpMargin;
    private float mSaleLowMargin;

    private boolean mBuyUpMarginOn = false;
    private boolean mBuyLowMarginOn = false;
    private boolean mSaleUpMarginOn = false;
    private boolean mSaleLowMarginOn = false;

    public ExchangeRate(int minimumAmount, float buyPrice, float salePrice) {
        setMinimumAmount(minimumAmount);
        setBuyPrice(buyPrice);
        setSalePrice(salePrice);

        //Setting default margins:

        setBuyUpMargin(buyPrice);
        setBuyLowMargin(buyPrice);
        setSaleUpMargin(salePrice);
        setSaleLowMargin(salePrice);
    }

    public boolean isBuyUpMarginOn() {
        return mBuyUpMarginOn;
    }

    public void setBuyUpMarginOn(boolean buyUpMarginOn) {
        mBuyUpMarginOn = buyUpMarginOn;
    }

    public boolean isBuyLowMarginOn() {
        return mBuyLowMarginOn;
    }

    public void setBuyLowMarginOn(boolean buyLowMarginOn) {
        mBuyLowMarginOn = buyLowMarginOn;
    }

    public boolean isSaleUpMarginOn() {
        return mSaleUpMarginOn;
    }

    public void setSaleUpMarginOn(boolean saleUpMarginOn) {
        mSaleUpMarginOn = saleUpMarginOn;
    }

    public boolean isSaleLowMarginOn() {
        return mSaleLowMarginOn;
    }

    public void setSaleLowMarginOn(boolean saleLowMarginOn) {
        mSaleLowMarginOn = saleLowMarginOn;
    }

    public float getBuyUpMargin() {
        return mBuyUpMargin;
    }

    public void setBuyUpMargin(float buyUpMargin) {
        mBuyUpMargin = buyUpMargin;
    }

    public float getBuyLowMargin() {
        return mBuyLowMargin;
    }

    public void setBuyLowMargin(float buyLowMargin) {
        mBuyLowMargin = buyLowMargin;
    }

    public float getSaleUpMargin() {
        return mSaleUpMargin;
    }

    public void setSaleUpMargin(float saleUpMargin) {
        mSaleUpMargin = saleUpMargin;
    }

    public float getSaleLowMargin() {
        return mSaleLowMargin;
    }

    public void setSaleLowMargin(float saleLowMargin) {
        mSaleLowMargin = saleLowMargin;
    }

    public float getBuyPrice() {
        return mBuyPrice;
    }

    public void setBuyPrice(float buyPrice) {
        mBuyPrice = buyPrice;
    }

    public float getSalePrice() {
        return mSalePrice;
    }

    public void setSalePrice(float salePrice) {
        mSalePrice = salePrice;
    }

    public int getMinimumAmount() {
        return mMinimumAmount;
    }

    public void setMinimumAmount(int minimumAmount) {
        mMinimumAmount = minimumAmount;
    }
}
