package com.example.buzzblitz_android_cliente.Services;

import com.example.buzzblitz_android_cliente.Models.Comentario;
import com.example.buzzblitz_android_cliente.Models.UltimoComentarioDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Set;

public interface ForumBuzzBlitzService {
    @GET("foro/getTemas")
    Call<Set<String>> getTemas();

    @GET("foro/getComentarios/{tema}")
    Call<List<Comentario>> getComentarios(@Path("tema") String tema);

    @POST("foro/publicarComentario")
    Call<Void> publicarComentario(@Body Comentario comentario, @Query("tema") String tema);

    @POST("foro/crearTema")
    Call<Void> crearTema(@Query("nombre") String nombre);

    @DELETE("foro/eliminarComentario")
    Call<Void> eliminarComentario(@Query("tema") String tema, @Query("index") int index);

    @PUT("foro/editarComentario")
    Call<Void> editarComentario(@Query("tema") String tema, @Query("index") int index, @Query("nuevoContenido") String nuevoContenido);

    @PUT("foro/votarComentario")
    Call<Void> votarComentario(@Query("tema") String tema, @Query("index") int index, @Query("positivo") boolean positivo);

    @GET("foro/ultimosComentarios")
    Call<List<UltimoComentarioDTO>> getUltimosComentarios();
}