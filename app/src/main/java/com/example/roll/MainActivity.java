package com.example.roll;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button adventureButton;
    private Button randomButton;
    private Button settingsButton;
    private Button otherGameButton;
    private Button highScoresButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adventureButton = findViewById(R.id.adventureButton);
        adventureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAdventureMode();
            }
        });
        randomButton = findViewById(R.id.randomButton);
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRandomMode();
            }
        });
        settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettings();
            }
        });
        otherGameButton = findViewById(R.id.otherGameButton);
        otherGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMultiBallMode();
            }
        });
        highScoresButton = findViewById(R.id.highScoresButton2);
        highScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHighScores();
            }
        });
    }

    public void openAdventureMode(){
        int mode = 1;
        Intent intent = new Intent(this, StartAdventureActivity.class);
        intent.putExtra("mode", mode);
        startActivity(intent);
    }
    public void openRandomMode(){
        int mode = -1;
        Intent intent = new Intent(this, StartAdventureActivity.class);
        intent.putExtra("mode", mode);
        startActivity(intent);
    }
    public void openSettings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    public void openMultiBallMode(){
        Intent intent = new Intent(this, OtherGameActivity.class);
        startActivity(intent);
    }
    public void openHighScores(){
        Intent intent = new Intent(this, HighScoresActivity.class);
        startActivity(intent);
    }
}