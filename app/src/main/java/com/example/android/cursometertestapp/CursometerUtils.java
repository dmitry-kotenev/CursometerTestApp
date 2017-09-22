package com.example.android.cursometertestapp;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

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
            Log.e(LOG_TAG, "Connection error.", exception);
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
}