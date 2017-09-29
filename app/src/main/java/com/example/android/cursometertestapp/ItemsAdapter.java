package com.example.android.cursometertestapp;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    private List<CursometerData.Quotation> mQuotations;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mMinAmountTxtView;
        private TextView mSalePriceTxtView;
        private TextView mBuyPriceTxtView;
        private ImageView mBuyRingImgView;
        private ImageView mSaleRingImgView;
        private View mDividerLine;
        private int sourceId;
        private String setNotificationsTitle;

        public ViewHolder(View oneItemView){
            super(oneItemView);
            mMinAmountTxtView = (TextView) oneItemView.findViewById(R.id.minimum_amount_text_view);
            mSalePriceTxtView = (TextView) oneItemView.findViewById(R.id.sale_price);
            mBuyPriceTxtView = (TextView) oneItemView.findViewById(R.id.purchase_price_textView);
            mBuyRingImgView = (ImageView) oneItemView.findViewById(R.id.purchase_ring_image);
            mSaleRingImgView = (ImageView) oneItemView.findViewById(R.id.sale_ring_image);
            mDividerLine = oneItemView.findViewById(R.id.divider_line);
            itemView.setOnClickListener(this);
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
        holder.mMinAmountTxtView.setText("From: " + (mQuotations.get(position).getFrom()));
        holder.mSalePriceTxtView.setText(String.format("%.2f", mQuotations.get(position).
                getSalePriceNow()));
        holder.mBuyPriceTxtView.setText(String.format("%.2f", mQuotations.get(position).
                getBuyPriceNow()));
        holder.sourceId = (int) mQuotations.get(position).getId();

        if (position == (getItemCount() - 1)){
            holder.mDividerLine.setVisibility(View.GONE);
        } else {
            holder.mDividerLine.setVisibility(View.VISIBLE);
        }

        //TODO set appropriate image to the Image Views

    }

    @Override
    public int getItemCount() {
        return mQuotations.size();
    }

    public void setData(List<CursometerData.Quotation> quotations){
        this.mQuotations = quotations;
    }
}