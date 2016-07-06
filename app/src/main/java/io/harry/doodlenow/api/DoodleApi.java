package io.harry.doodlenow.api;

import io.harry.doodlenow.model.cloudant.CloudantResponse;
import io.harry.doodlenow.model.Doodle;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DoodleApi {
    @GET("/doodles/_all_docs?include_docs=true")
    Call<CloudantResponse<Doodle>> getAllDoodles();

    @POST("/doodles")
    Call<Void> postDoodle(@Body String content);
}
