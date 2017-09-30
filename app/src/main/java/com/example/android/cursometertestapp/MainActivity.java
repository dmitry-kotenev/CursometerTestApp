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
import java.util.Arrays;
import java.util.List;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<SubscribedData>,
        ViewPager.OnPageChangeListener {

    public static final String TAG_RETAINED_FRAGMENT = "RetainedFragment";
    private static final int DATA_IS_EMPTY_POS = -1;
    private static final int DATA_IS_NULL_POS = -2;
    private static final String LOG_TAG = "MainActivity";
    private static final int ASYNC_TASK_LOADER_ID = 1;

    private static String cookiesString = null;
    // https://stackoverflow.com/questions/27856709/loading-data-from-asynctask-to-fragments-using-fragmentpageradapter
    public static SubscribedData mApplicationCurrentData = null;
    private static int currentViewPagerPosition = DATA_IS_NULL_POS;

    private List<DataUpdateListener> mListeners;
    private ViewPager viewPager;
    private CurrenciesFragmentPagerAdapter pagerAdapter;
    private RelativeLayout noQuotationsSelectedView;
    private ImageView splashScreenView;
    private Toolbar appToolbar;
    private FloatingActionButton mFAB;


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        // FAB может быть спрятана на предыдущей странице, поэтому она показывается явно при
        // перелистывании на новую страницу.
        mFAB.show();

        // Аналогично показывается AppBar
//        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
//        appBarLayout.setExpanded(true, true);

//        ViewPager viewPager = (ViewPager) findViewById(R.id.currencies_viewpager);
        ActionBar actionBar =  getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mApplicationCurrentData.getCurrencyPair(position).getName());
        }
        currentViewPagerPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    // Update data in fragments through listeners:
    // https://stackoverflow.com/questions/37759734/dynamically-updating-a-fragment
    // https://medium.com/inloop/adventures-with-fragmentstatepageradapter-4f56a643f8e0
    public interface DataUpdateListener {
        void onDataUpdate();
    }

    public synchronized void registerDataUpdateListener(DataUpdateListener listener) {
        mListeners.add(listener);
    }

    public synchronized void unregisterDataUpdateListener(DataUpdateListener listener) {
        mListeners.remove(listener);
    }

    public synchronized void dataUpdated() {
        for (DataUpdateListener listener : mListeners) {
            listener.onDataUpdate();
        }
    }

    public MainActivity() {
        Log.e(LOG_TAG, "MAIN_ACTIVITI CONSTRUCTOR");
        mListeners = new ArrayList<>();
    }

    private class AuthorizationAsyncTask extends AsyncTask<String, Long, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.e(LOG_TAG, "AuthorizationAsyncTask doInBackground is executed"); // for testing

            return CursometerUtils.makeAuthorizationPostRequest(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(String tempCookiesString) {
//            Log.v(LOG_TAG, "Authorization response: " + responseBody); // for testing
//            Log.v(LOG_TAG, "Cookies: " + cookiesString); // for testing
            cookiesString = tempCookiesString;
            Bundle bundle = new Bundle();
            bundle.putString("url", "http://currency.btc-solutions.ru:8080/api/CurrencySubscription?Lang=0");
            bundle.putString("cookies", cookiesString);
            getSupportLoaderManager().initLoader(ASYNC_TASK_LOADER_ID, bundle, MainActivity.this);
        }
    }





//    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
//
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            return;
//        }
//
//        @Override
//        public void onPageSelected(int position) {
//
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//            return;
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Данные хранятся во фрагменте с тэгом TAG_RETAINED_FRAGMENT который не уничтожается при
        // перезапуске Activity. Если Activity перезапускается (например, при повороте экрана) -
        // используем существующий фрагмент, если приложение запускается заново - создаём фрагмент.
        //
        // Это временное решение! Оно не работает если система начинает освобождать память когда
        // приложение временно не используется. Система уничтожает mRetainedFragment и данные в
        // нём теряются, что ведёт к "аварийной" остановке приложения. В будущем, механизм храненрия
        // данных будет изменён.

