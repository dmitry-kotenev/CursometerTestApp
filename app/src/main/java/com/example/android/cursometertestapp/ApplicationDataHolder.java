package com.example.android.cursometertestapp;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.util.ArrayList;

/**
 * This class is created on purpose to hold global application variables in it.
 *
 * Класс для хранения глобальных переменных.
 */

public class ApplicationDataHolder implements LoaderManager.LoaderCallbacks<AppData> {

    private static AppData appData = null;
    private static String cookiesString = null;
    private static ArrayList<DataUpdateListener> mListeners = new ArrayList<>();

    interface DataUpdateListener {
        void onDataUpdate();
        void onStartDataDownloading();
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
            listener.onDataUpdate();
        }
    }

    private static synchronized AppData getAppData() {
        return appData;
    }

    private static synchronized void setAppData(AppData seatedAppData) {
        appData = seatedAppData;
    }

    private static synchronized String getCookiesString() {
        return cookiesString;
    }

    private static synchronized void setCookieString(String seatedCookiesString) {
        cookiesString = seatedCookiesString;
    }

    private static synchronized void downloadAppData() {
        // TODO Download app data through AsyncTaskLoader.
    }

    private static synchronized void getAuthorization() {
        // TODO make authorization request and set cookies string.
    }

    @Override
    public Loader<AppData> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<AppData> loader, AppData data) {

    }

    @Override
    public void onLoaderReset(Loader<AppData> loader) {

    }
}
