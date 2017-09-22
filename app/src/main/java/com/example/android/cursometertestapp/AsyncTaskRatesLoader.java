package com.example.android.cursometertestapp;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import android.util.StringBuilderPrinter;

import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Loader for rates.
 */

public class AsyncTaskRatesLoader extends AsyncTaskLoader<String> {

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
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        Log.e(LOG_TAG, "doInBackground is running."); // for testing
        HttpURLConnection urlConnection = CursometerUtils.createConnection(
                CursometerUtils.createUrl(urlString), "GET", cookiesString);
        //CursometerUtils.writeToConnection(urlConnection, "{\"userID\":\"exampleid174942\"}");
        String resultBody = CursometerUtils.readFromConnection(urlConnection);
        //cookiesString = CursometerUtils.getCookiesString(urlConnection);
        urlConnection.disconnect();
        return resultBody;
    }
}
