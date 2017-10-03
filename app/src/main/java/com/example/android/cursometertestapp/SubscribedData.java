package com.example.android.cursometertestapp;

import java.util.ArrayList;

/**
 * Class represents data about quotations
 */

public class SubscribedData extends ArrayList<SubscribedData.CurrencyPair> {

    public static final int BUY_MIN = 0;
    public static final int BUY_MAX = 1;
    public static final int SALE_MIN = 2;
    public static final int SALE_MAX = 3;
    public static final int NOTIFY_PERMANENT = 0;
    public static final int NOTIFY_ONCE = 1;

    public CurrencyPair getCurrencyPair(int index) {
        return get(index);
    }

    public static class Trigger {

        private int triggerId = -1;
        private int triggerFireType = -1;
        private int triggerType = -1;
        private float value = -1;

        public Trigger(int triggerId, int triggerFireType, int triggerType, float value) {
            this.triggerId = triggerId;
            this.triggerFireType = triggerFireType;
            this.triggerType = triggerType;
            this.value = value;
        }

        public int getTriggerId() {
            return triggerId;
        }

        public void setTriggerId(Integer triggerId) {
            this.triggerId = triggerId;
        }

        public int getTriggerFireType() {
            return triggerFireType;
        }

        public void setTriggerFireType(Integer triggerFireType) {
            this.triggerFireType = triggerFireType;
        }

        public int getTriggerType() {
            return triggerType;
        }

        public void setTriggerType(Integer triggerType) {
            this.triggerType = triggerType;
        }

        public float getValue() {
            return value;
        }

        public void setValue(Float value) {
            this.value = value;
        }
    }

    public static class Quotation {
        private int id = -1;
        private int from = -1;
        private float buyPriceNow = -1;
        private float salePriceNow =-1;
        private String dateTime;
        private ArrayList<Trigger> triggers;
        int precision = -1;
        boolean showSelPrice;
        int triggerFireType = -1;

        public Quotation() {
            this.triggers = new ArrayList<Trigger>();
            for (int i = 0; i < 4; i++) {
                triggers.add(null);
            }
        }

        public Trigger getTrigger(int triggerType) {
            return getTriggers().get(triggerType);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getFrom() {
            return from;
        }

        public void setFrom(int from) {
            this.from = from;
        }

        public float getBuyPriceNow() {
            return buyPriceNow;
        }

        public void setBuyPriceNow(float buyPriceNow) {
            this.buyPriceNow = buyPriceNow;
        }

        public float getSalePriceNow() {
            return salePriceNow;
        }

        public void setSalePriceNow(float salePriceNow) {
            this.salePriceNow = salePriceNow;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public int getPrecision() {
            return precision;
        }

        public void setPrecision(int precision) {
            this.precision = precision;
        }

        public boolean isShowSelPrice() {
            return showSelPrice;
        }

        public void setShowSelPrice(boolean showSelPrice) {
            this.showSelPrice = showSelPrice;
        }

        public int getTriggerFireType() {
            return triggerFireType;
        }

        public void setTriggerFireType(Integer triggerFireType) {
            this.triggerFireType = triggerFireType;
        }

        public ArrayList<Trigger> getTriggers() {
            return triggers;
        }

        public void setTriggers(ArrayList<Trigger> triggers) {
            this.triggers = triggers;
        }
    }

    public static class Bank {
        private String name;
        private int id = -1;
        private ArrayList<Quotation> quotations;

        public boolean isShowSellPrice() {
            return showSellPrice;
        }

        public void setShowSellPrice(boolean showSellPrice) {
            this.showSellPrice = showSellPrice;
        }

        private boolean showSellPrice;

        public Quotation getQuotation(int quotationIndex) {
            return getQuotations().get(quotationIndex);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public ArrayList<Quotation> getQuotations() {
            return quotations;
        }

        public void setQuotations(ArrayList<Quotation> quotations) {
            this.quotations = quotations;
        }
    }

    public static class CurrencyPair {
        private int id;
        private String name;
        private String fullName;
        ArrayList<Bank> banks;

        public Bank getBank(int bankIndex) {
            return getBanks().get(bankIndex);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public ArrayList<Bank> getBanks() {
            return banks;
        }

        public void setBanks(ArrayList<Bank> banks) {
            this.banks = banks;
        }
    }
}
