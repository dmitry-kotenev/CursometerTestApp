package com.example.android.cursometertestapp;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import org.json.JSONObject;

/**
 * Class to load application data from server.
 */

class AsyncTaskAppDataLoader extends AsyncTaskLoader<AppData> {

    private static final String LOG_TAG = "AsyncTaskAppDataLoader";

    private String cookiesString;
    private String urlCurrentSubscriptionString;
    private String urlAvailableCurrString;

    AsyncTaskAppDataLoader(Context context, String urlCurrentSubscriptionString,
                           String urlAvailableCurrString, String cookiesString) {
        super(context);
        this.cookiesString = cookiesString;
        this.urlCurrentSubscriptionString = urlCurrentSubscriptionString;
        this.urlAvailableCurrString = urlAvailableCurrString;

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public AppData loadInBackground() {
        AppData resultAppData = new AppData();
        JSONObject subscribedDataJSON = CursometerUtils.makeGetRequest(urlCurrentSubscriptionString,
                cookiesString);
        JSONObject availableCurrenciesDataJSON = CursometerUtils.
                makePostRequest(urlAvailableCurrString,
                cookiesString,
                "{\"lang\": 0}" /* temporarily hardcoded. */);
        resultAppData.setSubscribedData(CursometerUtils.
                getSubscribedDataFromJSONResponse(subscribedDataJSON));
        resultAppData.setAvailableCurrenciesData(CursometerUtils.
                getAvailableCurrenciesDataFromJSON(availableCurrenciesDataJSON));
        return resultAppData;
    }
}