//        FragmentManager fm = getSupportFragmentManager();
//        RetainedFragment mRetainedFragment = (RetainedFragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);
//
//        if (mRetainedFragment == null) {
//            mRetainedFragment = new RetainedFragment();
//            fm.beginTransaction().add(mRetainedFragment, TAG_RETAINED_FRAGMENT).commit();
//
//            // Как временное решение, для целей тестирования UI, используем вымышленные, подставные
//            // двнные. См. метод getExampleArrayOfCurrenciesRates().
//            mRetainedFragment.setData(getExampleArrayOfCurrenciesRates());
//        }
//
//        mApplicationCurrentData = mRetainedFragment.getData();

        //mApplicationCurrentData = new ArrayList<CurrenciesRates>();

//        if (mApplicationCurrentData == null) {
//            mApplicationCurrentData = getExampleArrayOfCurrenciesRates();
//        }


        // Устанавливаем Toolbar в качестве ActionBar.
        // https://developer.android.com/training/appbar/setting-up.html
        appToolbar = (Toolbar) findViewById(R.id.toolbar);
        appToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(appToolbar);
        mFAB = (FloatingActionButton) findViewById(R.id.fab);

        noQuotationsSelectedView = (RelativeLayout) findViewById(R.id.no_quot_selected_view);
        splashScreenView = (ImageView) findViewById(R.id.splash_screen);
        if (mApplicationCurrentData == null) {
            splashScreenView.setVisibility(View.VISIBLE);
        }
//        int AppBarHeight = customAppBar.getHeight();
//        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) noQuotationsSelectedView.getLayoutParams();
//        lp.setMargins(0, AppBarHeight, 0, 0);

//        noQuotationsSelectedView.setPadding(0, customAppBar.getHeight(), 0, 0);


        viewPager = (ViewPager) findViewById(R.id.currencies_viewpager);
        //viewPager.setSaveFromParentEnabled(false); // !!! It prevents from getting blank fragments, but fragments are created many times.
        pagerAdapter =
                new CurrenciesFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);

//        viewPager.setVisibility(View.INVISIBLE);








//        if ((mApplicationCurrentData != null) && (!mApplicationCurrentData.isEmpty())) {
//            Log.e(LOG_TAG, "Selected 0 page"); //For testing
//            pageChangeListener.onPageSelected(0);
//        }

        // Вызов метода onPageSelected для первой страницы после запуска приложения, для
        // отображения корректного заголовка в ActionBar.
        // https://stackoverflow.com/questions/16074058/onpageselected-doesnt-work-for-first-page
        //
        // Переменные  pageChangeListener и viewPager объявлены глобальными для доступа к ним из
        // метода run() ниже.
//        Runnable mRunnable = new Runnable(){
//            @Override
//            public void run()
//            {
//                pageChangeListener.onPageSelected(viewPager.getCurrentItem());
//            }
//        };

