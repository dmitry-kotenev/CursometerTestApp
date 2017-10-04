package com.example.android.cursometertestapp;

/**
 * Class to keep track of application data while execution.
 *
 * Класс для хранения данных приложения во время выполнения.
 */

class AppData {
    private SubscribedData subscribedData = new SubscribedData();
    private AvailableCurrenciesData availableCurrenciesData = new AvailableCurrenciesData();

    SubscribedData getSubscribedData() {
        return subscribedData;
    }

    void setSubscribedData(SubscribedData subscribedData) {
        this.subscribedData = subscribedData;
    }

    public AvailableCurrenciesData getAvailableCurrenciesData() {
        return availableCurrenciesData;
    }

    void setAvailableCurrenciesData(AvailableCurrenciesData availableCurrenciesData) {
        this.availableCurrenciesData = availableCurrenciesData;
    }
}
