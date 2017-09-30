package com.example.android.cursometertestapp;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

/**
 * Loader for rates.
 */

public class AsyncTaskRatesLoader extends AsyncTaskLoader<SubscribedData> {

    private static final String LOG_TAG = "AsyncTaskLoader";

    private String cookiesString;
    private String urlString;

    public AsyncTaskRatesLoader(Context context, String urlString, String cookiesString) {
        super(context);
        this.cookiesString = cookiesString;
        this.urlString = urlString;
    }

    @Override
    protected void onStartLoading() {
        Log.e(LOG_TAG, "onStartLoading is running."); // for testing
        forceLoad();
    }

    @Override
    public SubscribedData loadInBackground() {
        Log.e(LOG_TAG, "doInBackground is running."); // for testing

        JSONObject jsonResponse = CursometerUtils.makeGetRequest(urlString, cookiesString);

        // ***for testing:
        SubscribedData subscribedData = CursometerUtils.getDataFromJSONResponse2(jsonResponse);
//        Log.v("Loader", "Data Size: " + subscribedData.size());
//        int saleMinTriggerID = subscribedData.get(1).getBanks().get(0).getQuotations().get(0).
//                getTriggers().get(SubscribedData.SALE_MAX).getTriggerId();
//        Log.v("Loader", "Sale min trigger ID: " + saleMinTriggerID);
        // ***

        return CursometerUtils.getDataFromJSONResponse2(jsonResponse);
    }
}
