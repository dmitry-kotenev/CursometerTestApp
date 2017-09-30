package com.example.android.cursometertestapp;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Вспомогательные функции
 */

public final class CursometerUtils {

    private static String LOG_TAG = "CutsometerUtils";
    private static String COOKIES_HEADER = "Set-Cookie";

    /**
     * Возвращает объект URL.
     * @param stringUrl - url в виде строки.
     * @return - объект URL в случае успеха, или null.
     */
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error during creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Возвращает cookies в виде строки.
     * @param urlConnection
     * @return - cookies, или null в случае если в urlConnection нет cookies.
     */
    public static String getCookiesString(HttpURLConnection urlConnection) {
        Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
        List<String> cookiesList = headerFields.get(COOKIES_HEADER);
        if (cookiesList == null){
            return null;
        }
        return TextUtils.join(";", cookiesList);
    }

    public static HttpURLConnection createConnection(URL url, String requestType, @Nullable String cookies) {

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(requestType);
            urlConnection.setReadTimeout(15000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.addRequestProperty("Accept", "application/json");
            urlConnection.addRequestProperty("Content-Type", "application/json");

            urlConnection.setDoInput(true);
            if (requestType == "POST") {
                urlConnection.setDoOutput(true);
            }
            else {
                urlConnection.setDoOutput(false);
            }

            if (cookies != null) {
                urlConnection.setRequestProperty("Cookie", cookies);
            }
            urlConnection.connect();
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Error in creating connection.", exception);
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return urlConnection;
    }

    public static String readFromConnection(HttpURLConnection urlConnection){
        String result = null;
        InputStream inputStream = null;
        try {
            inputStream = urlConnection.getInputStream();
            result =  readFromStream(inputStream);
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Error while reading data from connection", exception);
        }
        return result;
    }

    public static void writeToConnection(HttpURLConnection urlConnection, String message){
        OutputStream outputStream = null;
        try {
            outputStream = urlConnection.getOutputStream();
            writeToStream(outputStream, message);
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Error while writing data to connection", exception);
        }
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = null;
                line = reader.readLine();
            while (line != null) {
                output.append(line);
                    line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static void writeToStream(OutputStream outputStream, String bodyString) throws IOException{
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, Charset.forName("UTF-8"));
        BufferedWriter writer = new BufferedWriter(outputStreamWriter);
        writer.write(bodyString);
        writer.flush();
        writer.close();
    }

    public static JSONObject convertResponseToJSON(String strJSON) {
        boolean success = false;
        String errorMessage = "";
        JSONObject resultJSON = null;
        try {
            resultJSON = new JSONObject(strJSON);
            success = resultJSON.getBoolean("success");
            errorMessage = resultJSON.getString("error");
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Falling to convert received data to proper JSONObject", e);
        }

        try {
            if (!success) {
                throw new IOException("Authorization is unsuccessful." +
                        " Error message from server: " + errorMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return resultJSON;
    }

    public static boolean isConnectionOK(HttpURLConnection urlConnection) {
        try {
            if (urlConnection.getResponseCode() != 200) {
                throw new IOException("Connection response code: " + urlConnection.getResponseCode()
                        + "; Connection response message: " + urlConnection.getResponseMessage());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Wrong connection response. ", e);
            return false;
        }
        return true;
    }

    public static String makeAuthorizationPostRequest(String urlString, String userID) {
        HttpURLConnection urlConnection = createConnection(createUrl(urlString), "POST", null);
        writeToConnection(urlConnection, "{\"userID\":\"" + userID + "\"}");
        String resultBody = readFromConnection(urlConnection);
        String tempCookiesString = getCookiesString(urlConnection);

        if (!isConnectionOK(urlConnection)) {
            urlConnection.disconnect();
            return null;
        }
        urlConnection.disconnect();

        // Call this method to check if response is okay:
        convertResponseToJSON(resultBody);

        return tempCookiesString;
    }

    public static JSONObject makeGetRequest(String urlString, String cookiesString) {
        HttpURLConnection urlConnection = createConnection(createUrl(urlString), "GET",
                cookiesString);
        String resultBody = readFromConnection(urlConnection);

        if (!isConnectionOK(urlConnection)) {
            urlConnection.disconnect();
            return null;
        }
        urlConnection.disconnect();

        return convertResponseToJSON(resultBody);
    }

    public static JSONObject makePostRequest(String urlString, String cookiesString, String bodyString) {
        HttpURLConnection urlConnection = createConnection(createUrl(urlString), "POST",
                cookiesString);
        writeToConnection(urlConnection, bodyString);
        String resultBody = readFromConnection(urlConnection);

        if (!isConnectionOK(urlConnection)) {
            urlConnection.disconnect();
            return null;
        }
        urlConnection.disconnect();

        return convertResponseToJSON(resultBody);
    }

    public static List<CurrenciesRates> getDataFromJSONResponse(JSONObject receivedData) {

        ArrayList<CurrenciesRates> resAllCurrencies = new ArrayList<CurrenciesRates>();
        try {
            JSONArray sourceAllCurrencies = receivedData.getJSONArray("subscriptionCategories");

            for (int i = 0; i < sourceAllCurrencies.length(); i++) {
                JSONObject sourceOneCurrPair = sourceAllCurrencies.getJSONObject(i);
                JSONArray sourceAllBanks = sourceOneCurrPair.getJSONArray("sources");
                ArrayList<BankRates> resAllBanks = new ArrayList<BankRates>();

                for (int j = 0; j < sourceAllBanks.length(); j++) {

                    // Create BankRates
                    JSONObject sourceOneBank = sourceAllBanks.getJSONObject(j);

                    // create list with quotation
                    JSONArray sourceBankRatesList = sourceOneBank.getJSONArray("ranges");
                    ArrayList<ExchangeRate> resBankRatesList = new ArrayList<ExchangeRate>();

                    for (int k = 0; k < sourceBankRatesList.length(); k++) {
                        JSONObject sourceOneRate = sourceBankRatesList.getJSONObject(k);
                        ExchangeRate resOneRate = new ExchangeRate(
                                sourceOneRate.getInt("range"),
                                (float) sourceOneRate.getDouble("buyPriceNow"),
                                (float) sourceOneRate.getDouble("salePriceNow"));
                        resBankRatesList.add(resOneRate);
                    }
                    // create list of quotation end.

                    BankRates resOneBank = new BankRates(
                            sourceOneBank.getString("name"),
                            resBankRatesList);
                    // Create BankRates end

                    resAllBanks.add(resOneBank);
                }
                CurrenciesRates resOneCurrenciesRates = new CurrenciesRates(
                        sourceOneCurrPair.getString("fullName"),
                        sourceOneCurrPair.getString("name"),
                        resAllBanks);
                resAllCurrencies.add(resOneCurrenciesRates);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resAllCurrencies;
    }

    public static SubscribedData getDataFromJSONResponse2(JSONObject receivedData) {
        SubscribedData resultData = new SubscribedData();
        try {
            JSONArray sourceSubCurrencyPairs = receivedData.getJSONArray("subscriptionCategories");
            Log.v(LOG_TAG, "N of CurrPairs: " + sourceSubCurrencyPairs.length());
            for (int i = 0; i < sourceSubCurrencyPairs.length(); i++) {
                Log.e(LOG_TAG, "Index counter: " + i);

                JSONObject sourceOneCurrPair = sourceSubCurrencyPairs.getJSONObject(i);
                SubscribedData.CurrencyPair resOneCurrencyPair = new SubscribedData.CurrencyPair();
                resOneCurrencyPair.setId(getInteger(sourceOneCurrPair, "id"));
                resOneCurrencyPair.setName(sourceOneCurrPair.getString("name"));
                resOneCurrencyPair.setFullName(sourceOneCurrPair.getString("fullName"));

                JSONArray sourceAllBanks = sourceOneCurrPair.getJSONArray("sources");
                ArrayList<SubscribedData.Bank> resAllBanks = new ArrayList<SubscribedData.Bank>();
                for (int j = 0; j < sourceAllBanks.length(); j++) {
                    // Create BankRates
                    JSONObject sourceOneBank = sourceAllBanks.getJSONObject(j);
                    SubscribedData.Bank resOneBank = new SubscribedData.Bank();
                    resOneBank.setName(sourceOneBank.getString("name"));
                    resOneBank.setId(getInteger(sourceOneBank, "id"));
                    // create list with quotation
                    JSONArray sourceBankQuotList = sourceOneBank.getJSONArray("ranges");
                    ArrayList<SubscribedData.Quotation> resBankQuotList = new ArrayList<SubscribedData.Quotation>();

                    for (int k = 0; k < sourceBankQuotList.length(); k++) {
                        JSONObject sourceOneQoutation = sourceBankQuotList.getJSONObject(k);
                        SubscribedData.Quotation resOneQuotation = new SubscribedData.Quotation();
                        resOneQuotation.setId(getInteger(sourceOneQoutation, "id"));
                        resOneQuotation.setFrom(getInteger(sourceOneQoutation, "range"));
                        resOneQuotation.setBuyPriceNow(getFloat(sourceOneQoutation, "buyPriceNow"));
                        resOneQuotation.setSalePriceNow(getFloat(sourceOneQoutation, "salePriceNow"));
                        resOneQuotation.setDateTime(sourceOneQoutation.getString("inserDateTime"));

                        ArrayList<SubscribedData.Trigger> triggers = new ArrayList<>();
                        for (int m = 0; m < 4; m++){
                            triggers.add(null);
                        }
                        SubscribedData.Trigger buyMinTrigger = new SubscribedData.Trigger(
                                getInteger(sourceOneQoutation, "buyMinTriggerId"),
                                getInteger(sourceOneQoutation, "buyMinTriggerFireType"),
                                SubscribedData.BUY_MIN,
                                getFloat(sourceOneQoutation, "buyMinTriggerPrice")
                        );
                        triggers.add(SubscribedData.BUY_MIN, buyMinTrigger);

                        SubscribedData.Trigger buyMoreTrigger = new SubscribedData.Trigger(
                                getInteger(sourceOneQoutation, "buyMoreTriggerId"),
                                getInteger(sourceOneQoutation, "buyMoreTriggerFireType"),
                                SubscribedData.BUY_MAX,
                                getFloat(sourceOneQoutation, "buyMoreTriggerPrice")
                        );
                        triggers.add(SubscribedData.BUY_MAX, buyMoreTrigger);

                        SubscribedData.Trigger saleMinTrigger = new SubscribedData.Trigger(
                                getInteger(sourceOneQoutation, "sellMinTriggerId"),
                                getInteger(sourceOneQoutation, "sellMinTriggerFireType"),
                                SubscribedData.SALE_MIN,
                                getFloat(sourceOneQoutation, "sellMinTriggerPrice")
                        );
                        triggers.add(SubscribedData.SALE_MIN, saleMinTrigger);

                        SubscribedData.Trigger saleMoreTrigger = new SubscribedData.Trigger(
                                getInteger(sourceOneQoutation, "saleMoreTriggerId"),
                                getInteger(sourceOneQoutation, "saleMoreTriggerFireType"),
                                SubscribedData.SALE_MAX,
                                getFloat(sourceOneQoutation, "saleMoreTriggerPrice")
                        );
                        triggers.add(SubscribedData.SALE_MAX, saleMoreTrigger);

                        resOneQuotation.setTriggers(triggers);
                        resOneQuotation.setPrecision(getInteger(sourceOneQoutation,"precision"));
                        resOneQuotation.setShowSelPrice(sourceOneQoutation.getBoolean("showSellPrice"));
                        resOneQuotation.setTriggerFireType(getInteger(sourceOneQoutation, "triggerFireType"));

                        resBankQuotList.add(resOneQuotation);
                    }
                    // create list of quotation end.
                    resOneBank.setQuotations(resBankQuotList);
                    resAllBanks.add(resOneBank);
                }
                resOneCurrencyPair.setBanks(resAllBanks);
                resultData.add(resOneCurrencyPair);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultData;
    }

    private static int getInteger (JSONObject jsonobject, String fieldName) {
        try {
            if (!jsonobject.isNull(fieldName)) {
                return jsonobject.getInt(fieldName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static float getFloat (JSONObject jsonobject, String fieldName) {
        try {
            if (!jsonobject.isNull(fieldName)) {
                return (float) jsonobject.getDouble(fieldName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1.0f;
    }
}