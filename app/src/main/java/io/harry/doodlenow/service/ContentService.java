package io.harry.doodlenow.service;

import java.util.List;

import io.harry.doodlenow.api.ContentApi;
import io.harry.doodlenow.model.CloudantResponse;
import io.harry.doodlenow.model.Content;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContentService {
    ContentApi contentApi;

    public ContentService(ContentApi contentApi) {
        this.contentApi = contentApi;
    }

    public void getContents(final ServiceCallback<List<Content>> serviceCallback) {
        Call<CloudantResponse<Content>> call = contentApi.getAllDoodles();
        call.enqueue(new Callback<CloudantResponse<Content>>() {
            @Override
            public void onResponse(Call<CloudantResponse<Content>> call, Response<CloudantResponse<Content>> response) {
                if(!response.isSuccessful()) {
                    return;
                }

                CloudantResponse<Content> body = response.body();
                serviceCallback.onSuccess(body.rows);
            }

            @Override
            public void onFailure(Call<CloudantResponse<Content>> call, Throwable t) {}
        });
    }

    public void saveContent(ServiceCallback<Void> serviceCallback) {

    }
}
