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
import com.example.buzzblitz_android_cliente.Adapters.ForumPostAdapter;
import com.example.buzzblitz_android_cliente.Models.Comentario;
import com.example.buzzblitz_android_cliente.R;
import com.example.buzzblitz_android_cliente.RetrofitClient;
import com.example.buzzblitz_android_cliente.Services.ForumBuzzBlitzService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForumActivity extends BaseActivity {
    private ForumPostAdapter adapter;
    private List<Comentario> comentarios = new ArrayList<>();
    private String temaActual = "general";
    private String currentUserId;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable comentariosUpdater;
    private int lastComentariosCount = 0;
    private boolean isInForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        crearCanalDeNotificaciones();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        currentUserId = sharedPreferences.getString("currentUserId", "");

        String temaIntent = getIntent().getStringExtra("tema");
        if (temaIntent != null && !temaIntent.isEmpty()) {
            temaActual = temaIntent;
        }

        TextView tvUserIdCorner = findViewById(R.id.tvUserIdCorner);
        tvUserIdCorner.setText(currentUserId);

        RecyclerView rvForumPosts = findViewById(R.id.rvForumPosts);
        rvForumPosts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ForumPostAdapter(comentarios, new ForumPostAdapter.OnForumPostActionListener() {
            @Override
            public void onEdit(int position, String mensaje) {
                mostrarDialogoEditar(position, mensaje);
            }
            @Override
            public void onDelete(int position) {
                eliminarComentario(position);
            }
        });
        rvForumPosts.setAdapter(adapter);

        FloatingActionButton fabNewPost = findViewById(R.id.fabNewPost);
        fabNewPost.setOnClickListener(v -> mostrarDialogoNuevoComentario());

        cargarComentarios();
        iniciarActualizacionComentarios();
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

    private void cargarComentarios() {
        ForumBuzzBlitzService forumService = RetrofitClient.getForumService();
        forumService.getComentarios(temaActual).enqueue(new Callback<List<Comentario>>() {
            @Override
            public void onResponse(Call<List<Comentario>> call, Response<List<Comentario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean hayNuevo = response.body().size() > comentarios.size();
                    comentarios.clear();
                    comentarios.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    if (hayNuevo && comentarios.size() > 0) {
                        Comentario ultimo = comentarios.get(comentarios.size() - 1);
                        if (!ultimo.getAutor().equals(currentUserId)) {
                            mostrarNotificacionNuevoComentario(ultimo.getAutor(), ultimo.getContenido());
                        }
                    }
                } else {
                    Toast.makeText(ForumActivity.this, "Error al cargar comentarios", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Comentario>> call, Throwable t) {
                Toast.makeText(ForumActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void publicarComentario(String contenido) {
        ForumBuzzBlitzService forumService = RetrofitClient.getForumService();
        Comentario comentario = new Comentario();
        comentario.setAutor(currentUserId);
        comentario.setContenido(contenido);
        forumService.publicarComentario(comentario, temaActual).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                cargarComentarios();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ForumActivity.this, "Error al publicar comentario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eliminarComentario(int index) {
        ForumBuzzBlitzService forumService = RetrofitClient.getForumService();
        forumService.eliminarComentario(temaActual, index).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                cargarComentarios();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ForumActivity.this, "Error al eliminar comentario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editarComentario(int index, String nuevoContenido) {
        ForumBuzzBlitzService forumService = RetrofitClient.getForumService();
        forumService.editarComentario(temaActual, index, nuevoContenido).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                cargarComentarios();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ForumActivity.this, "Error al editar comentario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoNuevoComentario() {
        EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Nuevo comentario")
                .setView(input)
                .setPositiveButton("Publicar", (dialog, which) -> {
                    String contenido = input.getText().toString().trim();
                    if (!contenido.isEmpty()) {
                        publicarComentario(contenido);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoEditar(int index, String contenidoActual) {
        EditText input = new EditText(this);
        input.setText(contenidoActual);

        new AlertDialog.Builder(this)
                .setTitle("Editar comentario")
                .setView(input)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nuevoContenido = input.getText().toString().trim();
                    if (!nuevoContenido.isEmpty()) {
                        editarComentario(index, nuevoContenido);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void iniciarActualizacionComentarios() {
        comentariosUpdater = new Runnable() {
            @Override
            public void run() {
                cargarComentarios();
                handler.postDelayed(this, 5000);
            }
        };
        handler.post(comentariosUpdater);
    }

    private void crearCanalDeNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "forum_channel";
            CharSequence nombre = "Nuevos comentarios en el foro";
            String descripcion = "Notificaciones para nuevos comentarios en el foro";
            int importancia = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel canal = new NotificationChannel(channelId, nombre, importancia);
            canal.setDescription(descripcion);

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(canal);
            }
        }
    }

    private void mostrarNotificacionNuevoComentario(String autor, String contenido) {
        if (isInForeground) return;

        String channelId = "forum_channel";
        int notificationId = (int) System.currentTimeMillis();

        Intent intent = new Intent(this, ForumActivity.class);
        intent.putExtra("tema", temaActual);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        RemoteViews customLayout = new RemoteViews(getPackageName(), R.layout.custom_notification);
        customLayout.setTextViewText(R.id.tvTitle, "Nuevo comentario en el foro");
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
        if (comentariosUpdater != null) {
            handler.removeCallbacks(comentariosUpdater);
        }
    }
}