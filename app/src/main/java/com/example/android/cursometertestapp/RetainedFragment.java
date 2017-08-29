package com.example.android.cursometertestapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;

/**
 * Класс для храннеия данных приложения при изменении конфигурации (например, при вращении экрана).
 *
 * https://developer.android.com/guide/topics/resources/runtime-changes.html
 */

public class RetainedFragment extends Fragment {

    private ArrayList<CurrenciesRates> data;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public void setData(ArrayList<CurrenciesRates> data) {
        this.data = data;
    }

    public ArrayList<CurrenciesRates> getData() {
        Log.v("RetainedFragment", "Data: " + data);
        return data;
    }
}