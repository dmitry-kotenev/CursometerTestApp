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
    private String urlString;

    AsyncTaskAppDataLoader(Context context, String urlString, String cookiesString) {
        super(context);
        this.cookiesString = cookiesString;
        this.urlString = urlString;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public SubscribedData loadInBackground() {
        JSONObject jsonResponse = CursometerUtils.makeGetRequest(urlString, cookiesString);
        return CursometerUtils.getDataFromJSONResponse2(jsonResponse);
    }
}