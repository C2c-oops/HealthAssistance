package com.c2c.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.c2c.myapplication.R;

public class MainActivity extends AppCompatActivity {

    Button button, button2;
    ImageButton buttonImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.btnSymptoms);
        buttonImg = findViewById(R.id.liveUpdateButton);
        button2 = findViewById(R.id.btnHospital);

        button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SymptomsPrecaution.class);
            startActivity(intent);
        });

        buttonImg.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LiveUpdates.class);
            startActivity(intent);
        });

        button2.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), NearbyHospital.class);
            startActivity(intent);
        });
    }
}
