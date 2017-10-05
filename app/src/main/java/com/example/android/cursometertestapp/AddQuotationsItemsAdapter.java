package com.example.android.cursometertestapp;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Adapter for list of available quotations.
 *
 * Адаптер для списка доступных кортировок.
 */

class AddQuotationsItemsAdapter
        extends RecyclerView.Adapter<AddQuotationsItemsAdapter.ViewHolder> {

    private AvailableCurrenciesData appAvailCurrData;

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mQuotationFullNameTxt;
        TextView mBankListStringTxt;
        ImageView mIsSubscribedImg;

        ViewHolder(View itemView) {
            super(itemView);
            mQuotationFullNameTxt = (TextView) itemView.findViewById(R.id.quot_name);
            mBankListStringTxt = (TextView) itemView.findViewById(R.id.banks_list_string_txt);
            mIsSubscribedImg = (ImageView) itemView.findViewById(R.id.is_subscribed_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.v("AddQuotItemsAdapter", "Item is clicked, position: " + getAdapterPosition());
        }
    }

    AddQuotationsItemsAdapter(AvailableCurrenciesData appAvailCurrData) {
        this.appAvailCurrData = appAvailCurrData;
    }

    @Override
    public AddQuotationsItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View oneItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_quot_one_item, parent, false);
        return new ViewHolder(oneItemView);
    }

    @Override
    public void onBindViewHolder(AddQuotationsItemsAdapter.ViewHolder holder, int position) {
        AvailableCurrenciesData.CurrenciesPair currPair =
                appAvailCurrData.getCurrenciesPair(position);

        holder.mQuotationFullNameTxt.setText(currPair.getFullName());

        boolean isAnyBankSubscribed = false;
        ArrayList<String> subscribedBankNamesArray = new ArrayList<>();

        for (int i = 0; i < currPair.size(); i++) {
            AvailableCurrenciesData.Bank bank = currPair.getBank(i);
            if (bank.isSubscribed()) {
                subscribedBankNamesArray.add(bank.getName());
                isAnyBankSubscribed = true;
            }
        }

        if (isAnyBankSubscribed) {
            String BankNamesString = TextUtils.join(", ", subscribedBankNamesArray);
            holder.mBankListStringTxt.setText(BankNamesString);
            holder.mBankListStringTxt.setVisibility(View.VISIBLE);
            holder.mIsSubscribedImg.setVisibility(View.VISIBLE);
        } else {
            holder.mBankListStringTxt.setVisibility(View.GONE);
            holder.mIsSubscribedImg.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return appAvailCurrData.size();
    }
}