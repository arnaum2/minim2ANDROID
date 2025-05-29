package com.example.buzzblitz_android_cliente.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.buzzblitz_android_cliente.R;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        TextView tvUserIdCorner = findViewById(R.id.tvUserIdCorner);
        tvUserIdCorner.setText(sharedPreferences.getString("currentUserId", ""));

        AppCompatButton btnCredits = findViewById(R.id.btnCredits);
        btnCredits.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, CreditsActivity.class));
        });

        AppCompatButton btnPolitica = findViewById(R.id.btnPoliticaPriv);
        btnPolitica.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, PoliticaPrivacidadActivity.class));
        });

        AppCompatButton btnTerminos = findViewById(R.id.btnTerminosCondiciones);
        btnTerminos.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, TerminosActivity.class));
        });
    }
}
