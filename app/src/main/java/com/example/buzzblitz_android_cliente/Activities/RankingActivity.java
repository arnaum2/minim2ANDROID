package com.example.buzzblitz_android_cliente.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buzzblitz_android_cliente.Adapters.MyRankingAdapter;
import com.example.buzzblitz_android_cliente.Models.Info;
import com.example.buzzblitz_android_cliente.Models.InfoList;
import com.example.buzzblitz_android_cliente.R;
import com.example.buzzblitz_android_cliente.RetrofitClient;
import com.example.buzzblitz_android_cliente.Services.GameBuzzBlitzService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankingActivity extends BaseActivity {
    private RecyclerView rvRanking;
    private TextView tvBestScore;
    private static final String TAG = "RANKING_DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        SharedPreferences prefs = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        String userId = prefs.getString("currentUserId", "");
        if (userId.isEmpty()) {
            Toast.makeText(this, "Error: usuario no logueado", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "No hay userId en SharedPreferences");
            return;
        }
        int bestScore = prefs.getInt("currentBestScore", 0);

        tvBestScore = findViewById(R.id.textView6);
        tvBestScore.setText("Tu mejor puntuación: " + bestScore);

        rvRanking = findViewById(R.id.rvRanking);
        rvRanking.setLayoutManager(new LinearLayoutManager(this));

        GameBuzzBlitzService api = RetrofitClient.getApiService();
        Log.d(TAG, "Llamando a getInfo con userId: " + userId);
        api.getInfo(userId).enqueue(new Callback<InfoList>() {
            @Override
            public void onResponse(Call<InfoList> call, Response<InfoList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Info> rankingCompleto = response.body().getRanking();
                    int userPosition = response.body().getPosicionUsuario();

                    Log.d(TAG, "Ranking recibido de la API (size=" + rankingCompleto.size() + "):");
                    for (int i = 0; i < rankingCompleto.size(); i++) {
                        Info info = rankingCompleto.get(i);
                        Log.d(TAG, "Pos " + (i+1) + ": " + info.getUsuario() + " - Puntos: " + info.getMejorPuntuacion());
                    }
                    Log.d(TAG, "Posición del usuario logueado: " + userPosition);

                    List<Info> top5 = new ArrayList<>();
                    for (int i = 0; i < Math.min(5, rankingCompleto.size()); i++) {
                        top5.add(rankingCompleto.get(i));
                    }

                    if (userPosition > 5 && top5.size() == 5) {
                        Info userInfo = null;
                        for (Info info : rankingCompleto) {
                            if (info.getUsuario().equals(userId)) {
                                userInfo = info;
                                break;
                            }
                        }
                        if (userInfo != null) {
                            Log.d(TAG, "Reemplazando el último del top5 por el usuario logueado: " + userInfo.getUsuario() + " - Puntos: " + userInfo.getMejorPuntuacion());
                            top5.set(4, userInfo);
                        } else {
                            Log.e(TAG, "No se encontró el usuario logueado en el rankingCompleto");
                        }
                    }

                    Log.d(TAG, "Lista final que se pasa al adaptador:");
                    for (int i = 0; i < top5.size(); i++) {
                        Info info = top5.get(i);
                        Log.d(TAG, "Item " + i + ": " + info.getUsuario() + " - Puntos: " + info.getMejorPuntuacion());
                    }

                    MyRankingAdapter adapter = new MyRankingAdapter(top5, userId, userPosition);
                    rvRanking.setAdapter(adapter);
                } else {
                    Log.e(TAG, "Error al obtener datos del ranking. Código: " + response.code());
                    Toast.makeText(RankingActivity.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InfoList> call, Throwable t) {
                Log.e(TAG, "Error de conexión al obtener ranking", t);
                Toast.makeText(RankingActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}