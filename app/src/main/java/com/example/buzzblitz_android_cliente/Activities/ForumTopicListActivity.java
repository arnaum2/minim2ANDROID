package com.example.buzzblitz_android_cliente.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.buzzblitz_android_cliente.Adapters.ForumTopicAdapter;
import com.example.buzzblitz_android_cliente.Models.UltimoComentarioDTO;
import com.example.buzzblitz_android_cliente.R;
import com.example.buzzblitz_android_cliente.RetrofitClient;
import com.example.buzzblitz_android_cliente.Services.ForumBuzzBlitzService;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForumTopicListActivity extends BaseActivity {
    private ForumTopicAdapter adapter;
    private List<UltimoComentarioDTO> temas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        RecyclerView rvTopics = findViewById(R.id.rvFriendsList);
        rvTopics.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ForumTopicAdapter(temas, this::abrirTema);
        rvTopics.setAdapter(adapter);

        cargarUltimosComentarios();
    }

    private void cargarUltimosComentarios() {
        ForumBuzzBlitzService forumService = RetrofitClient.getForumService();
        forumService.getUltimosComentarios().enqueue(new Callback<List<UltimoComentarioDTO>>() {
            @Override
            public void onResponse(Call<List<UltimoComentarioDTO>> call, Response<List<UltimoComentarioDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    temas.clear();
                    temas.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ForumTopicListActivity.this, "Error al cargar temas", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<UltimoComentarioDTO>> call, Throwable t) {
                Toast.makeText(ForumTopicListActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void abrirTema(String tema) {
        Intent intent = new Intent(this, ForumActivity.class);
        intent.putExtra("tema", tema);
        startActivity(intent);
    }
}