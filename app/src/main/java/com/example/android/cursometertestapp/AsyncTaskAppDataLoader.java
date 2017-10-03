package com.example.android.cursometertestapp;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import org.json.JSONObject;

/**
 * Class to load application data from server.
 */

class AsyncTaskAppDataLoader extends AsyncTaskLoader<SubscribedData> {

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
    public SubscribedData loadInBackground() {
        JSONObject jsonResponse = CursometerUtils.makeGetRequest(urlCurrentSubscriptionString,
                cookiesString);

        // for testing (start):
        JSONObject jsonResponse2 = CursometerUtils.makePostRequest(urlAvailableCurrString,
                cookiesString,
                "{\"lang\": 0}" /* temporarily hardcoded. */);
        if (jsonResponse2 != null) {
            Log.v(LOG_TAG, "Available currencies in JSON: " + jsonResponse2.toString());
        }

        AvailableCurrenciesData availCurrData =
                CursometerUtils.getAvailableCurrenciesDataFromJSON(jsonResponse2);
        String toPrint = "";
        for (int i = 0; i < availCurrData.size(); i++) {
            toPrint += (availCurrData.getCurrenciesPair(i).getName() + "; ");
        }
        Log.v(LOG_TAG, "Currencies pairs in program data: " + toPrint);
        // for testing (end).

        return CursometerUtils.getSubscribedDataFromJSONResponse(jsonResponse);
    }
}