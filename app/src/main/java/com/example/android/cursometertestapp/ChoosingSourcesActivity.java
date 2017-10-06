package com.example.android.cursometertestapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

/**
 * Activity to choose sources of particular quotation rates.
 *
 * Активити для выбора источников крсов для одной котировки.
 */

public class ChoosingSourcesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing_sources);
        int quotationPosition = getIntent().getExtras().getInt("QUOTATION_POSITION");
        if (MainActivity.getApplicationData() == null) {
            NavUtils.navigateUpFromSameTask(this);
        }
        Toolbar chooseSourceToolbar = (Toolbar) findViewById(R.id.choosing_source_toolbar);
        chooseSourceToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        chooseSourceToolbar.setTitle("Temp text");
        setSupportActionBar(chooseSourceToolbar);
        ListView sourcesList = (ListView) findViewById(R.id.choosing_source_list);
        if (MainActivity.getApplicationData() != null) {
            AvailableCurrenciesData.CurrenciesPair currPair = MainActivity.getApplicationData().
                    getAvailableCurrenciesData().getCurrenciesPair(quotationPosition);
            chooseSourceToolbar.setTitle(currPair.getFullName());
            sourcesList.setAdapter(new SourcesAdapter(this, currPair, currPair.getId()));
        }
    }
}
