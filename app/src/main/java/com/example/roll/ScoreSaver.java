package com.example.roll;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ScoreSaver {
    public static void SetScore(int level, int mode, long miliseconds, String name, Context context){
        databaseHelper dbHelper;
        dbHelper = new databaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String table_name = (mode==1 ? "adventure" : "random");
        ContentValues values = new ContentValues();
        values.put("level", level*mode);
        values.put("miliseconds", miliseconds);
        db.insert(table_name, null, values);
        JSONObject postData = new JSONObject();
        String url = (mode==1 ? "http://192.168.71.218:5537/adventure/add" : "http://192.168.71.218:5537/random/add");
        try {
            postData.put("name", name);
            postData.put("level", level*mode);
            postData.put("miliseconds", miliseconds);

            new SendScore().execute(url, postData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dbHelper.close();
    }
    private static class SendScore extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String data = "";
            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(3000);

                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.write(params[1].getBytes());

                wr.flush();
                wr.close();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("TAG", result); // this is expecting a response code to be sent from your server upon receiving the POST data
        }
    }
}
