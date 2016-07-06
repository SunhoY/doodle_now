package io.harry.doodlenow.api;

import io.harry.doodlenow.model.cloudant.CloudantResponse;
import io.harry.doodlenow.model.Doodle;
import retrofit2.Call;
import retrofit2.http.GET;

public interface DoodleApi {
    @GET("simpledocument")
    Call<CloudantResponse<Doodle>> getAllDoodles();
}
