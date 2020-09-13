package com.c2c.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.c2c.myapplication.R;

public class MainActivity extends AppCompatActivity {

    Button button;
    ImageButton button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.btnSymptoms);
        button1 = findViewById(R.id.liveUpdateButton);

        button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SymptomsPrecaution.class);
            startActivity(intent);
        });

        button1.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LiveUpdates.class);
            startActivity(intent);
        });
    }
}
