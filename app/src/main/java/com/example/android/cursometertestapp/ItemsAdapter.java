package com.example.android.cursometertestapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * Адаптер для списка котировок в каждой карточке.
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    private List<ExchangeRate> mExRates;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mMinAmountTxtView;
        private TextView mSalePriceTxtView;
        private TextView mBuyPriceTxtView;
        private ImageView mBuyRingImgView;
        private ImageView mSaleRingImgView;
        private View mDividerLine;

        public ViewHolder(View oneItemView){
            super(oneItemView);
            mMinAmountTxtView = (TextView) oneItemView.findViewById(R.id.minimum_amount_text_view);
            mSalePriceTxtView = (TextView) oneItemView.findViewById(R.id.sale_price);
            mBuyPriceTxtView = (TextView) oneItemView.findViewById(R.id.purchase_price_textView);
            mBuyRingImgView = (ImageView) oneItemView.findViewById(R.id.purchase_ring_image);
            mSaleRingImgView = (ImageView) oneItemView.findViewById(R.id.sale_ring_image);
            mDividerLine = oneItemView.findViewById(R.id.divider_line);

        }
    }

    @Override
    public ItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View oneItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.one_rate_item, parent, false);
        return new ViewHolder(oneItemView);
    }

    @Override
    public void onBindViewHolder(ItemsAdapter.ViewHolder holder, int position) {
        holder.mMinAmountTxtView.setText("From: " + (mExRates.get(position).getMinimumAmount()));
        holder.mSalePriceTxtView.setText(String.format("%.2f", mExRates.get(position).
                getSalePrice()));
        holder.mBuyPriceTxtView.setText(String.format("%.2f", mExRates.get(position).
                getBuyPrice()));

        if (position == (getItemCount() - 1)){
            holder.mDividerLine.setVisibility(View.GONE);
        } else {
            holder.mDividerLine.setVisibility(View.VISIBLE);
        }

        //TODO set appropriate image to the Image Views

    }

    @Override
    public int getItemCount() {
        return mExRates.size();
    }

    public void setData(List<ExchangeRate> mExRates){
        this.mExRates = mExRates;
    }
}