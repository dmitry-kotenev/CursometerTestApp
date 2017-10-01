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
 * Вспомогательные функции.
 *
 * Helper methods.
 */

final class CursometerUtils {

    private static final String LOG_TAG = "CutsometerUtils";
    private static final String COOKIES_HEADER = "Set-Cookie";

    /**
     * Transforms url in form of String to the URL object.
     * @param stringUrl - url String.
     * @return - URL object or null in case of exception occurred.
     */
    private static URL createUrl(String stringUrl) {
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error during creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Returns cookies from the connection in form of String.
     * @param urlConnection - HttpURLConnection object.
     * @return - cookies or null in case of URL object does not contain cookies.
     */
    private static String getCookiesString(HttpURLConnection urlConnection) {
        Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
        List<String> cookiesList = headerFields.get(COOKIES_HEADER);
        if (cookiesList == null){
            return null;
        }
        return TextUtils.join(";", cookiesList);
    }

    /**
     * Create HttpURLConnection.
     * @param url - URL object.
     * @param requestType - "POST" or "GET"
     * @param cookies - cookies in form of String.
     * @return - HttpURLConnection.
     */
    private static HttpURLConnection createConnection(URL url, String requestType,
                                                      @Nullable String cookies) {

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(requestType);
            urlConnection.setReadTimeout(15000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.addRequestProperty("Accept", "application/json");
            urlConnection.addRequestProperty("Content-Type", "application/json");

            urlConnection.setDoInput(true);
            if (requestType.equals("POST")) {
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

    /**
     * Read from connection and return result in a form of String.
     * @param urlConnection - HttpURLConnection object.
     * @return String or null in case of exception is occurred.
     */
    private static String readFromConnection(HttpURLConnection urlConnection){
        String result = null;
        try {
            InputStream inputStream = urlConnection.getInputStream();
            result =  readFromStream(inputStream);
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Error while reading data from connection", exception);
        }
        return result;
    }

    /**
     * Write message to the HttpURLConnection.
     * @param urlConnection - HttpURLConnection object.
     * @param message - String.
     */
    private static void writeToConnection(HttpURLConnection urlConnection, String message){
        try {
            OutputStream outputStream = urlConnection.getOutputStream();
            writeToStream(outputStream, message);
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Error while writing data to connection", exception);
        }
    }

    /**
     * Read message from connection stream. Is used in readFromConnection method.
     * @param inputStream - InputStream of HttpURLConnection object.
     * @return received information as a String.
     * @throws IOException - It is supposed, that exception will be caught in readFromConnection
     * method.
     */
    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                    line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     *
     * @param outputStream - OutputStream of HttpURLConnection object.
     * @param bodyString - Information as a String that will be sent.
     * @throws IOException - It is supposed, that exception will be caught in writeToConnection
     * method.
     */
    private static void writeToStream(OutputStream outputStream, String bodyString)
            throws IOException{
        OutputStreamWriter outputStreamWriter =
                new OutputStreamWriter(outputStream, Charset.forName("UTF-8"));
        BufferedWriter writer = new BufferedWriter(outputStreamWriter);
        writer.write(bodyString);
        writer.flush();
        writer.close();
    }

    /**
     * @param strJSON - String.
     * @return - JSONObject.
     */
    private static JSONObject convertResponseToJSON(String strJSON) {
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

    /**
     * Check connection for the response code 200.
     * @param urlConnection - HttpURLConnection object.
     * @return boolean value.
     */
    private static boolean isConnectionOK(HttpURLConnection urlConnection) {
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

    /**
     * @param urlString - API authorization request endpoint.
     * @param userID - unique usr ID.
     * @return - cookies as String.
     */
    static String makeAuthorizationPostRequest(String urlString, String userID) {
        HttpURLConnection urlConnection = createConnection(createUrl(urlString), "POST", null);
        // Body format of the request is hardcoded:
        writeToConnection(urlConnection, "{\"userID\":\"" + userID + "\"}");
        String resultBody = readFromConnection(urlConnection);
        String tempCookiesString = getCookiesString(urlConnection);

        if (!isConnectionOK(urlConnection)) {
            urlConnection.disconnect();
            return null;
        }
        urlConnection.disconnect();

        // Calling following method to check if response is okay (success : true):
        convertResponseToJSON(resultBody);

        return tempCookiesString;
    }

    /**
     * @param urlString - API endpoint.
     * @param cookiesString - cookies.
     * @return - body of the response as String.
     */
    static JSONObject makeGetRequest(String urlString, String cookiesString) {
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

    /**
     * @param urlString - API endpoint.
     * @param cookiesString - cookies.
     * @param bodyString - body of the "POST" request.
     * @return - body of the response as String.
     */
    public static JSONObject makePostRequest(String urlString, String cookiesString,
                                             String bodyString) {
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

    /**
     * Convert JSONObject with a currencies subscription list to the SubscribedData object.
     * @param receivedData - JSONObject object.
     * @return - SubscribedData object.
     */
    static SubscribedData getDataFromJSONResponse(JSONObject receivedData) {
        SubscribedData resultData = new SubscribedData();
        try {
            JSONArray sourceSubCurrencyPairs = receivedData.getJSONArray("subscriptionCategories");
            for (int i = 0; i < sourceSubCurrencyPairs.length(); i++) {

                JSONObject sourceOneCurrPair = sourceSubCurrencyPairs.getJSONObject(i);
                SubscribedData.CurrencyPair resOneCurrencyPair = new SubscribedData.CurrencyPair();
                resOneCurrencyPair.setId(getInteger(sourceOneCurrPair, "id"));
                resOneCurrencyPair.setName(sourceOneCurrPair.getString("name"));
                resOneCurrencyPair.setFullName(sourceOneCurrPair.getString("fullName"));

                JSONArray sourceAllBanks = sourceOneCurrPair.getJSONArray("sources");
                ArrayList<SubscribedData.Bank> resAllBanks = new ArrayList<>();
                for (int j = 0; j < sourceAllBanks.length(); j++) {

                    JSONObject sourceOneBank = sourceAllBanks.getJSONObject(j);
                    SubscribedData.Bank resOneBank = new SubscribedData.Bank();
                    resOneBank.setName(sourceOneBank.getString("name"));
                    resOneBank.setId(getInteger(sourceOneBank, "id"));

                    JSONArray sourceBankQuotList = sourceOneBank.getJSONArray("ranges");
                    ArrayList<SubscribedData.Quotation> resBankQuotList = new ArrayList<>();

                    for (int k = 0; k < sourceBankQuotList.length(); k++) {
                        JSONObject sourceOneQoutation = sourceBankQuotList.getJSONObject(k);
                        SubscribedData.Quotation resOneQuotation = new SubscribedData.Quotation();
                        resOneQuotation.setId(getInteger(sourceOneQoutation, "id"));
                        resOneQuotation.setFrom(getInteger(sourceOneQoutation, "range"));
                        resOneQuotation.setBuyPriceNow(getFloat(sourceOneQoutation, "buyPriceNow"));
                        resOneQuotation.
                                setSalePriceNow(getFloat(sourceOneQoutation, "salePriceNow"));
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
                        resOneQuotation.
                                setShowSelPrice(sourceOneQoutation.getBoolean("showSellPrice"));
                        resOneQuotation.setTriggerFireType(
                                getInteger(sourceOneQoutation, "triggerFireType"));

                        resBankQuotList.add(resOneQuotation);
                    }

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