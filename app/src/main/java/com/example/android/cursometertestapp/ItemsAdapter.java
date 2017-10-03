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
    private boolean mShowSalePrice;

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mMinAmountTxtView;
        private TextView mSalePriceTxtView;
        private TextView mBuyPriceTxtView;
        private ImageView mBuyRingImgView;
        private ImageView mSaleRingImgView;
        private View mDividerLine;
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
            Log.e("ItemsAdapter", "Item is clicked. Position: " + getAdapterPosition());
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
        DecimalFormat df = new DecimalFormat(decimalPaceHolder);

        holder.mBuyPriceTxtView.setText(df.format(mQuotations.get(position).getBuyPriceNow()));
        CursometerUtils.setupRingAppearance(holder.mBuyRingImgView,
                mQuotations.get(position).getBuyPriceNow(),
                mQuotations.get(position).getTrigger(SubscribedData.BUY_MIN).getValue(),
                mQuotations.get(position).getTrigger(SubscribedData.BUY_MAX).getValue(),
                R.drawable.icn_notification_green,
                R.drawable.icn_notification_red);

        if (mShowSalePrice) {
            holder.mSalePriceTxtView.setText(df.format(mQuotations.get(position).getSalePriceNow()));
            CursometerUtils.setupRingAppearance(holder.mSaleRingImgView,
                    mQuotations.get(position).getSalePriceNow(),
                    mQuotations.get(position).getTrigger(SubscribedData.SALE_MIN).getValue(),
                    mQuotations.get(position).getTrigger(SubscribedData.SALE_MAX).getValue(),
                    R.drawable.icn_notification_green,
                    R.drawable.icn_notification_red);
            holder.mSalePriceTxtView.setVisibility(View.VISIBLE);
        } else {
            holder.mSalePriceTxtView.setVisibility(View.INVISIBLE);
            holder.mSaleRingImgView.setVisibility(View.INVISIBLE);
        }

        if (position == (getItemCount() - 1)){
            holder.mDividerLine.setVisibility(View.GONE);
        } else {
            holder.mDividerLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mQuotations.size();
    }

    void setData(List<SubscribedData.Quotation> quotations, boolean showSalePrice){
        this.mQuotations = quotations;
        this.mShowSalePrice = showSalePrice;
    }
}