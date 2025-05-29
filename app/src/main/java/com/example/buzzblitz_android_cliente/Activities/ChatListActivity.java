package com.example.buzzblitz_android_cliente.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.buzzblitz_android_cliente.Adapters.UserChatAdapter;
import com.example.buzzblitz_android_cliente.Models.UltimoMensajeDTO;
import com.example.buzzblitz_android_cliente.R;
import com.example.buzzblitz_android_cliente.RetrofitClient;
import com.example.buzzblitz_android_cliente.Services.ChatBuzzBlitzService;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatListActivity extends BaseActivity {
    private UserChatAdapter adapterChatsPrivados;
    private List<UltimoMensajeDTO> chatsPrivados = new ArrayList<>();
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        currentUserId = sharedPreferences.getString("currentUserId", "");

        RecyclerView rvRecentChats = findViewById(R.id.rvRecentChats);
        rvRecentChats.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapterChatsPrivados = new UserChatAdapter(chatsPrivados, this::abrirChatConUsuario);
        rvRecentChats.setAdapter(adapterChatsPrivados);

        cargarUltimosMensajes();
    }

    private void cargarUltimosMensajes() {
        ChatBuzzBlitzService chatService = RetrofitClient.getChatService();
        chatService.getUltimosMensajes(currentUserId).enqueue(new Callback<List<UltimoMensajeDTO>>() {
            @Override
            public void onResponse(Call<List<UltimoMensajeDTO>> call, Response<List<UltimoMensajeDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    chatsPrivados.clear();
                    chatsPrivados.addAll(response.body());
                    adapterChatsPrivados.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<UltimoMensajeDTO>> call, Throwable t) {
                Toast.makeText(ChatListActivity.this, "Error al cargar chats privados", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void abrirChatConUsuario(String userId) {
        if (userId.equals(currentUserId)) {
            Toast.makeText(this, "No puedes chatear contigo mismo", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chatUserId", userId);
        startActivity(intent);
    }
}