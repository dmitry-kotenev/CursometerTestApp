package com.example.android.cursometertestapp;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;

/**
 * Listener для обновления данных при swipe.
 */

public class RefreshData implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout view;

    private MainActivity mMainActivityInstance;

    RefreshData(MainActivity mainActivityInstance, SwipeRefreshLayout v) {
        mMainActivityInstance = mainActivityInstance;

        view = v;
    }

    @Override
    public void onRefresh() {
        mMainActivityInstance.refreshDataFromServer();
        view.setRefreshing(false);
    }
}