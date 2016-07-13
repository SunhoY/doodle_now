package io.harry.doodlenow.api;

import io.harry.doodlenow.model.DoodleJson;
import io.harry.doodlenow.model.cloudant.CloudantQueryResponse;
import io.harry.doodlenow.model.cloudant.CloudantResponse;
import io.harry.doodlenow.model.cloudant.CreatedAtQuery;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DoodleApi {
    @POST("/doodles")
    Call<Void> postDoodle(@Body DoodleJson doodleJson);

    @POST("/doodles/_find")
    Call<CloudantQueryResponse> getDoodles(@Body CreatedAtQuery createdAtQuery);
}
