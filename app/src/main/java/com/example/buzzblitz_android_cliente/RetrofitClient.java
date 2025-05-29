package com.example.buzzblitz_android_cliente;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import com.example.buzzblitz_android_cliente.Services.FriendBuzzBlitzService;
import com.example.buzzblitz_android_cliente.Services.GameBuzzBlitzService;
import com.example.buzzblitz_android_cliente.Services.ChatBuzzBlitzService;
import com.example.buzzblitz_android_cliente.Services.ForumBuzzBlitzService;
import com.example.buzzblitz_android_cliente.Services.IssueBlitzService;
import com.google.gson.GsonBuilder;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static final String BASE_URL = "http://10.0.2.2:8080/dsaApp/";

    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(ScalarsConverterFactory.create()) // Para text/plain
                    .addConverterFactory(GsonConverterFactory.create())    // Para JSON
                    .build();
        }
        return retrofit;
    }

    public static GameBuzzBlitzService getApiService() {
        return getRetrofitInstance().create(GameBuzzBlitzService.class);
    }

    public static ChatBuzzBlitzService getChatService() {
        return getRetrofitInstance().create(ChatBuzzBlitzService.class);
    }

    public static ForumBuzzBlitzService getForumService() {
        return getRetrofitInstance().create(ForumBuzzBlitzService.class);
    }

    public static FriendBuzzBlitzService getFriendService() {
        return getRetrofitInstance().create(FriendBuzzBlitzService.class);
    }
    public static IssueBlitzService getIssueService() {
        return getRetrofitInstance().create(IssueBlitzService.class);
    }
}