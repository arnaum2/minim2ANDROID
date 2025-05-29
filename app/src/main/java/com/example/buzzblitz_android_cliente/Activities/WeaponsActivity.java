package com.example.buzzblitz_android_cliente.Activities;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buzzblitz_android_cliente.Adapters.MySkinAdapter;
import com.example.buzzblitz_android_cliente.Adapters.MyWeaponsAdapter;
import com.example.buzzblitz_android_cliente.Models.ConsultaTienda;
import com.example.buzzblitz_android_cliente.Models.Objeto;
import com.example.buzzblitz_android_cliente.Models.AuthUtil;
import com.example.buzzblitz_android_cliente.R;
import com.example.buzzblitz_android_cliente.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeaponsActivity extends BaseActivity {
    private RecyclerView rvObjetos;
    private TextView tvEmpty;
    private MyWeaponsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userweapons);

        rvObjetos = findViewById(R.id.rvObjetos);
        tvEmpty = findViewById(R.id.tvEmpty);

        rvObjetos.setLayoutManager(new LinearLayoutManager(this));
        loadUserWeapons();
    }

    private void loadUserWeapons() {
        String userId = AuthUtil.getCurrentUserId(this);
        Log.d("SkinsActivity", "User ID obtenido: " + userId);
        if (userId == null || userId.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvObjetos.setVisibility(View.GONE);
            return;
        }
        Call<ConsultaTienda> call = RetrofitClient.getApiService().getArmasUsuario(userId);
        call.enqueue(new Callback<ConsultaTienda>() {
            @Override
            public void onResponse(Call<ConsultaTienda> call, Response<ConsultaTienda> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<Objeto> armas = response.body().getConsulta();
                    if(armas.isEmpty()) {
                        tvEmpty.setVisibility(View.VISIBLE);
                        rvObjetos.setVisibility(View.GONE);
                    } else {
                        tvEmpty.setVisibility(View.GONE);
                        rvObjetos.setVisibility(View.VISIBLE);
                        adapter = new MyWeaponsAdapter(armas, WeaponsActivity.this);
                        rvObjetos.setAdapter(adapter);
                    }
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                    rvObjetos.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ConsultaTienda> call, Throwable t) {
                tvEmpty.setVisibility(View.VISIBLE);
                rvObjetos.setVisibility(View.GONE);
                Toast.makeText(WeaponsActivity.this, "Error al cargar armas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}