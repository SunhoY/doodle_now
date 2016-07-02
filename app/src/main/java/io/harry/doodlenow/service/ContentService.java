package io.harry.doodlenow.service;

import java.util.List;

import javax.inject.Inject;

import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.api.ContentApi;
import io.harry.doodlenow.model.CloudantResponse;
import io.harry.doodlenow.model.Content;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContentService {

    @Inject
    ContentApi contentApi;

    {
        DoodleApplication.inject(this);
    }

    public void getContents(final ServiceCallback<List<Content>> serviceCallback) {
        Call<CloudantResponse<Content>> call = contentApi.getAllDoodles();
        call.enqueue(new Callback<CloudantResponse<Content>>() {
            @Override
            public void onResponse(Call<CloudantResponse<Content>> call, Response<CloudantResponse<Content>> response) {
                if(response.isSuccessful()) {
                    CloudantResponse<Content> body = response.body();
                    serviceCallback.onSuccess(body.rows);
                }
                else {
                    serviceCallback.onFailure("err!!!");
                }
            }

            @Override
            public void onFailure(Call<CloudantResponse<Content>> call, Throwable t) {
                serviceCallback.onFailure(t.getMessage());
            }
        });
    }
}
