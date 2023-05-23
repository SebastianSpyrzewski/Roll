package com.example.roll;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{
    SharedPreferences sharedPref;
    private Button doneButton;
    private Spinner colorSpinner;
    private EditText nameField;
    private long colorId;
    private String name;
    private static final String[] themes = {"classic", "red", "green", "blue", "yellow", "pacman"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPref = getSharedPreferences("mySettings", MODE_PRIVATE);
        doneButton = findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainMenu();
            }
        });
        colorSpinner = findViewById(R.id.colorSpinner);
        colorSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> arrayAdapter =  new ArrayAdapter(this, android.R.layout.simple_spinner_item, themes);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(arrayAdapter);
        colorSpinner.setSelection((int)sharedPref.getLong("colorPalette", 0));
        nameField = findViewById(R.id.editTextName);
        nameField.setText(sharedPref.getString("name", "unnamed"));
        nameField = findViewById(R.id.editTextName);

    }

    public void mainMenu(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("colorPalette", colorId);
        editor.putString("name", nameField.getText().toString());
        editor.commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        colorId = id;
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }
}