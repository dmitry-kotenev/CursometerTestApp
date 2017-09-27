package com.example.android.cursometertestapp;

import android.app.job.JobInfo;

import java.util.ArrayList;

/**
 * Created by d on 27.09.2017.
 */

public class CursometerData {

    public class Trigger {

        public static final int BUY_MIN = 0;
        public static final int BUY_MAX = 1;
        public static final int SELL_MIN = 2;
        public static final int SELL_MAX = 3;

        private int triggerId;
        private int triggerFireType;
        private int triggerType;
        private float value;

        public Trigger(int triggerId, int triggerFireType, int triggerType, float value) {
            this.triggerId = triggerId;
            this.triggerFireType = triggerFireType;
            this.triggerType = triggerType;
            this.value = value;
        }

        public int getTriggerId() {
            return triggerId;
        }

        public void setTriggerId(int triggerId) {
            this.triggerId = triggerId;
        }

        public int getTriggerFireType() {
            return triggerFireType;
        }

        public void setTriggerFireType(int triggerFireType) {
            this.triggerFireType = triggerFireType;
        }

        public int getTriggerType() {
            return triggerType;
        }

        public void setTriggerType(int triggerType) {
            this.triggerType = triggerType;
        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }
    }

    public class Quotation {
        private int id;
        private int from;
        private float buyPriceNow;
        private float salePriceNow;
        private String dateTime;
        private ArrayList<Trigger> triggers;
        int precision;
        boolean showSelPrice;
        int triggerFireType;

        public Quotation() {
            this.triggers = new ArrayList<Trigger>();
            for (int i = 0; i < 0; i++) {
                triggers.add(null);
            }
        }
    }
}
