package com.example.android.cursometertestapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

/**
 * Activity shows list of all available quotations. Subscribed quotations are marked.
 *
 * Активити отображает все доступные котировки. Котировки, на которые подписан пользователь
 * отмечены.
 */

public class AddQuotationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MainActivity.getApplicationData() == null) {
            NavUtils.navigateUpFromSameTask(this);
        }

        setContentView(R.layout.activity_add_quotations);

        Toolbar addQuotToolbar = (Toolbar) findViewById(R.id.add_quot_toolbar);
        addQuotToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        addQuotToolbar.setTitle("Add quotations");
        setSupportActionBar(addQuotToolbar);

        RecyclerView addQuotRecyclerView = (RecyclerView) findViewById(R.id.add_quot_recycler_view);
        addQuotRecyclerView.setNestedScrollingEnabled(false);
        addQuotRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (MainActivity.getApplicationData() != null) {
            AddQuotationsItemsAdapter adapter = new AddQuotationsItemsAdapter(MainActivity.getApplicationData().getAvailableCurrenciesData());
            addQuotRecyclerView.setAdapter(adapter);
        }
    }
}