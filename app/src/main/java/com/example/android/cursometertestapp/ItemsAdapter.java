package com.example.android.cursometertestapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Adapter for quotations in each cards.
 *
 * Адаптер для списка котировок в каждой карточке.
 */

class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    private List<SubscribedData.Quotation> mQuotations;

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mMinAmountTxtView;
        private TextView mSalePriceTxtView;
        private TextView mBuyPriceTxtView;
        private ImageView mBuyRingImgView;
        private ImageView mSaleRingImgView;
        private View mDividerLine;
        private int sourceId;
        private String setNotificationsTitle;
        private Context mContext;

        ViewHolder(View oneItemView){
            super(oneItemView);
            mMinAmountTxtView = (TextView) oneItemView.findViewById(R.id.minimum_amount_text_view);
            mSalePriceTxtView = (TextView) oneItemView.findViewById(R.id.sale_price);
            mBuyPriceTxtView = (TextView) oneItemView.findViewById(R.id.purchase_price_textView);
            mBuyRingImgView = (ImageView) oneItemView.findViewById(R.id.purchase_ring_image);
            mSaleRingImgView = (ImageView) oneItemView.findViewById(R.id.sale_ring_image);
            mDividerLine = oneItemView.findViewById(R.id.divider_line);
            itemView.setOnClickListener(this);
            mContext = oneItemView.getContext();
        }

        @Override
        public void onClick(View v) {
            Log.e("ItemsAdapter", "Item is clicked. Purchase Price " + sourceId);
            Intent intent = new Intent(v.getContext(), SetNotificationsActivity.class);
            v.getContext().startActivity(intent);
        }
    }

    @Override
    public ItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View oneItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.one_rate_item, parent, false);
        return new ViewHolder(oneItemView);
    }

    @Override
    public void onBindViewHolder(ItemsAdapter.ViewHolder holder, final int position) {

        Resources res = holder.mContext.getResources();
        holder.mMinAmountTxtView.setText(
                res.getString(R.string.from, mQuotations.get(position).getFrom()));
        //holder.mMinAmountTxtView.setText("From: " + (mQuotations.get(position).getFrom()));
        int precision = mQuotations.get(position).getPrecision();
        String decimalPaceHolder;
        if (precision < 1) {
            decimalPaceHolder = "#";
        } else {
            decimalPaceHolder = "#.";
        }
        for (int i = 0; i < precision; i++) {
            decimalPaceHolder =  decimalPaceHolder + "#";
        }
        Log.v("ItemsAdapter", "Precision: " + precision + "; placeholder: " + decimalPaceHolder);
        DecimalFormat df = new DecimalFormat(decimalPaceHolder);
        Log.v("ItemsAdapter", "Formatted value: " + df.format(1234.56789123));
        holder.mSalePriceTxtView.setText(df.format(mQuotations.get(position).getSalePriceNow()));
        holder.mBuyPriceTxtView.setText(df.format(mQuotations.get(position).getBuyPriceNow()));
        holder.sourceId = mQuotations.get(position).getId();

        if (position == (getItemCount() - 1)){
            holder.mDividerLine.setVisibility(View.GONE);
        } else {
            holder.mDividerLine.setVisibility(View.VISIBLE);
        }

        float currentBuyPrice = mQuotations.get(position).getBuyPriceNow();
        float currentSalePrice = mQuotations.get(position).getSalePriceNow();
        float buyMaxMargin =  mQuotations.
                get(position).getTrigger(SubscribedData.BUY_MAX).getValue();
        float buyMinMargin =  mQuotations.
                get(position).getTrigger(SubscribedData.BUY_MIN).getValue();
        float saleMaxMargin =  mQuotations.
                get(position).getTrigger(SubscribedData.SALE_MAX).getValue();
        float saleMinMargin =  mQuotations.
                get(position).getTrigger(SubscribedData.SALE_MIN).getValue();

        if (buyMaxMargin < 0){
            if (buyMinMargin < 0) {
                holder.mBuyRingImgView.setVisibility(View.INVISIBLE);
            }
        } else if (CursometerUtils.isValueInMargin(currentBuyPrice, buyMinMargin, buyMaxMargin)) {
            holder.mBuyRingImgView.setImageResource(R.drawable.icn_notification_green);
            holder.mBuyRingImgView.setVisibility(View.VISIBLE);
        } else {
            holder.mBuyRingImgView.setImageResource(R.drawable.icn_notification_red);
            holder.mBuyRingImgView.setVisibility(View.VISIBLE);
        }

        if (saleMaxMargin < 0){
            if (saleMinMargin < 0) {
                holder.mSaleRingImgView.setVisibility(View.INVISIBLE);
            }
        } else if (CursometerUtils.isValueInMargin(currentSalePrice, saleMinMargin, saleMaxMargin)) {
            holder.mSaleRingImgView.setImageResource(R.drawable.icn_notification_green);
            holder.mSaleRingImgView.setVisibility(View.VISIBLE);
        } else {
            holder.mSaleRingImgView.setImageResource(R.drawable.icn_notification_red);
            holder.mSaleRingImgView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mQuotations.size();
    }

    public void setData(List<SubscribedData.Quotation> quotations){
        this.mQuotations = quotations;
    }
}