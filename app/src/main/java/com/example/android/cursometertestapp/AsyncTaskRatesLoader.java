package com.example.android.cursometertestapp;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.List;

/**
 * Loader for rates.
 */

public class AsyncTaskRatesLoader extends AsyncTaskLoader<List<CurrenciesRates>> {

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
    public List<CurrenciesRates> loadInBackground() {
        Log.e(LOG_TAG, "doInBackground is running."); // for testing

        JSONObject jsonResponse = CursometerUtils.makeGetRequest(urlString, cookiesString);
        return CursometerUtils.getDataFromJSONResponse(jsonResponse);
    }
}
