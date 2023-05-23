package com.example.roll;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class HighScoresActivity extends AppCompatActivity {

    private RecyclerView adventureRecyclerView;
    private RecyclerView randomRecyclerView;
    private RecyclerView adventureRecyclerViewGlobal;
    private RecyclerView randomRecyclerViewGlobal;
    private String[] stringSet ={"no connection to database"};
    private String[] stringSet2 ={"no connection to database"};
    private static String url1 = "http://192.168.71.218:5537/adventure";
    private static String url2 = "http://192.168.71.218:5537/random";
    private ProgressDialog pDialog;
    private databaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        dbHelper = new databaseHelper(getApplicationContext());
        adventureRecyclerView = findViewById(R.id.adventureScores);
        prepareRecyclerView(adventureRecyclerView, highscores(1));
        randomRecyclerView = findViewById(R.id.randomScores);
        prepareRecyclerView(randomRecyclerView, highscores(2));
        adventureRecyclerViewGlobal = findViewById(R.id.adventureScoresGlobal);
        randomRecyclerViewGlobal = findViewById(R.id.randomScoresGlobal);
        new GetScores().execute();
        prepareRecyclerView(adventureRecyclerViewGlobal, stringSet);
        prepareRecyclerView(randomRecyclerViewGlobal, stringSet2);
    }

    private void prepareRecyclerView(RecyclerView rv, String[] stringSet){
        CustomAdapter adapter = new CustomAdapter(stringSet);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(layoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
    }

    String[] highscores(int type){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<String> itemList = new ArrayList<>();
        String table_name = (type==1 ? "adventure" : "random");
        String order_by = "level desc, miliseconds asc";
        Cursor cursor = db.query(table_name, null, null, null, null, null, order_by);
        int counter = 1;

        while(cursor.moveToNext()){
            if(counter>10){
                long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
                String[] selectionArgs = {String.valueOf(id)};
                db.delete(table_name, "id like ?", selectionArgs);
            }
            else {
                long miliseconds = cursor.getLong(cursor.getColumnIndexOrThrow("miliseconds"));
                long level = cursor.getLong(cursor.getColumnIndexOrThrow("level"));
                long minutes = miliseconds / 60000;
                long seconds = (miliseconds % 60000) / 1000;
                long tenth = (miliseconds % 1000) / 100;
                String padding = (counter==10 ? "           " : "             ");
                String padding2 = (level<10 ? "              " : "            ");
                String text = counter+padding+level + padding2 + minutes + (seconds > 9 ? ":" : ":0") + seconds + "." + tenth;
                itemList.add(text);
            }
            counter++;
        }
        String[] stringSet = new String[itemList.size()];
        itemList.toArray(stringSet);
        return stringSet;
    }

    private class GetScores extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(HighScoresActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            {
                String jsonStr = sh.makeServiceCall(url1);

                Log.e(TAG, "Response from url: " + jsonStr);

                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        // Getting JSON Array node
                        JSONArray contacts = jsonObj.getJSONArray("json_list");

                        // looping through All Contacts
                        List<String> itemList = new ArrayList<>();
                        for (int i = 1; i <= contacts.length(); i++) {
                            JSONObject c = contacts.getJSONObject(i-1);

                            String name = c.getString("imie");
                            Long level = c.getLong("level");
                            Long miliseconds = c.getLong("miliseconds");
                            long minutes = miliseconds / 60000;
                            long seconds = (miliseconds % 60000) / 1000;
                            long tenth = (miliseconds % 1000) / 100;
                            String padding = (i==10 ? "       " : "         ");
                            String padding2 = (level<10 ? "          " : "        ");
                            String text = i+padding+level + padding2 + minutes + (seconds > 9 ? ":" : ":0") + seconds + "." + tenth+"        "+name;
                            itemList.add(text);

                            // adding contact to contact list
                        }
                        stringSet = new String[itemList.size()];
                        itemList.toArray(stringSet);
                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

                    }
                } else {
                    Log.e(TAG, "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "No connection to database",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            }
            {
                String jsonStr = sh.makeServiceCall(url2);

                Log.e(TAG, "Response from url: " + jsonStr);

                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        // Getting JSON Array node
                        JSONArray contacts = jsonObj.getJSONArray("json_list");

                        // looping through All Contacts
                        List<String> itemList = new ArrayList<>();
                        for (int i = 1; i <= contacts.length(); i++) {
                            JSONObject c = contacts.getJSONObject(i-1);

                            String name = c.getString("imie");
                            Long level = c.getLong("level");
                            Long miliseconds = c.getLong("miliseconds");
                            long minutes = miliseconds / 60000;
                            long seconds = (miliseconds % 60000) / 1000;
                            long tenth = (miliseconds % 1000) / 100;
                            String padding = (i==10 ? "       " : "         ");
                            String padding2 = (level<10 ? "          " : "        ");
                            String text = i+padding+level + padding2 + minutes + (seconds > 9 ? ":" : ":0") + seconds + "." + tenth+"        "+name;
                            itemList.add(text);

                        }
                        stringSet2 = new String[itemList.size()];
                        itemList.toArray(stringSet2);
                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

                    }
                } else {
                    Log.e(TAG, "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "No connection to database",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            CustomAdapter adapter = new CustomAdapter(stringSet);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            adventureRecyclerViewGlobal.setLayoutManager(layoutManager);
            adventureRecyclerViewGlobal.setItemAnimator(new DefaultItemAnimator());
            adventureRecyclerViewGlobal.setAdapter(adapter);

            CustomAdapter adapter2 = new CustomAdapter(stringSet2);
            RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
            randomRecyclerViewGlobal.setLayoutManager(layoutManager2);
            randomRecyclerViewGlobal.setItemAnimator(new DefaultItemAnimator());
            randomRecyclerViewGlobal.setAdapter(adapter2);
        }

    }

    @Override
    protected void onDestroy(){
        dbHelper.close();
        super.onDestroy();
    }
}