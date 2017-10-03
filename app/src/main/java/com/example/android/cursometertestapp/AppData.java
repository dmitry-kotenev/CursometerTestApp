package com.example.android.cursometertestapp;

/**
 * Class to keep track of application data while execution.
 *
 * Класс для хранения данных приложения во время выполнения.
 */

public class AppData {
    private SubscribedData subscribedData;
    private AvailableCurrenciesData availableCurrenciesData;

    public SubscribedData getSubscribedData() {
        return subscribedData;
    }

    public void setSubscribedData(SubscribedData subscribedData) {
        this.subscribedData = subscribedData;
    }

    public AvailableCurrenciesData getAvailableCurrenciesData() {
        return availableCurrenciesData;
    }

    public void setAvailableCurrenciesData(AvailableCurrenciesData availableCurrenciesData) {
        this.availableCurrenciesData = availableCurrenciesData;
    }
}
