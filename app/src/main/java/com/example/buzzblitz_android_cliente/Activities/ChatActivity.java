package com.example.buzzblitz_android_cliente.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RemoteViews;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.buzzblitz_android_cliente.Adapters.MessageAdapter;
import com.example.buzzblitz_android_cliente.Models.Mensaje;
import com.example.buzzblitz_android_cliente.R;
import com.example.buzzblitz_android_cliente.RetrofitClient;
import com.example.buzzblitz_android_cliente.Services.ChatBuzzBlitzService;
import com.airbnb.lottie.LottieAnimationView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends BaseActivity {
    private MessageAdapter adapter;
    private List<Mensaje> mensajes = new ArrayList<>();
    private String canalActual = "general";
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable mensajeUpdater;
    private String currentUserId;
    private String chatUserId;
    private int lastMensajesCount = 0;
    private boolean isInForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        crearCanalDeNotificaciones();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        currentUserId = sharedPreferences.getString("currentUserId", "");

        // --- Lógica para chat privado ---
        chatUserId = getIntent().getStringExtra("chatUserId");
        if (chatUserId != null && !chatUserId.isEmpty()) {
            if (currentUserId.compareTo(chatUserId) < 0) {
                canalActual = currentUserId + "_" + chatUserId;
            } else {
                canalActual = chatUserId + "_" + currentUserId;
            }
        } else {
            canalActual = "general";
        }

        TextView tvUserIdCorner = findViewById(R.id.tvUserIdCorner);
        tvUserIdCorner.setText(currentUserId);

        RecyclerView rvChatMessages = findViewById(R.id.rvChatMessages);
        rvChatMessages.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageAdapter(mensajes, new MessageAdapter.OnMessageActionListener() {
            @Override
            public void onEdit(int position, String mensaje) {
                mostrarDialogoEditar(position, mensaje);
            }
            @Override
            public void onDelete(int position) {
                eliminarMensaje(position);
            }
        });
        rvChatMessages.setAdapter(adapter);

        cargarMensajes();

        EditText etMessageInput = findViewById(R.id.etMessageInput);
        LottieAnimationView btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(v -> {
            String mensaje = etMessageInput.getText().toString().trim();
            if (!mensaje.isEmpty()) {
                enviarMensaje(mensaje);
                etMessageInput.setText("");
            } else {
                Toast.makeText(this, "El mensaje no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
        });

        iniciarActualizacionMensajes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInForeground = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isInForeground = false;
    }

    private void cargarMensajes() {
        ChatBuzzBlitzService chatService = RetrofitClient.getChatService();
        chatService.getMensajes(canalActual).enqueue(new Callback<List<Mensaje>>() {
            @Override
            public void onResponse(Call<List<Mensaje>> call, Response<List<Mensaje>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Mensaje> nuevosMensajes = response.body();
                    if (!isInForeground && nuevosMensajes.size() > lastMensajesCount && nuevosMensajes.size() > 0) {
                        Mensaje ultimo = nuevosMensajes.get(nuevosMensajes.size() - 1);
                        if (!ultimo.getAutor().equals(currentUserId)) {
                            mostrarNotificacionNuevoMensaje(ultimo.getAutor(), ultimo.getContenido());
                        }
                    }
                    lastMensajesCount = nuevosMensajes.size();

                    mensajes.clear();
                    mensajes.addAll(nuevosMensajes);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Mensaje>> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Error al cargar mensajes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enviarMensaje(String contenido) {
        ChatBuzzBlitzService chatService = RetrofitClient.getChatService();
        Mensaje mensaje = new Mensaje();
        mensaje.setAutor(currentUserId);
        mensaje.setContenido(contenido);
        chatService.enviarMensaje(mensaje, canalActual).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                cargarMensajes();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Error al enviar mensaje", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eliminarMensaje(int index) {
        ChatBuzzBlitzService chatService = RetrofitClient.getChatService();
        chatService.eliminarMensaje(canalActual, index).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                cargarMensajes();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Error al eliminar mensaje", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editarMensaje(int index, String nuevoMensaje) {
        ChatBuzzBlitzService chatService = RetrofitClient.getChatService();
        chatService.editarMensaje(canalActual, index, nuevoMensaje).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                cargarMensajes();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Error al editar mensaje", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoEditar(int index, String mensajeActual) {
        EditText input = new EditText(this);
        input.setText(mensajeActual);

        new AlertDialog.Builder(this)
                .setTitle("Editar mensaje")
                .setView(input)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nuevoMensaje = input.getText().toString().trim();
                    if (!nuevoMensaje.isEmpty()) {
                        editarMensaje(index, nuevoMensaje);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void iniciarActualizacionMensajes() {
        mensajeUpdater = new Runnable() {
            @Override
            public void run() {
                cargarMensajes();
                handler.postDelayed(this, 5000);
            }
        };
        handler.post(mensajeUpdater);
    }

    private void crearCanalDeNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "chat_channel";
            CharSequence nombre = "Mensajes de Chat";
            String descripcion = "Notificaciones para nuevos mensajes de chat";
            int importancia = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel canal = new NotificationChannel(channelId, nombre, importancia);
            canal.setDescription(descripcion);

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(canal);
            }
        }
    }

    private void mostrarNotificacionNuevoMensaje(String autor, String contenido) {
        if (isInForeground) return;

        String channelId = "chat_channel";
        int notificationId = (int) System.currentTimeMillis();

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chatUserId", chatUserId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        RemoteViews customLayout = new RemoteViews(getPackageName(), R.layout.custom_notification);
        customLayout.setTextViewText(R.id.tvTitle, "Nuevo mensaje en el chat");
        customLayout.setTextViewText(R.id.tvMessage, autor + ": " + contenido);
        customLayout.setImageViewResource(R.id.ivIcon, R.drawable.ic_launcher);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(customLayout)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(notificationId, builder.build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mensajeUpdater != null) {
            handler.removeCallbacks(mensajeUpdater);
        }
    }
}