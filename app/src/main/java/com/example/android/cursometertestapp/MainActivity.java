package com.example.android.cursometertestapp;

import android.os.AsyncTask;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<SubscribedData>,
        ViewPager.OnPageChangeListener {

    private static final int DATA_IS_EMPTY_POS = -1; // for currentViewPagerPosition variable.
    private static final int DATA_IS_NULL_POS = -2; // for currentViewPagerPosition variable.
    private static final String LOG_TAG = "MainActivity";
    private static final int ASYNC_TASK_LOADER_ID = 1;
    private static final String CURRENCY_SUBSCRIPTION_REQUEST_API_ENDPOINT =
            "http://currency.btc-solutions.ru:8080/api/CurrencySubscription?Lang=0";
    private static final String AUTHORIZATION_REQUEST_API_ENDPOINT =
            "http://currency.btc-solutions.ru:8080/api/Account";

    private static String cookiesString = null;
    // https://stackoverflow.com/questions/27856709/loading-data-from-asynctask-to-fragments-using-fragmentpageradapter
    public static SubscribedData mApplicationCurrentData = null;
    private static int currentViewPagerPosition = DATA_IS_NULL_POS;

    // Container to keep track of fragments that need update, when data is changed:
    private List<DataUpdateListener> mListeners;
    private CurrenciesFragmentPagerAdapter pagerAdapter;
    private RelativeLayout noQuotationsSelectedView;
    private ImageView splashScreenView;
    private FloatingActionButton mFAB;

    /**
     * Class to make async request to the server to get cookie string to serve following requests.
     */
    private class AuthorizationAsyncTask extends AsyncTask<String, Long, String> {

        /**
         *
         * @param params - param[0] - API url for authorization POST request; param[0] - unique
         *               user ID.
         * @return String - cookies in form of one string.
         */
        @Override
        protected String doInBackground(String... params) {
            return CursometerUtils.makeAuthorizationPostRequest(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(String tempCookiesString) {
            cookiesString = tempCookiesString;
            Bundle bundle = new Bundle();
            bundle.putString("url", CURRENCY_SUBSCRIPTION_REQUEST_API_ENDPOINT);
            bundle.putString("cookies", cookiesString);
            getSupportLoaderManager().initLoader(ASYNC_TASK_LOADER_ID, bundle, MainActivity.this);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // Necessary to implement ViewPager.OnPageChangeListener.
    }

    @Override
    public void onPageSelected(int position) {
        // Necessary to implement ViewPager.OnPageChangeListener.

        // Explicitly show FAB after changing page because it could be hidden in previous page.
        mFAB.show();

        ActionBar actionBar =  getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mApplicationCurrentData.getCurrencyPair(position).getName());
        }

        // Keep track of current position in view pager to set appropriate title in onLoadFinished
        // method when data is changed.
        currentViewPagerPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // Necessary to implement ViewPager.OnPageChangeListener.
    }

    // Update data in fragments through listeners:
    // https://stackoverflow.com/questions/37759734/dynamically-updating-a-fragment
    // https://medium.com/inloop/adventures-with-fragmentstatepageradapter-4f56a643f8e0

    /**
     * All fragments need to implement this class in case of they need to get data from
     * MainActivity.
     */
    interface DataUpdateListener {
        void onDataUpdate();
    }

    /**
     * @param listener - fragment, that will be updated.
     */
    public synchronized void registerDataUpdateListener(DataUpdateListener listener) {
        mListeners.add(listener);
    }

    /**
     * @param listener - fragment, that will be updated.
     */
    public synchronized void unregisterDataUpdateListener(DataUpdateListener listener) {
        mListeners.remove(listener);
    }

    public synchronized void dataUpdated() {
        for (DataUpdateListener listener : mListeners) {
            listener.onDataUpdate();
        }
    }

    public MainActivity() {
        mListeners = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set Toolbar as ActionBar.
        // https://developer.android.com/training/appbar/setting-up.html
        Toolbar appToolbar = (Toolbar) findViewById(R.id.toolbar);
        appToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(appToolbar);
        mFAB = (FloatingActionButton) findViewById(R.id.fab);

        noQuotationsSelectedView = (RelativeLayout) findViewById(R.id.no_quot_selected_view);
        splashScreenView = (ImageView) findViewById(R.id.splash_screen);
        if (mApplicationCurrentData == null) {
            splashScreenView.setVisibility(View.VISIBLE);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.currencies_viewpager);
        pagerAdapter =
                new CurrenciesFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);

        //https://stackoverflow.com/questions/38459309/how-do-you-create-an-android-view-pager-with-a-dots-indicator
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager, true);

        if (cookiesString == null) {
            new AuthorizationAsyncTask().
                    execute(AUTHORIZATION_REQUEST_API_ENDPOINT, "exampleid174942"); // Explicit user ID is temporarily here.
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("url", CURRENCY_SUBSCRIPTION_REQUEST_API_ENDPOINT);
            bundle.putString("cookies", cookiesString);
            getSupportLoaderManager().initLoader(ASYNC_TASK_LOADER_ID, bundle, MainActivity.this);
        }
    }

    public synchronized SubscribedData getApplicationCurrentData(){
        return mApplicationCurrentData;
    }

    public synchronized void refreshDataFromServer() {
        Bundle bundle = new Bundle();
        bundle.putString("url", CURRENCY_SUBSCRIPTION_REQUEST_API_ENDPOINT);
        bundle.putString("cookies", cookiesString);
        getSupportLoaderManager().restartLoader(ASYNC_TASK_LOADER_ID, bundle, MainActivity.this);



//        Snackbar noInternetConnectionMessage = Snackbar.make((CoordinatorLayout) this.findViewById(R.id.main_coordinator_layout),
//                "No Internet connection", Snackbar.LENGTH_LONG);
//        noInternetConnectionMessage.setAction("RETRY", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //TODO Add check Internet connection functionality.
//
//                // https://stackoverflow.com/questions/24587925/swiperefreshlayout-trigger-programmatically
//
//                return;
//            }
//        });
//
//        noInternetConnectionMessage.show();
    }

    /**
     *
     * @param id - loader ID.
     * @param args - contains API url and cookies string.
     * @return - AsyncTaskLoader
     */
    @Override
    public AsyncTaskLoader<SubscribedData> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskAppDataLoader(this, args.getString("url"), args.getString("cookies"));
        }

    @Override
    public void onLoadFinished(Loader<SubscribedData> loader, SubscribedData resultData) {
        splashScreenView.setVisibility(View.GONE);
        mApplicationCurrentData = resultData;
        pagerAdapter.notifyDataSetChanged();
        dataUpdated();

        if (mApplicationCurrentData.isEmpty()) {
            noQuotationsSelectedView.setVisibility(View.VISIBLE);
            currentViewPagerPosition = DATA_IS_EMPTY_POS;
        } else {
            noQuotationsSelectedView.setVisibility(View.GONE);
            if ((currentViewPagerPosition == DATA_IS_EMPTY_POS) ||
                    (currentViewPagerPosition == DATA_IS_NULL_POS)) {
                currentViewPagerPosition = 0;
            }
        }

        String title;
        switch(currentViewPagerPosition) {
            case DATA_IS_EMPTY_POS: title = "No quotations are selected.";
                break;
            default: title = mApplicationCurrentData.getCurrencyPair(currentViewPagerPosition).
                    getName();
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(title);
        }
    }

    @Override
    public void onLoaderReset(Loader<SubscribedData> loader) {
        // Necessary to implement LoaderManager.LoaderCallbacks.
    }
}