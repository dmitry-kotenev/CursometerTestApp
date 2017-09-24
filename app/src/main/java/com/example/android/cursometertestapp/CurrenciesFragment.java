package com.example.android.cursometertestapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.widget.SwipeRefreshLayout;
import java.util.ArrayList;

/**
 * Класс для фрагментов, для отображения всех обменных курсов для одной пары валют по всем банкам.
 */

public class CurrenciesFragment extends android.support.v4.app.Fragment {

    private int position;
    private MainActivity mMainActivityInstance;

    private RecyclerView listOfCards;

    public CurrenciesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt("Position");
        }
        Log.e("CurrenciesFragment", "Fragment is created, position: " + position);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e("CurrenciesFragment", "Setting data to fragment, position: " + position);

        mMainActivityInstance = (MainActivity) getActivity();
        CardsAdapter mCardsAdapter;
        if (mMainActivityInstance.getApplicationCurrentData() != null) {
            mCardsAdapter = new CardsAdapter(mMainActivityInstance.getApplicationCurrentData().get(position).getBankRates());
        }   else {
            mCardsAdapter = new CardsAdapter(new ArrayList<BankRates>());
        }

        listOfCards.setAdapter(mCardsAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("Position", position);
        super.onSaveInstanceState(outState);
    }

    public void setParams(int pos){
        position = pos;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SwipeRefreshLayout resultView = (SwipeRefreshLayout) inflater.inflate(R.layout.currencies_rates_list, container, false);
        listOfCards = (RecyclerView) resultView.findViewById(R.id.list_of_ex_rates);
        listOfCards.setLayoutManager(new LinearLayoutManager(CurrenciesFragment.this.getContext()));
        listOfCards.setNestedScrollingEnabled(false);  //строка необходима для плавной перемотки списка.
        resultView.setOnRefreshListener(new RefreshData(mMainActivityInstance, resultView));
        return resultView;
    }
}