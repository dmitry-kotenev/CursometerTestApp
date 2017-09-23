package com.example.android.cursometertestapp;

import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<CurrenciesRates>> {

    public static final String TAG_RETAINED_FRAGMENT = "RetainedFragment";
    private static final String LOG_TAG = "MainActivity";
    private static final int ASYNC_TASK_LOADER_ID = 1;

    private static String cookiesString = null;
    // https://stackoverflow.com/questions/27856709/loading-data-from-asynctask-to-fragments-using-fragmentpageradapter
    public static ArrayList<CurrenciesRates> mApplicationCurrentData = null;

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


    // pageChangeListener и viewPager - глобальные переменные для доступа к ним из внутреннего
    // класса (см. объект Runnable mRunnable в методе onCreate).

    private ViewPager viewPager;
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            return;
        }

        @Override
        public void onPageSelected(int position) {
            // FAB может быть спрятана на предыдущей странице, поэтому она показывается явно при
            // перелистывании на новую страницу.
            FloatingActionButton mFAB = (FloatingActionButton) findViewById(R.id.fab);
            mFAB.show();

            // Аналогично показывается AppBar
            AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
            appBarLayout.setExpanded(true, true);

            ViewPager viewPager = (ViewPager) findViewById(R.id.currencies_viewpager);
            String title =
                    ((CurrenciesFragmentPagerAdapter) viewPager.getAdapter()).
                            getFragmentShortTitle(position);
            Toolbar appToolBar = (Toolbar) findViewById(R.id.toolbar);
            appToolBar.setTitle(title);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            return;
        }
    };

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

        mApplicationCurrentData = new ArrayList<CurrenciesRates>();

//        if (mApplicationCurrentData == null) {
//            mApplicationCurrentData = getExampleArrayOfCurrenciesRates();
//        }

        // Устанавливаем Toolbar в качестве ActionBar.
        // https://developer.android.com/training/appbar/setting-up.html
        Toolbar customAppBar = (Toolbar) findViewById(R.id.toolbar);
        customAppBar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(customAppBar);


        viewPager = (ViewPager) findViewById(R.id.currencies_viewpager);

        viewPager.addOnPageChangeListener(pageChangeListener);

        // Вызов метода onPageSelected для первой страницы после запуска приложения, для
        // отображения корректного заголовка в ActionBar.
        // https://stackoverflow.com/questions/16074058/onpageselected-doesnt-work-for-first-page
        //
        // Переменные  pageChangeListener и viewPager объявлены глобальными для доступа к ним из
        // метода run() ниже.
        Runnable mRunnable = new Runnable(){
            @Override
            public void run()
            {
                pageChangeListener.onPageSelected(viewPager.getCurrentItem());
            }
        };

        if (!mApplicationCurrentData.isEmpty()) {
            viewPager.post(mRunnable);
        }

        //https://stackoverflow.com/questions/38459309/how-do-you-create-an-android-view-pager-with-a-dots-indicator
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager, true);

        CurrenciesFragmentPagerAdapter adapter =
                new CurrenciesFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
        if (!mApplicationCurrentData.isEmpty()) {
            pageChangeListener.onPageSelected(0);
        }

        Log.e(LOG_TAG, "onCreate is running. Cookie string: " + cookiesString); // for testing
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

    public ArrayList<CurrenciesRates> getApplicationCurrentData(){
        return mApplicationCurrentData;
    }

    public void refreshDataFromServer() {

        //TODO try to get data from Internet and update it.

        // На данный момент, обновления данных не происходит. Показывается Snackbar с сообщением о
        // недоступности интернет-соединения.

        Snackbar noInternetConnectionMessage = Snackbar.make((CoordinatorLayout) this.findViewById(R.id.main_coordinator_layout),
                "No Internet connection", Snackbar.LENGTH_LONG);
        noInternetConnectionMessage.setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO try to get data from Internet and update it.

                // https://stackoverflow.com/questions/24587925/swiperefreshlayout-trigger-programmatically

                return;
            }
        });

        noInternetConnectionMessage.show();
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
        super.onStart();
        Log.e(LOG_TAG, "onStart is running. Cookie string: " + cookiesString); // for testing
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(LOG_TAG, "onResume is running. Cookie string: " + cookiesString); // for testing
    }

    @Override
    public AsyncTaskLoader<List<CurrenciesRates>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskRatesLoader(this, args.getString("url"), args.getString("cookies"));
        }

    @Override
    public void onLoadFinished(Loader<List<CurrenciesRates>> loader, List<CurrenciesRates> resultList) {
        Log.e(LOG_TAG, "onLoadFinished is running. Cookie string: " + cookiesString); // for testing

        String logString = "";
        for (int i = 0; i < resultList.size(); i++){
            logString += (resultList.get(i).getCurrenciesShortName() + ", ");
        }
        Log.e(LOG_TAG, "Data from Loader: " + logString); // for testing

        mApplicationCurrentData = (ArrayList<CurrenciesRates>) resultList;
        viewPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<CurrenciesRates>> loader) {
        Log.e(LOG_TAG, "onLoader Reset is running."); // for testing
    }
}