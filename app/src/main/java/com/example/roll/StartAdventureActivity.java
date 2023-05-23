package com.example.roll;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartAdventureActivity extends AppCompatActivity {
    private Button startAdventureButton;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_adventure);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mode = extras.getInt("mode");
        }
        startAdventureButton = findViewById(R.id.startAdventureButton);
        startAdventureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAdventureMode();
            }
        });
    }

    public void startAdventureMode(){
        Intent intent = new Intent(this, AdventureActivity.class);
        intent.putExtra("mode", mode);
        startActivity(intent);
    }
}