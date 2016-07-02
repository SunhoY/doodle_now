package io.harry.doodlenow.api;

import java.util.List;

import io.harry.doodlenow.model.CloudantResponse;
import io.harry.doodlenow.model.Content;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ContentApi {
    @GET("simpledocument")
    Call<CloudantResponse<Content>> getAllDoodles();
}
