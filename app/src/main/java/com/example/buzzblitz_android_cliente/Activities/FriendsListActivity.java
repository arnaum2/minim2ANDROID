package com.example.buzzblitz_android_cliente.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.buzzblitz_android_cliente.Adapters.FriendsListAdapter;
import com.example.buzzblitz_android_cliente.R;
import com.example.buzzblitz_android_cliente.RetrofitClient;
import com.example.buzzblitz_android_cliente.Services.FriendBuzzBlitzService;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsListActivity extends BaseActivity {
    private FriendsListAdapter adapter;
    private List<String> amigos = new ArrayList<>();
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        currentUserId = sharedPreferences.getString("currentUserId", "");

        RecyclerView rvFriendsList = findViewById(R.id.rvFriendsList);
        rvFriendsList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendsListAdapter(amigos, new FriendsListAdapter.OnFriendActionListener() {
            @Override
            public void onDelete(int position) {
                eliminarAmigo(position);
            }
        });
        rvFriendsList.setAdapter(adapter);

        cargarAmigos();
    }

    private void cargarAmigos() {
        FriendBuzzBlitzService friendService = RetrofitClient.getFriendService();
        friendService.getAmigos(currentUserId).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    amigos.clear();
                    amigos.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(FriendsListActivity.this, "Error al cargar amigos", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(FriendsListActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eliminarAmigo(int position) {
        String amigoId = amigos.get(position);
        FriendBuzzBlitzService friendService = RetrofitClient.getFriendService();
        friendService.rechazarSolicitud(currentUserId, amigoId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                amigos.remove(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(FriendsListActivity.this, "Amigo eliminado", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(FriendsListActivity.this, "Error al eliminar amigo", Toast.LENGTH_SHORT).show();
            }
        });
    }
}