package com.example.buzzblitz_android_cliente.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.buzzblitz_android_cliente.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> {
            startActivity(new Intent(this, CargarJuegoActivity.class));
            finish();
        });
    }

}
