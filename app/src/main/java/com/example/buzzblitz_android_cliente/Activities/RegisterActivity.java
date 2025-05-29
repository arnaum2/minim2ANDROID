package com.example.buzzblitz_android_cliente.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.buzzblitz_android_cliente.Models.UsuReg;
import com.example.buzzblitz_android_cliente.R;
import com.example.buzzblitz_android_cliente.RetrofitClient;
import com.example.buzzblitz_android_cliente.Services.GameBuzzBlitzService;

import java.security.MessageDigest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmailRegister, etPasswordRegister, etRepeatPassword;
    private EditText etRespuesta1, FullNameRegister, IdUserRegister;
    private Spinner spinnerPregunta1;
    private Button btnRegister, btnGoToLogin;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FullNameRegister = findViewById(R.id.FullName);
        IdUserRegister = findViewById(R.id.IdUserRegister);
        etEmailRegister = findViewById(R.id.etEmailRegister);
        etPasswordRegister = findViewById(R.id.etPasswordRegister);
        etRepeatPassword = findViewById(R.id.etRepeatPassword);
        spinnerPregunta1 = findViewById(R.id.spinnerPregunta1);
        etRespuesta1 = findViewById(R.id.etRespuesta1);
        btnRegister = findViewById(R.id.btnRegister);
        btnGoToLogin = findViewById(R.id.btnGoToLogin);

        sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.security_questions,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPregunta1.setAdapter(adapter);

        btnRegister.setOnClickListener(v -> registerUser(v));
        btnGoToLogin.setOnClickListener(v -> finish());
    }

    public void registerUser(View view) {
        String fullName = FullNameRegister.getText().toString().trim();
        String userId = IdUserRegister.getText().toString().trim();
        String email = etEmailRegister.getText().toString().trim();
        String password = etPasswordRegister.getText().toString().trim();
        String confirmPassword = etRepeatPassword.getText().toString().trim();
        String question1 = spinnerPregunta1.getSelectedItem().toString();
        String answer1 = etRespuesta1.getText().toString().trim();

        String[] nameParts = fullName.split("\\s+", 2);
        if (nameParts.length != 2) {
            showToast("Enter name and surname separated by a space");
            return;
        }
        String firstName = nameParts[0];
        String lastName = nameParts[1];

        if (userId.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showToast("Fill in all required fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showToast("The passwords do not match");
            return;
        }

        UsuReg newUser = new UsuReg(userId, firstName, lastName, password, email, question1, answer1);
        GameBuzzBlitzService api = RetrofitClient.getApiService();

        Call<Void> call = api.registerUsuario(newUser);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    guardarDatosLocales(email, question1, answer1);
                    guardarUserIdEnSharedPreferences(userId);
                    showToast("Registration successful!");
                    finish();
                } else {
                    showToast("Registration error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showToast("Network error: " + t.getMessage());
            }
        });
    }

    private void guardarUserIdEnSharedPreferences(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("currentUserId", userId);
        editor.apply();
    }

    private void guardarDatosLocales(String email, String pregunta, String respuesta) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(email + "_q1", pregunta);
        editor.putString(email + "_a1", hash(respuesta));
        editor.apply();
    }

    private String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Hash error", ex);
        }
    }

    private void showToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}