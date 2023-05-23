package com.example.roll;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class AdventureWinActivity extends AppCompatActivity {
    private Button goBack;
    private Button tryAgainButton;
    private Button highScoresButton;
    private TextView levelTV;
    private TextView timeTV;
    private long miliseconds;
    private int mode;
    private int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adventure_win);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mode = extras.getInt("mode");
            level = extras.getInt("level");
            miliseconds = extras.getLong("time");
        }
        levelTV=findViewById(R.id.levelView);
        timeTV=findViewById(R.id.timeView);
        levelTV.setText("You have completed "+level*mode + (level==1?" level":" levels"));
        {
            long minutes = miliseconds/60000;
            long seconds = (miliseconds%60000)/1000;
            long tenth = (miliseconds%1000)/100;
            String text = "in time: "+minutes + (seconds > 9 ? ":" : ":0") + seconds + "." + tenth;
            timeTV.setText(text);
        }
        goBack = findViewById(R.id.adventureWinButton);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainMenu();
            }
        });
        tryAgainButton = findViewById(R.id.tryAgainButton);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryAgain();
            }
        });
        highScoresButton = findViewById(R.id.highScoresButton);
        highScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHighScores();
            }
        });
    }
    public void mainMenu(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void tryAgain(){
        Intent intent = new Intent(this, AdventureActivity.class);
        intent.putExtra("mode", mode);
        startActivity(intent);
    }
    public void openHighScores(){
        Intent intent = new Intent(this, HighScoresActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}