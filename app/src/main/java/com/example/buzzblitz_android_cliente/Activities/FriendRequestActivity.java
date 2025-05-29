package com.example.buzzblitz_android_cliente.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.buzzblitz_android_cliente.Adapters.FriendRequestAdapter;
import com.example.buzzblitz_android_cliente.Models.SolicitudAmistad;
import com.example.buzzblitz_android_cliente.R;
import com.example.buzzblitz_android_cliente.RetrofitClient;
import com.example.buzzblitz_android_cliente.Services.FriendBuzzBlitzService;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendRequestActivity extends BaseActivity {
    private FriendRequestAdapter adapterRecibidas;
    private FriendRequestAdapter adapterEnviadas;
    private List<SolicitudAmistad> solicitudesRecibidas = new ArrayList<>();
    private List<SolicitudAmistad> solicitudesEnviadas = new ArrayList<>();
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        crearCanalDeNotificaciones();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        currentUserId = sharedPreferences.getString("currentUserId", "");

        RecyclerView rvFriendRequests = findViewById(R.id.rvFriendRequests);
        rvFriendRequests.setLayoutManager(new LinearLayoutManager(this));
        adapterRecibidas = new FriendRequestAdapter(solicitudesRecibidas, new FriendRequestAdapter.OnFriendRequestActionListener() {
            @Override
            public void onAccept(int position) {
                aceptarSolicitud(position);
            }
            @Override
            public void onReject(int position) {
                rechazarSolicitud(position);
            }
        });
        rvFriendRequests.setAdapter(adapterRecibidas);

        RecyclerView rvSentRequests = findViewById(R.id.rvSentRequests);
        rvSentRequests.setLayoutManager(new LinearLayoutManager(this));
        adapterEnviadas = new FriendRequestAdapter(solicitudesEnviadas, null);
        rvSentRequests.setAdapter(adapterEnviadas);

        cargarSolicitudesRecibidas();
        cargarSolicitudesEnviadas();

        EditText etUserIdToAdd = findViewById(R.id.etUserIdToAdd);
        Button btnSendRequest = findViewById(R.id.btnSendRequest);
        btnSendRequest.setOnClickListener(v -> {
            String destinatarioId = etUserIdToAdd.getText().toString().trim();
            if (!destinatarioId.isEmpty()) {
                enviarSolicitud(destinatarioId);
            } else {
                Toast.makeText(this, "Introduce un ID de usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarSolicitudesRecibidas() {
        FriendBuzzBlitzService friendService = RetrofitClient.getFriendService();
        friendService.getSolicitudesRecibidas(currentUserId).enqueue(new Callback<List<SolicitudAmistad>>() {
            @Override
            public void onResponse(Call<List<SolicitudAmistad>> call, Response<List<SolicitudAmistad>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SolicitudAmistad> nuevasSolicitudes = response.body();
                    for (SolicitudAmistad solicitud : nuevasSolicitudes) {
                        if (!solicitudesRecibidas.contains(solicitud)) {
                            mostrarNotificacionPersonalizada(solicitud.getDe());
                        }
                    }
                    solicitudesRecibidas.clear();
                    solicitudesRecibidas.addAll(nuevasSolicitudes);
                    adapterRecibidas.notifyDataSetChanged();
                } else {
                    Toast.makeText(FriendRequestActivity.this, "Error al cargar solicitudes recibidas", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<SolicitudAmistad>> call, Throwable t) {
                Toast.makeText(FriendRequestActivity.this, "Error de conexión al cargar recibidas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarSolicitudesEnviadas() {
        FriendBuzzBlitzService friendService = RetrofitClient.getFriendService();
        friendService.getSolicitudesEnviadas(currentUserId).enqueue(new Callback<List<SolicitudAmistad>>() {
            @Override
            public void onResponse(Call<List<SolicitudAmistad>> call, Response<List<SolicitudAmistad>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    solicitudesEnviadas.clear();
                    solicitudesEnviadas.addAll(response.body());
                    adapterEnviadas.notifyDataSetChanged();
                } else {
                    Toast.makeText(FriendRequestActivity.this, "Error al cargar solicitudes enviadas", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<SolicitudAmistad>> call, Throwable t) {
                Toast.makeText(FriendRequestActivity.this, "Error de conexión al cargar enviadas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enviarSolicitud(String destinatarioId) {
        FriendBuzzBlitzService friendService = RetrofitClient.getFriendService();
        friendService.enviarSolicitud(currentUserId, destinatarioId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(FriendRequestActivity.this, "Solicitud enviada con éxito", Toast.LENGTH_SHORT).show();
                    cargarSolicitudesEnviadas();
                } else {
                    Toast.makeText(FriendRequestActivity.this, "Error al enviar la solicitud", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(FriendRequestActivity.this, "Error de conexión al enviar solicitud", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void aceptarSolicitud(int position) {
        SolicitudAmistad solicitud = solicitudesRecibidas.get(position);
        FriendBuzzBlitzService friendService = RetrofitClient.getFriendService();
        friendService.aceptarSolicitud(solicitud.getDe(), currentUserId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    solicitudesRecibidas.remove(position);
                    adapterRecibidas.notifyItemRemoved(position);
                    Toast.makeText(FriendRequestActivity.this, "Solicitud aceptada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FriendRequestActivity.this, "Error al aceptar solicitud", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(FriendRequestActivity.this, "Error de conexión al aceptar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void rechazarSolicitud(int position) {
        SolicitudAmistad solicitud = solicitudesRecibidas.get(position);
        FriendBuzzBlitzService friendService = RetrofitClient.getFriendService();
        friendService.rechazarSolicitud(solicitud.getDe(), currentUserId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    solicitudesRecibidas.remove(position);
                    adapterRecibidas.notifyItemRemoved(position);
                    Toast.makeText(FriendRequestActivity.this, "Solicitud rechazada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FriendRequestActivity.this, "Error al rechazar solicitud", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(FriendRequestActivity.this, "Error de conexión al rechazar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- Notificaciones ---
    private void crearCanalDeNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "friend_requests_channel";
            CharSequence nombre = "Solicitudes de Amistad";
            String descripcion = "Notificaciones para nuevas solicitudes de amistad";
            int importancia = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel canal = new NotificationChannel(channelId, nombre, importancia);
            canal.setDescription(descripcion);

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(canal);
            }
        }
    }

    private void mostrarNotificacionPersonalizada(String usuarioSolicitante) {
        String channelId = "friend_requests_channel";
        int notificationId = (int) System.currentTimeMillis();

        Intent intent = new Intent(this, FriendRequestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        RemoteViews customLayout = new RemoteViews(getPackageName(), R.layout.custom_notification);
        customLayout.setTextViewText(R.id.tvTitle, "Nueva solicitud de amistad");
        customLayout.setTextViewText(R.id.tvMessage, usuarioSolicitante + " te ha enviado una solicitud de amistad.");
        customLayout.setImageViewResource(R.id.ivIcon, R.drawable.ic_launcher);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(customLayout)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, builder.build());
    }
}