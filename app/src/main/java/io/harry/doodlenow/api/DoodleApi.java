package io.harry.doodlenow.api;

import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.model.cloudant.CloudantResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DoodleApi {
    @GET("/doodles/_all_docs?include_docs=true")
    Call<CloudantResponse<Doodle>> getAllDoodles();

    @POST("/doodles")
    Call<Void> postDoodle(@Body Doodle doodle);

    @GET("/doodles/{doodleId}")
    Call<Doodle> getDoodle(@Path("doodleId") String doodleId);

    @PUT("/doodles/{doodleId}")
    Call<Void> putDoodle(@Path("doodleId") String doodleId, @Body Doodle doodle);
}