//        if ((mApplicationCurrentData != null) && (!mApplicationCurrentData.isEmpty())) {
//            viewPager.post(mRunnable);
//        }

        //https://stackoverflow.com/questions/38459309/how-do-you-create-an-android-view-pager-with-a-dots-indicator
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager, true);

        Log.e(LOG_TAG, "ONCREATE is running. Cookie string: " + cookiesString); // for testing
        if (cookiesString == null) {
            new AuthorizationAsyncTask().
                    execute("http://currency.btc-solutions.ru:8080/api/Account", "exampleid174942");
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("url", "http://currency.btc-solutions.ru:8080/api/CurrencySubscription?Lang=0");
            bundle.putString("cookies", cookiesString);
            getSupportLoaderManager().initLoader(ASYNC_TASK_LOADER_ID, bundle, MainActivity.this);
        }
    }

    public synchronized SubscribedData getApplicationCurrentData(){
        return mApplicationCurrentData;
    }

    public synchronized void refreshDataFromServer() {

        //TODO try to get data from Internet and update it.
        Bundle bundle = new Bundle();
        bundle.putString("url", "http://currency.btc-solutions.ru:8080/api/CurrencySubscription?Lang=0");
        bundle.putString("cookies", cookiesString);
        getSupportLoaderManager().restartLoader(ASYNC_TASK_LOADER_ID, bundle, MainActivity.this);



//        Snackbar noInternetConnectionMessage = Snackbar.make((CoordinatorLayout) this.findViewById(R.id.main_coordinator_layout),
//                "No Internet connection", Snackbar.LENGTH_LONG);
//        noInternetConnectionMessage.setAction("RETRY", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //TODO try to get data from Internet and update it.
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
     * Метод генерирует вымышленные данные о котировках и настройках пользрователя. Используется
     * временно.
     */
    private ArrayList<CurrenciesRates> getExampleArrayOfCurrenciesRates() {

        ExchangeRate exampleRubUsdExRate1 = new ExchangeRate(1, 65.0f, 60.0f);
        ExchangeRate exampleRubUsdExRate2 = new ExchangeRate(2500, 63.0f, 62.0f);
        ExchangeRate exampleRubUsdExRate3 = new ExchangeRate(1, 75.0f, 70.0f);
        ExchangeRate exampleRubUsdExRate4 = new ExchangeRate(1, 39.0f, 37.0f);
        ExchangeRate exampleRubEurExRate1 = new ExchangeRate(1, 70.0f, 78.0f);
        ExchangeRate exampleUsdOilExRate1 = new ExchangeRate(1, 50.0f, 52.0f);

        BankRates exampleRubUsdBankRates1 = new BankRates("Сбербанк", new ArrayList<ExchangeRate>(
                Arrays.asList(exampleRubUsdExRate1, exampleRubUsdExRate2, exampleRubUsdExRate3, exampleRubUsdExRate3, exampleRubUsdExRate3, exampleRubUsdExRate3, exampleRubUsdExRate3, exampleRubUsdExRate3)));

        BankRates exampleRubUsdBankRates2 = new BankRates("Альфа-Банк", new ArrayList<ExchangeRate>(
                Arrays.asList(exampleRubUsdExRate3)));

        BankRates exampleRubUsdBankRates3 = new BankRates("Центральный Банк", new ArrayList<ExchangeRate>(
                Arrays.asList(exampleRubUsdExRate4)));

        BankRates exampleRubEurBankRates1 = new BankRates("Сбербанк", new ArrayList<ExchangeRate>(
                Arrays.asList(exampleRubEurExRate1)));

        BankRates exampleUsdOilBankRates1 = new BankRates("Биржа", new ArrayList<ExchangeRate>(
                Arrays.asList(exampleUsdOilExRate1)));


        CurrenciesRates exampleRubUsdRates = new CurrenciesRates("Рубль / Доллар США", "RUB/USD",
                new ArrayList<BankRates>(Arrays.asList(exampleRubUsdBankRates1, exampleRubUsdBankRates2, exampleRubUsdBankRates3)));

        CurrenciesRates exampleRubEurRates = new CurrenciesRates("Рубль / Евро", "RUB/EUR",
                new ArrayList<BankRates>(Arrays.asList(exampleRubEurBankRates1)));

        CurrenciesRates exampleUsdOilRates = new CurrenciesRates("Нефть / Доллар", "Нефть / Доллар",
                new ArrayList<BankRates>(Arrays.asList(exampleUsdOilBankRates1)));

        return new ArrayList<CurrenciesRates>(Arrays.asList(exampleRubUsdRates, exampleRubEurRates, exampleRubUsdRates, exampleUsdOilRates));
    }

    @Override
    protected void onStart() {
        Log.e(LOG_TAG, "onStart is running. Cookie string: " + cookiesString); // for testing
//        if (mApplicationCurrentData == null) {
//            viewPager.getAdapter().notifyDataSetChanged();
//        }
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(LOG_TAG, "onResume is running. Cookie string: " + cookiesString); // for testing
    }

    @Override
    public AsyncTaskLoader<SubscribedData> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskRatesLoader(this, args.getString("url"), args.getString("cookies"));
        }

    @Override
    public void onLoadFinished(Loader<SubscribedData> loader, SubscribedData resultData) {
        Log.e(LOG_TAG, "onLoadFinished is running. Cookie string: " + cookiesString); // for testing

        String logString = "";
        for (int i = 0; i < resultData.size(); i++){
            logString += (resultData.getCurrencyPair(i).getFullName() + ", ");
        }
        Log.e(LOG_TAG, "Data from Loader: " + logString); // for testing

        splashScreenView.setVisibility(View.GONE);

//        if (mApplicationCurrentData == null) {
//            mApplicationCurrentData = (ArrayList<CurrenciesRates>) resultList;
//            pagerAdapter =
//                    new CurrenciesFragmentPagerAdapter(getSupportFragmentManager(), this);
//            viewPager.setAdapter(pagerAdapter);
//            pagerAdapter.notifyDataSetChanged();
//        } else {
//            mApplicationCurrentData = (ArrayList<CurrenciesRates>) resultList;
//            //mApplicationCurrentData = new ArrayList<CurrenciesRates>(); // for testing
//            //viewPager.removeAllViews();
//            pagerAdapter.notifyDataSetChanged();
//        }

        mApplicationCurrentData = resultData;
//        mApplicationCurrentData = new ArrayList<CurrenciesRates>(); // uncomment for test "no quotations is selected" screen
//        mApplicationCurrentData = getExampleArrayOfCurrenciesRates(); // uncomment for testing on known superficial data

//        pagerAdapter =
//                new CurrenciesFragmentPagerAdapter(getSupportFragmentManager(), this);
//        viewPager.setAdapter(pagerAdapter);
////            //mApplicationCurrentData = new ArrayList<CurrenciesRates>(); // for testing
////            //viewPager.removeAllViews();
        pagerAdapter.notifyDataSetChanged();
        dataUpdated();

        //int position;

        if (mApplicationCurrentData.isEmpty()) {
            noQuotationsSelectedView.setVisibility(View.VISIBLE);
            currentViewPagerPosition = DATA_IS_EMPTY_POS;
            //appToolbar.setTitle("No quotations is selected.");
            //currentViewPagerPosition = -1;
        } else {
            noQuotationsSelectedView.setVisibility(View.GONE);
            //int position =  pagerAdapter.getItemPosition(viewPager.getCurrentItem());
            if ((currentViewPagerPosition == DATA_IS_EMPTY_POS) || (currentViewPagerPosition == DATA_IS_NULL_POS)) {
                currentViewPagerPosition = 0;
            }
        }

        String title;
        switch(currentViewPagerPosition) {
            case DATA_IS_EMPTY_POS: title = "No quotations are selected.";
                break;
            default: title = mApplicationCurrentData.getCurrencyPair(currentViewPagerPosition).getName();
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(title);
        }
    }

    @Override
    public void onLoaderReset(Loader<SubscribedData> loader) {
        Log.e(LOG_TAG, "onLoader Reset is running."); // for testing
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

//        if (viewPager != null) {
//            // before screen rotation it's better to detach pagerAdapter from the ViewPager, so
//            // pagerAdapter can remove all old fragments, so they're not reused after rotation.
//            viewPager.setAdapter(null);
//        }

        super.onSaveInstanceState(outState);
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.e(LOG_TAG, "ONSTOP");
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.e(LOG_TAG, "ONDESTROY");
//    }
}