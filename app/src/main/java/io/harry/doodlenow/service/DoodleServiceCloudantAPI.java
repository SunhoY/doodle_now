package io.harry.doodlenow.service;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.harry.doodlenow.api.DoodleApi;
import io.harry.doodlenow.model.cloudant.CloudantDocument;
import io.harry.doodlenow.model.cloudant.CloudantResponse;
import io.harry.doodlenow.model.Doodle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoodleServiceCloudantAPI implements DoodleService {
    DoodleApi doodleApi;

    public DoodleServiceCloudantAPI(DoodleApi doodleApi) {
        this.doodleApi = doodleApi;
    }

    @Override
    public void retrieveDoodles(final ServiceCallback<List<Doodle>> serviceCallback) {
        Call<CloudantResponse<Doodle>> call = doodleApi.getAllDoodles();
        call.enqueue(new Callback<CloudantResponse<Doodle>>() {
            @Override
            public void onResponse(Call<CloudantResponse<Doodle>> call, Response<CloudantResponse<Doodle>> response) {
                if(!response.isSuccessful()) {
                    return;
                }

                List<Doodle> doodles = parseCloudantResponse(response);

                serviceCallback.onSuccess(doodles);
            }

            @Override
            public void onFailure(Call<CloudantResponse<Doodle>> call, Throwable t) {}
        });
    }

    @Override
    public void createDoodle(Doodle doodle, final ServiceCallback<String> serviceCallback) {
        Call<Doodle> postDoodleCall = doodleApi.postDoodle(doodle);
        postDoodleCall.enqueue(new Callback<Doodle>() {
            @Override
            public void onResponse(Call<Doodle> call, Response<Doodle> response) {
                if(!response.isSuccessful()) {
                    return;
                }
                serviceCallback.onSuccess(response.body()._id);
            }

            @Override
            public void onFailure(Call<Doodle> call, Throwable t) {

            }
        });
    }

    @Override
    public void retrieveDoodle(String doodleId, final ServiceCallback<Doodle> serviceCallback) {
        Call<Doodle> call = doodleApi.getDoodle(doodleId);
        call.enqueue(new Callback<Doodle>() {
            @Override
            public void onResponse(Call<Doodle> call, Response<Doodle> response) {
                if(!response.isSuccessful()) {
                    return;
                }

                serviceCallback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Doodle> call, Throwable t) {

            }
        });
    }

    public void updateDoodle(Doodle doodle, final ServiceCallback<Void> serviceCallback) {
        Call<Void> call = doodleApi.putDoodle(doodle._id, doodle);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!response.isSuccessful()) {
                    return;
                }

                serviceCallback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    @NonNull
    private <T> List<T> parseCloudantResponse(Response<CloudantResponse<T>> response) {
        CloudantResponse<T> body = response.body();
        List<CloudantDocument<T>> documents = body.rows;
        List<T> result = new ArrayList<>();

        for(CloudantDocument<T> document : documents) {
            result.add(document.doc);
        }
        return result;
    }
}
