package com.ddmeng.androidmruntimepermissionsample;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
    private static final String DEBUG_TAG = "PermissionTest";

    public static void sendRequest(String inputUrl) {

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String input = params[0];
                InputStream is = null;
                // Only display the first 500 characters of the retrieved
                // web page content.
                int len = 500;

                try {
                    URL url = new URL(input);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    // Starts the query
                    conn.connect();
                    int response = conn.getResponseCode();
                    Log.d(DEBUG_TAG, "The response is: " + response);
                    is = conn.getInputStream();

                    // Convert the InputStream into a string
                    String contentAsString = readIt(is, len);
                    return contentAsString;

                    // Makes sure that the InputStream is closed after the app is
                    // finished using it.
                } catch (IOException e) {
                    Log.e(DEBUG_TAG, "Catch IOException: " + e);
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            Log.e(DEBUG_TAG, "Catch IOException when close: " + e);
                        }
                    }
                }
                return null;
            }


            @Override
            protected void onPostExecute(String result) {
                Log.d(DEBUG_TAG, "The result is: " + result);
            }
        }.execute(inputUrl);
    }

    // Reads an InputStream and converts it to a String.
    public static String readIt(InputStream stream, int len) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
