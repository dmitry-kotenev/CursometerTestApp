package com.example.android.cursometertestapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Adapter for sources list in ChoosingSourcesActivity.
 *
 * Адаптер для списка источников (банков) в ChoosingSourcesActivity.
 */

class SourcesAdapter extends ArrayAdapter<AvailableCurrenciesData.Bank> {

    private static final String CURRENCY_SUBSCRIPTION_API_ENDPOINT =
            "http://currency.btc-solutions.ru:8080/api/CurrencySubscription";

    private int quotationId;

    private class SubscribeToSource extends AsyncTask<Integer, Long, Boolean> {

        private Switch sourceSwitch;

        SubscribeToSource(Switch sourceSwitch) {
            this.sourceSwitch = sourceSwitch;
        }

        @Override
        protected Boolean doInBackground(Integer... params) {

            // params[0] - currenciesPair ID;
            // params[1] - Bank ID;

            boolean result = false;

            JSONObject requestResult =
                    CursometerUtils.makePostRequest(CURRENCY_SUBSCRIPTION_API_ENDPOINT,
                    MainActivity.getCookiesString(),
                    "{\"categoryId\":" + params[0] + ",\"sourceId\":" + params[1] + "}");
            if (requestResult != null) {
                try {
                    result = requestResult.getBoolean("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //super.onPostExecute(aBoolean);
            sourceSwitch.setClickable(true);
            if (aBoolean) {
                //
            }

        }
    }

    SourcesAdapter(@NonNull Context context,
                   @NonNull List<AvailableCurrenciesData.Bank> banks,
                   int quotationId) {
        super(context, 0, banks);
        this.quotationId = quotationId;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View oneSourceItemView = convertView;
        if (oneSourceItemView == null){
            oneSourceItemView = LayoutInflater.from(getContext()).
                    inflate(R.layout.one_source_item, parent, false);
        }

        final AvailableCurrenciesData.Bank sourceBank = getItem(position);
        Switch sourceSwitch =
                ((Switch) oneSourceItemView.findViewById(R.id.one_source_item_switch));

        if (sourceBank != null) {
            ((TextView) oneSourceItemView.findViewById(R.id.one_source_item_bank_name_txt)).
                    setText(sourceBank.getName());
            sourceSwitch.setChecked(sourceBank.isSubscribed());
        }

        sourceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    buttonView.setClickable(false);
                    if (sourceBank != null) {
                        new SubscribeToSource((Switch) buttonView).execute(quotationId,
                                sourceBank.getId());
                    }
                }
            }
        });

        return oneSourceItemView;
    }


}
