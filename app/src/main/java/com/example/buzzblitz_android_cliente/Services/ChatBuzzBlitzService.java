package com.example.buzzblitz_android_cliente.Services;

import com.example.buzzblitz_android_cliente.Models.Mensaje;
import com.example.buzzblitz_android_cliente.Models.UltimoMensajeDTO;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Set;

public interface ChatBuzzBlitzService {
    @GET("chat/getChats")
    Call<Set<String>> getChats();

    @GET("chat/getMensajes/{chat}")
    Call<List<Mensaje>> getMensajes(@Path("chat") String chat);

    @POST("chat/enviarMensaje")
    Call<Void> enviarMensaje(@Body Mensaje mensaje, @Query("chat") String chat);

    @POST("chat/crearChat")
    Call<Void> crearChat(@Query("nombre") String nombre);

    @DELETE("chat/eliminarMensaje")
    Call<Void> eliminarMensaje(@Query("chat") String chat, @Query("index") int index);

    @PUT("chat/editarMensaje")
    Call<Void> editarMensaje(@Query("chat") String chat, @Query("index") int index, @Query("nuevoContenido") String nuevoContenido);

    @GET("chat/ultimosMensajes")
    Call<List<UltimoMensajeDTO>> getUltimosMensajes(@Query("usuario") String usuario);
}