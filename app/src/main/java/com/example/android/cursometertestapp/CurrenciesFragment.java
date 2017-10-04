package com.example.android.cursometertestapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.SwipeRefreshLayout;
import java.util.ArrayList;

/**
 * Класс для фрагментов, для отображения всех обменных курсов для одной пары валют по всем банкам.
 *
 * Class for fragment that reflects all quotations for one currency pair for all banks.
 */
public class CurrenciesFragment extends android.support.v4.app.Fragment implements MainActivity.DataUpdateListener {

    private int positionInViewPager;
    private RecyclerView listOfCards;

    public CurrenciesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            positionInViewPager = savedInstanceState.getInt("Position");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onDataUpdate();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("Position", positionInViewPager);
        super.onSaveInstanceState(outState);
    }

    public void setParams(int pos){
        positionInViewPager = pos;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SwipeRefreshLayout resultView = (SwipeRefreshLayout) inflater.inflate(R.layout.currencies_rates_list, container, false);
        listOfCards = (RecyclerView) resultView.findViewById(R.id.list_of_ex_rates);
        listOfCards.setLayoutManager(new LinearLayoutManager(CurrenciesFragment.this.getContext()));
        // Строка необходима для плавной перемотки списка.
        // Following line is necessary for smooth scrolling of the RecyclerView.
        listOfCards.setNestedScrollingEnabled(false);
        resultView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((MainActivity) getActivity()).getDataFromServer();
                SwipeRefreshLayout view = (SwipeRefreshLayout) getView();
                if (view != null) {
                    view.setRefreshing(false);
                }
            }
        });
        return resultView;
    }

    @Override
    public void onDataUpdate() {
        AppData appData = MainActivity.getApplicationData();
        CardsAdapter mCardsAdapter;
        if (appData != null) { // Need to check getSubscribedData() too.
                               // Think about changing AppData class to simplify emptyData check,
                               // when it is needed.
//            if (appData.getSubscribedData() != null) {
                mCardsAdapter = new CardsAdapter(appData.getSubscribedData()
                        .getCurrencyPair(positionInViewPager).getBanks());
//            }
        } else {
            mCardsAdapter = new CardsAdapter(new ArrayList<SubscribedData.Bank>());
        }
        listOfCards.setAdapter(mCardsAdapter);
        SwipeRefreshLayout fragmentView = (SwipeRefreshLayout) getView();
        if (fragmentView != null) {
            fragmentView.setRefreshing(false);
        }
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        ((MainActivity) activity).registerDataUpdateListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) getActivity()).unregisterDataUpdateListener(this);
    }
}