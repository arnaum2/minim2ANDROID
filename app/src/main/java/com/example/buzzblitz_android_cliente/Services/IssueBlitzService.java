package com.example.buzzblitz_android_cliente.Services;
import com.example.buzzblitz_android_cliente.Models.Issue;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface IssueBlitzService {
    @POST("issues/addIssue")
    Call<Void> enviarIssue(@Body Issue issue);
}
