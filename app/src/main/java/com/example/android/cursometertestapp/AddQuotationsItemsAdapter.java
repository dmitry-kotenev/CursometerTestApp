package com.example.android.cursometertestapp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Adapter for list of available quotations.
 *
 * Адаптер для списка доступных кортировок.
 */

class AddQuotationsItemsAdapter
        extends RecyclerView.Adapter<AddQuotationsItemsAdapter.ViewHolder> {

    private AvailableCurrenciesData appAvailCurrData;

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mQuotationFullName;

        ViewHolder(View itemView) {
            super(itemView);
            mQuotationFullName = (TextView) itemView.findViewById(R.id.quot_name);
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
        holder.mQuotationFullName.setText(appAvailCurrData.
                getCurrenciesPair(position).getFullName());
    }

    @Override
    public int getItemCount() {
        return appAvailCurrData.size();
    }
}