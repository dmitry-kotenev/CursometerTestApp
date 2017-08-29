package com.example.android.cursometertestapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
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

    public CurrenciesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt("Position");
        }
        mMainActivityInstance = (MainActivity) getActivity();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("Position", position);
        super.onSaveInstanceState(outState);
    }

    public void setParams(int pos){
        position = pos;
    }

    /**
     * Созданеи каоточки для одного банка.
     * @param bankRates - курсы для одной пары валют для одного банка.
     * @param inflater
     * @param container
     * @return - Вид CardView - каоточка с курсами для одного банка.
     */
    private CardView makeBankCard(BankRates bankRates, LayoutInflater inflater, @Nullable ViewGroup container) {

        // ! на данный момент не реализована логика отображения картинок-колокольчиков. Постоянно
        // отображаются красная и зелёная картинка, независимо от настроек покупки и продажи.
        //
        // Т. к. механизм храеннеия данных изменится, считаю, с этим вопросом можно разобраться
        // после привязки реальных данных через API.

        CardView resultCardView = (CardView) inflater.inflate(R.layout.card_list, container, false);
        LinearLayout cardViewLayout = (LinearLayout) resultCardView.findViewById(R.id.card_view_layout);
        ((TextView) cardViewLayout.findViewById(R.id.bank_name)).setText(bankRates.getBankName());
        ((TextView) cardViewLayout.findViewById(R.id.last_update_date_time)).setText(bankRates.getLastUpdateDateAndTime());

        ArrayList<ExchangeRate> exRates = bankRates.getExRates();
        for (int i =0; i < exRates.size(); i++) {
            ExchangeRate rate = exRates.get(i);
            LinearLayout oneRateView = (LinearLayout) inflater.inflate(R.layout.one_rate_item, container, false);
            String temporaryString = String.format("From: %d", rate.getMinimumAmount());
            ((TextView) oneRateView.findViewById(R.id.minimum_amount_text_view)).setText(temporaryString);
            temporaryString = String.format("%.2f", rate.getBuyPrice());
            ((TextView) oneRateView.findViewById(R.id.purchase_price_textView)).setText(temporaryString);
            temporaryString = String.format("%.2f", rate.getSalePrice());
            ((TextView) oneRateView.findViewById(R.id.sale_price)).setText(temporaryString);
            cardViewLayout.addView(oneRateView);

            if ((i + 1) < exRates.size()) {
                //Линия-разделитель между двумя курсами в одной карточке.
                cardViewLayout.addView(inflater.inflate(R.layout.divider_line, container, false));
            }
        }
        return resultCardView;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SwipeRefreshLayout resultView = (SwipeRefreshLayout) inflater.inflate(R.layout.currencies_rates_list, container, false);
        LinearLayout listOfCards = (LinearLayout) resultView.findViewById(R.id.list_of_ex_rates);

//        // for testing
//        Log.v("CurrenciesFragment", "position: " + position);
//        Log.v("CurrenciesFragment", "MainActivityInstance: " + mMainActivityInstance);
//        FragmentManager fm = getFragmentManager();
//        RetainedFragment fragment = (RetainedFragment) fm.findFragmentByTag(MainActivity.TAG_RETAINED_FRAGMENT);
//        Log.v("CurrenciesFragment", "First currencies: " + fragment.getData().get(0).getCurrenciesName());


        int numberOfBanks = mMainActivityInstance.getApplicationCurrentData().get(position).getBankRates().size();
        for (int i = 0; i < numberOfBanks; i++) {
            BankRates bankRates = mMainActivityInstance.getApplicationCurrentData().get(position).getBankRates().get(i);
            listOfCards.addView(makeBankCard(bankRates, inflater, container));
        }

        resultView.setOnRefreshListener(new RefreshData(mMainActivityInstance, resultView));

        return resultView;
    }
}