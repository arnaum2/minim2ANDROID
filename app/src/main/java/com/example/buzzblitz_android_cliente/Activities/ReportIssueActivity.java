package com.example.buzzblitz_android_cliente.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buzzblitz_android_cliente.Models.Issue;
import com.example.buzzblitz_android_cliente.R;
import com.example.buzzblitz_android_cliente.Services.IssueBlitzService;
import com.example.buzzblitz_android_cliente.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportIssueActivity extends AppCompatActivity {

    private EditText dateEditText;
    private EditText titolEditText;
    private EditText senderEditText;
    private EditText messageEditText;
    private Button enviarButton;
    private Button enrereButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);

        // Referències als elements del layout
        dateEditText    = findViewById(R.id.date);
        titolEditText = findViewById(R.id.titol);
        senderEditText  = findViewById(R.id.sender);
        messageEditText = findViewById(R.id.message);
        enviarButton    = findViewById(R.id.btnReportIssue);
        enrereButton    = findViewById(R.id.enrere1);

        // Clic per enviar la denúncia
        enviarButton.setOnClickListener(view -> {
            String dateText    = dateEditText.getText().toString().trim();
            String titolText    = titolEditText.getText().toString().trim();
            String senderText  = senderEditText.getText().toString().trim();
            String messageText = messageEditText.getText().toString().trim();

            if (dateText.isEmpty() || senderText.isEmpty() || messageText.isEmpty()) {
                Toast.makeText(this, "Omple tots els camps", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crea l'objecte Issue
            Issue issue = new Issue(dateText, titolText, senderText, messageText);
            sendIssue(issue);
        });

        // Clic per tornar a la MainActivity
        enrereButton.setOnClickListener(view -> {
            Intent intent = new Intent(ReportIssueActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void sendIssue(Issue issue) {
        IssueBlitzService api = RetrofitClient.getIssueService();

        Call<Void> call = api.enviarIssue(issue);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Log.d("ISSUE", "ERROR SERVER CODE: " + response.code());
                    Toast.makeText(ReportIssueActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();
                    return;
                }
                Log.d("ISSUE", "201 Created");
                Toast.makeText(ReportIssueActivity.this, "Denúncia enviada!", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ISSUE", "ERROR: " + t.getMessage(), t);
                Toast.makeText(ReportIssueActivity.this, "Error de connexió", Toast.LENGTH_LONG).show();
            }
        });
    }
}
