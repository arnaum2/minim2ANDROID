package com.example.buzzblitz_android_cliente.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buzzblitz_android_cliente.R;
import com.example.buzzblitz_android_cliente.RetrofitClient;
import com.example.buzzblitz_android_cliente.Services.GameBuzzBlitzService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BorrarUsuarioActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrarusuario);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        TextView tvUserIdCorner = findViewById(R.id.tvUserIdCorner);
        tvUserIdCorner.setText(sharedPreferences.getString("currentUserId", ""));

        ImageButton btnSi = findViewById(R.id.boton_imagenyes);
        ImageButton btnNo = findViewById(R.id.boton_imagenno);
        GameBuzzBlitzService apiService = RetrofitClient.getApiService();

        btnSi.setOnClickListener(v -> {
            String currentUserEmail = sharedPreferences.getString("currentUser", "");
            String userId = sharedPreferences.getString("currentUserId", "");

            if (!userId.isEmpty()) {
                Call<Void> call = apiService.deleteUsuario(userId);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();

                            Intent intent = new Intent(BorrarUsuarioActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            Toast.makeText(BorrarUsuarioActivity.this, "Error al borrar usuario", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(BorrarUsuarioActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Usuario no identificado", Toast.LENGTH_SHORT).show();
            }
        });

        btnNo.setOnClickListener(v -> finish());
    }
}