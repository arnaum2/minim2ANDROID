package com.example.buzzblitz_android_cliente.Services;

import com.example.buzzblitz_android_cliente.Models.SolicitudAmistad;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface FriendBuzzBlitzService {
    @POST("amistades/enviar")
    Call<Void> enviarSolicitud(@Query("de") String de, @Query("para") String para);

    @PUT("amistades/aceptar")
    Call<Void> aceptarSolicitud(@Query("de") String de, @Query("para") String para);

    @DELETE("amistades/rechazar")
    Call<Void> rechazarSolicitud(@Query("de") String de, @Query("para") String para);

    @GET("amistades/amigos")
    Call<List<String>> getAmigos(@Query("usuario") String usuario);

    @GET("amistades/sonAmigos")
    Call<String> sonAmigos(@Query("u1") String u1, @Query("u2") String u2);

    @GET("amistades/pendientes/recibidas")
    Call<List<SolicitudAmistad>> getSolicitudesRecibidas(@Query("usuario") String usuario);

    @GET("amistades/pendientes/enviadas")
    Call<List<SolicitudAmistad>> getSolicitudesEnviadas(@Query("usuario") String usuario);

    @GET("amistades/usuarios")
    Call<List<String>> getTodosLosUsuarios();

    @GET("amistades/recientes")
    Call<List<String>> getUsuariosRecientes(@Query("usuario") String usuario);
}