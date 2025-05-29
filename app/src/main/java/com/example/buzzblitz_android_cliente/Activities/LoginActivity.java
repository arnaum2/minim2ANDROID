package com.example.buzzblitz_android_cliente.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.buzzblitz_android_cliente.Models.UsuarioEnviar;
import com.example.buzzblitz_android_cliente.Models.Usulogin;
import com.example.buzzblitz_android_cliente.Models.AuthUtil;
import com.example.buzzblitz_android_cliente.R;
import com.example.buzzblitz_android_cliente.RetrofitClient;
import com.example.buzzblitz_android_cliente.Services.GameBuzzBlitzService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etUserIdentifier, etPasswordLogin;
    private Button btnLogin, btnGoToRegister;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserIdentifier = findViewById(R.id.etUserIdentifier);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);

        sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);

        btnLogin.setOnClickListener(this::enviarLogin);
        btnGoToRegister.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class))
        );
    }

    public void enviarLogin(View view) {
        String userInput = etUserIdentifier.getText().toString().trim();
        String password = etPasswordLogin.getText().toString().trim();

        if (userInput.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Empty fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Usulogin peticion = new Usulogin();
        peticion.setIdoname(userInput);
        peticion.setPswd(password);

        GameBuzzBlitzService api = RetrofitClient.getApiService();

        Call<UsuarioEnviar> call = api.loginUsuario(peticion);
        call.enqueue(new Callback<UsuarioEnviar>() {
            public void onResponse(Call<UsuarioEnviar> call, Response<UsuarioEnviar> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UsuarioEnviar usuario = response.body();

                    AuthUtil.setCurrentUserId(LoginActivity.this, usuario.getId());
                    AuthUtil.setUserLoggedIn(LoginActivity.this, true);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("currentUserName", usuario.getName());
                    editor.putInt("currentTarrosMiel", usuario.getTarrosMiel());
                    editor.putInt("currentFlor", usuario.getFlor());
                    editor.putInt("currentFloreGold", usuario.getFloreGold());
                    editor.putInt("currentBestScore", usuario.getMejorPuntuacion());
                    editor.putString("currentUserId", usuario.getId());
                    editor.apply();

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    manejarError(response);
                }
            }

            public void onFailure(Call<UsuarioEnviar> call, Throwable t) {
                Log.e("LOGIN_DEBUG", "Network error: ", t);
                Toast.makeText(LoginActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void manejarError(Response<UsuarioEnviar> response) {
        try {
            if (response.errorBody() != null) {
                String errorBody = response.errorBody().string();
                Log.e("LOGIN_DEBUG", "Server error: " + errorBody);
                Toast.makeText(LoginActivity.this, errorBody, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Log.e("LOGIN_DEBUG", "Error reading error body", e);
            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
        }
    }
}