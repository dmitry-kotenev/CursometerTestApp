package com.example.android.cursometertestapp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Адаптер для списка карточек.
 */

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {
    private ArrayList<SubscribedData.Bank> mListOfBanks;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mBankNameTextView;
        public TextView mDateAndTimeTextView;
        public ItemsAdapter mItemsAdapter;

        public ViewHolder(CardView oneCard) {
            super(oneCard);
            mBankNameTextView = (TextView) oneCard.findViewById(R.id.bank_name);
            mDateAndTimeTextView = (TextView) oneCard.findViewById(R.id.last_update_date_time);

            RecyclerView cardRecyclerView = (RecyclerView) oneCard.
                    findViewById(R.id.items_in_card_list);

            // Строка ниже необходима для правильной работы FAB и AppBar когда пользователь
            // перематывает RecyclerView нажимая на карточку.
            //
            // https://stackoverflow.com/questions/33050907/nested-recyclerview-issue-with-appbarlayout
            cardRecyclerView.setNestedScrollingEnabled(false);

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(oneCard.getContext());
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            cardRecyclerView.setLayoutManager(mLayoutManager);

            cardRecyclerView.setHasFixedSize(true); // Возможно, улучшает производительность.

            mItemsAdapter = new ItemsAdapter();
            cardRecyclerView.setAdapter(mItemsAdapter);
        }
    }

    /**
     * Все ViewHolder'ы делим на типы в зависимости от количества строк в карточке. Тем самым, при
     * повторном использовании ViewHolder'а в нём не будет изменяться количество строк,
     * соответственно, не будет вызываться метод Inflate
     *
     * @param position
     * @return - number of exchange rates from one bank that is also considered as a ViewType.
     */
    @Override
    public int getItemViewType(int position) {
        return mListOfBanks.get(position).getQuotations().size();
    }

    public CardsAdapter(ArrayList<SubscribedData.Bank> data) {
        mListOfBanks = data;
    }

    @Override
    public CardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView oneCard = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_list, parent, false);
        return new ViewHolder(oneCard);
    }

    @Override
    public void onBindViewHolder(CardsAdapter.ViewHolder holder, int position) {
        holder.mBankNameTextView.setText(mListOfBanks.get(position).getName());
        holder.mDateAndTimeTextView.setText(mListOfBanks.get(position).getQuotation(0).getDateTime()); //set date and time for first quotation temporarily

        holder.mItemsAdapter.setData(mListOfBanks.get(position).getQuotations());
    }

    @Override
    public int getItemCount() {
        return mListOfBanks.size();
    }
}