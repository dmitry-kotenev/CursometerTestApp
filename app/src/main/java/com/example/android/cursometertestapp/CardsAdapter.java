package com.example.android.cursometertestapp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Class for adapter for list of cards.
 */

class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {
    private ArrayList<SubscribedData.Bank> mListOfBanks;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mBankNameTextView;
        TextView mDateAndTimeTextView;
        ItemsAdapter mItemsAdapter;

        ViewHolder(CardView oneCard) {
            super(oneCard);
            mBankNameTextView = (TextView) oneCard.findViewById(R.id.bank_name);
            mDateAndTimeTextView = (TextView) oneCard.findViewById(R.id.last_update_date_time);
            RecyclerView cardRecyclerView = (RecyclerView) oneCard.
                    findViewById(R.id.items_in_card_list);

            // The following line is necessary for right behaviour of FAB and AppBar when the user
            // scroll the recycler view tapping on card.
            // https://stackoverflow.com/questions/33050907/nested-recyclerview-issue-with-appbarlayout
            cardRecyclerView.setNestedScrollingEnabled(false);

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(oneCard.getContext());
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            cardRecyclerView.setLayoutManager(mLayoutManager);

            cardRecyclerView.setHasFixedSize(true); // Maybe this line leads to better performance.

            mItemsAdapter = new ItemsAdapter();
            cardRecyclerView.setAdapter(mItemsAdapter);
        }
    }

    /**
     * Все ViewHolder'ы делим на типы в зависимости от количества строк в карточке. Тем самым, при
     * повторном использовании ViewHolder'а в нём не будет изменяться количество строк,
     * соответственно, не будет вызываться метод Inflate
     *
     * All ViewHolders are divided to different types in accordance of the number of rows that they
     * have. That leads to not changing the number of rows of a ViewHolder while reusing it.
     * Therefore operation of inflate and finding views by ID are don't invokes while reusing
     * ViewHolders.
     *
     * @param position - position in a RecyclerView.
     * @return - number of exchange rates from one bank that is also considered as a ViewType.
     */
    @Override
    public int getItemViewType(int position) {
        return mListOfBanks.get(position).getQuotations().size();
    }

    CardsAdapter(ArrayList<SubscribedData.Bank> data) {
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
        // Set date and time from the first quotation as it is temporarily.
        // In the data structure that is received from API, each quotation record has it's own
        // update date and time. I define the way how to choose from them later.
        // From of representation of date and time is needed to be updated too.
        holder.mDateAndTimeTextView.setText(mListOfBanks.get(position).getQuotation(0).
                getDateTime());
        SubscribedData.Bank bank = mListOfBanks.get(position);
        holder.mItemsAdapter.setData(bank.getQuotations(), bank.isShowSellPrice());
    }

    @Override
    public int getItemCount() {
        return mListOfBanks.size();
    }
}