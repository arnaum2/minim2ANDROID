package com.example.buzzblitz_android_cliente.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buzzblitz_android_cliente.R;

public class OpcionesActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        TextView tvUserIdCorner = findViewById(R.id.tvUserIdCorner);
        tvUserIdCorner.setText(sharedPreferences.getString("currentUserId", ""));

        Button btnTutorial = findViewById(R.id.btnTutorial);
        Button btnBorrarUsuario = findViewById(R.id.btnBorrarUsuario);

        btnTutorial.setOnClickListener(v ->
                startActivity(new Intent(OpcionesActivity.this, TutorialActivity.class))
        );

        btnBorrarUsuario.setOnClickListener(v ->
                startActivity(new Intent(OpcionesActivity.this, BorrarUsuarioActivity.class))
        );
    }
}