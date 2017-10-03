package com.example.android.cursometertestapp;

import java.util.ArrayList;

/**
 * Class to keep list of currency pairs that is available for subscription or subscribed yet.
 *
 * Класс для хранениея списка доступных для подписки котровок.
 */

class AvailableCurrenciesData extends ArrayList<AvailableCurrenciesData.CurrenciesPair> {

    public CurrenciesPair getCurrenciesPair(int index) {
        return this.get(index);
    }

    public void addCurrenciesPair(AvailableCurrenciesData.CurrenciesPair currenciesPair) {
        this.add(currenciesPair);
    }

    static class CurrenciesPair extends ArrayList<AvailableCurrenciesData.Bank> {
        private int id;
        private String name;
        private String fullName;
        private boolean enable;

        public void addBank(AvailableCurrenciesData.Bank bank) {
            this.add(bank);
        }

        public AvailableCurrenciesData.Bank getBank(int index) {
            return this.get(index);
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

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }
    }

    static class Bank {
        int id;

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

        public boolean isSubscribed() {
            return subscribed;
        }

        public void setSubscribed(boolean subscribed) {
            this.subscribed = subscribed;
        }

        String name;
        boolean subscribed;
    }
}