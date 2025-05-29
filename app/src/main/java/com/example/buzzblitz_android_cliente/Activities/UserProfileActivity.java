package com.example.buzzblitz_android_cliente.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buzzblitz_android_cliente.R;

public class UserProfileActivity extends BaseActivity {

    private TextView tvName, tvTotalHoney, tvGoldenFlowers, tvNormalFlowers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileuser);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        String userName = sharedPreferences.getString("currentUserName", "");

        tvName = findViewById(R.id.textView5);
        tvTotalHoney = findViewById(R.id.tvTotalHoney);
        tvGoldenFlowers = findViewById(R.id.tvGoldenFlowers);
        tvNormalFlowers = findViewById(R.id.tvNormalFlowers);

        tvName.setText("Name: " + userName);

        TextView tvUserIdCorner = findViewById(R.id.tvUserIdCorner);
        tvUserIdCorner.setText(sharedPreferences.getString("currentUserId", ""));

        Button btnInventory = findViewById(R.id.btnGoToInventory);
        btnInventory.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, InventoryActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        tvTotalHoney.setText("Total honey jars: " + prefs.getInt("currentTarrosMiel", 0));
        tvNormalFlowers.setText("Normal flowers: " + prefs.getInt("currentFlor", 0));
        tvGoldenFlowers.setText("Golden flowers: " + prefs.getInt("currentFloreGold", 0));
    }
}
