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

public class DoodleService {
    DoodleApi doodleApi;

    public DoodleService(DoodleApi doodleApi) {
        this.doodleApi = doodleApi;
    }

    public void getDoodles(final ServiceCallback<List<Doodle>> serviceCallback) {
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

    public void saveContent(String content, ServiceCallback<Void> serviceCallback) {

    }


}
