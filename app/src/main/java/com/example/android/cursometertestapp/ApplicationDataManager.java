package com.example.android.cursometertestapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Singleton to load data from server and provide it to activities and fragments. Also it services
 * requests for changing data from activities and fragments.
 *
 * Singleton для загрузки данных с сервра и их предоставления в активити и Фрагменты. Так же,
 * \обрабатывает запросы на изменение данных от активити и фрагментов.
 */

class ApplicationDataManager {

    /**
     * Class to make async request to the server to get cookie string to serve following requests.
     */
    private class AuthorizationAsyncTask extends AsyncTask<String, Long, String> {

        /**
         * @param params - param[0] - API url for authorization POST request; param[0] - unique
         *               user ID.
         * @return String - cookies in form of one string.
         */
        @Override
        protected String doInBackground(String... params) {
            return CursometerUtils.makeAuthorizationPostRequest(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(String tempCookiesString) {
            cookiesString = tempCookiesString;
            downloadAppData();
        }
    }

    private class DownloadDataAsyncTask extends AsyncTask<String, Long, AppData> {

        @Override
        protected AppData doInBackground(String... params) {

            // params[0] - urlCurrentSubscriptionString
            // params[1] - urlAvailableCurrString

            AppData resultAppData = new AppData();
            JSONObject subscribedDataJSON = CursometerUtils.makeGetRequest(params[0],
                    cookiesString);
            JSONObject availableCurrenciesDataJSON = CursometerUtils.
                    makePostRequest(params[1],
                            cookiesString,
                            "{\"lang\": 0}" /* temporarily hardcoded. */);
            resultAppData.setSubscribedData(CursometerUtils.
                    getSubscribedDataFromJSONResponse(subscribedDataJSON));
            resultAppData.setAvailableCurrenciesData(CursometerUtils.
                    getAvailableCurrenciesDataFromJSON(availableCurrenciesDataJSON));
            return resultAppData;
        }

        @Override
        protected void onPostExecute(AppData applicationData) {
            appData = applicationData;
            dataUpdated();
        }
    }

    private static final String CURRENCY_SUBSCRIPTION_REQUEST_API_ENDPOINT =
            "http://currency.btc-solutions.ru:8080/api/CurrencySubscription?Lang=0";
    private static final String AUTHORIZATION_REQUEST_API_ENDPOINT =
            "http://currency.btc-solutions.ru:8080/api/Account";
    private static final String AVAILABLE_CURRENCIES_REQUEST_API_ENDPOINT =
            "http://currency.btc-solutions.ru:8080/api/CurrencyList";

    private static ApplicationDataManager instance = new ApplicationDataManager() ;

    private AppData appData = null;
    private String cookiesString = null;
    private ArrayList<DataUpdateListener> mListeners = new ArrayList<>();

    interface DataUpdateListener {
        void onDataUpdate(AppData appData);
        void onStartDataDownloading();
    }

    private ApplicationDataManager() {}

    public static ApplicationDataManager getInstance() {
        return instance;
    }

    /**
     * @param listener - fragment or activity, that will be updated.
     */
    public synchronized void registerDataUpdateListener(DataUpdateListener listener) {
        mListeners.add(listener);
    }

    /**
     * @param listener - fragment or activity, that will be updated.
     */
    public synchronized void unregisterDataUpdateListener(DataUpdateListener listener) {
        mListeners.remove(listener);
    }

    public synchronized void dataUpdated() {
        for (DataUpdateListener listener : mListeners) {
            listener.onDataUpdate(appData);
        }
    }

    synchronized void getData() {
        if (appData != null) {
            dataUpdated();
            return;
        }
        if (cookiesString != null) {
            // get data from server
            downloadAppData();
            return;
        }
        new AuthorizationAsyncTask().execute(AUTHORIZATION_REQUEST_API_ENDPOINT, "exampleid174942"); // Explicit user ID is temporarily here.
        }

//    private synchronized void setAppData(AppData seatedAppData) {
//        appData = seatedAppData;
//    }

//    private synchronized String getCookiesString() {
//        return cookiesString;
//    }

//    private synchronized void setCookieString(String seatedCookiesString) {
//        cookiesString = seatedCookiesString;
//    }

    private  void downloadAppData() {
        new DownloadDataAsyncTask().execute(CURRENCY_SUBSCRIPTION_REQUEST_API_ENDPOINT,
                AVAILABLE_CURRENCIES_REQUEST_API_ENDPOINT);
    }

    private static synchronized void getAuthorization() {
        // TODO make authorization request and set cookies string.
    }
}
