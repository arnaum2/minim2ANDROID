package com.example.buzzblitz_android_cliente.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buzzblitz_android_cliente.R;

public class InventoryActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinventory);

        ImageButton btnSkins = findViewById(R.id.boton_imagenskins);
        ImageButton btnWeapons = findViewById(R.id.boton_imagenweapons);

        btnSkins.setOnClickListener(v -> {
            Intent intent = new Intent(InventoryActivity.this, SkinsActivity.class);
            startActivity(intent);
        });

        btnWeapons.setOnClickListener(v ->
                startActivity(new Intent(InventoryActivity.this, WeaponsActivity.class))
        );

        btnSkins.setOnClickListener(v ->
                startActivity(new Intent(InventoryActivity.this, SkinsActivity.class))
        );
    }
}
