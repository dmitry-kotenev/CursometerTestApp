package com.example.android.cursometertestapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;

/**
 * Set notifications activity class.
 */

public class SetNotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_notifications);
        if (MainActivity.getApplicationData() == null) {
            NavUtils.navigateUpFromSameTask(this);
        }
    }
}