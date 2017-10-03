package com.example.android.cursometertestapp;

import java.util.ArrayList;

/**
 * Class represents data about quotations
 */

class SubscribedData extends ArrayList<SubscribedData.CurrencyPair> {

    static final int BUY_MIN = 0;
    static final int BUY_MAX = 1;
    static final int SALE_MIN = 2;
    static final int SALE_MAX = 3;
    public static final int NOTIFY_PERMANENT = 0;
    public static final int NOTIFY_ONCE = 1;

    CurrencyPair getCurrencyPair(int index) {
        return get(index);
    }

    static class Trigger {

        private int triggerId = -1;
        private int triggerFireType = -1;
        private int triggerType = -1;
        private float value = -1;

        Trigger(int triggerId, int triggerFireType, int triggerType, float value) {
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

        float getValue() {
            return value;
        }

        public void setValue(Float value) {
            this.value = value;
        }
    }

    static class Quotation {
        private int id = -1;
        private int from = -1;
        private float buyPriceNow = -1;
        private float salePriceNow =-1;
        private String dateTime;
        private ArrayList<Trigger> triggers;
        int precision = -1;
        boolean showSelPrice;
        int triggerFireType = -1;

        Quotation() {
            this.triggers = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                triggers.add(null);
            }
        }

        Trigger getTrigger(int triggerType) {
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

        float getBuyPriceNow() {
            return buyPriceNow;
        }

        void setBuyPriceNow(float buyPriceNow) {
            this.buyPriceNow = buyPriceNow;
        }

        float getSalePriceNow() {
            return salePriceNow;
        }

        void setSalePriceNow(float salePriceNow) {
            this.salePriceNow = salePriceNow;
        }

        String getDateTime() {
            return dateTime;
        }

        void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        int getPrecision() {
            return precision;
        }

        void setPrecision(int precision) {
            this.precision = precision;
        }

        public boolean isShowSelPrice() {
            return showSelPrice;
        }

        void setShowSelPrice(boolean showSelPrice) {
            this.showSelPrice = showSelPrice;
        }

        public int getTriggerFireType() {
            return triggerFireType;
        }

        void setTriggerFireType(Integer triggerFireType) {
            this.triggerFireType = triggerFireType;
        }

        ArrayList<Trigger> getTriggers() {
            return triggers;
        }

        void setTriggers(ArrayList<Trigger> triggers) {
            this.triggers = triggers;
        }
    }

    static class Bank {
        private String name;
        private int id = -1;
        private ArrayList<Quotation> quotations;

        boolean isShowSellPrice() {
            return showSellPrice;
        }

        void setShowSellPrice(boolean showSellPrice) {
            this.showSellPrice = showSellPrice;
        }

        private boolean showSellPrice;

        Quotation getQuotation(int quotationIndex) {
            return getQuotations().get(quotationIndex);
        }

        String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        ArrayList<Quotation> getQuotations() {
            return quotations;
        }

        void setQuotations(ArrayList<Quotation> quotations) {
            this.quotations = quotations;
        }
    }

    static class CurrencyPair {
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

        String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        public String getFullName() {
            return fullName;
        }

        void setFullName(String fullName) {
            this.fullName = fullName;
        }

        ArrayList<Bank> getBanks() {
            return banks;
        }

        void setBanks(ArrayList<Bank> banks) {
            this.banks = banks;
        }
    }
}