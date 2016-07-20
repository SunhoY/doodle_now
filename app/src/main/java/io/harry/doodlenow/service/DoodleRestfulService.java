package io.harry.doodlenow.service;

import java.util.ArrayList;
import java.util.List;

import io.harry.doodlenow.api.DoodleApi;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.model.DoodleJson;
import io.harry.doodlenow.model.cloudant.CloudantQueryResponse;
import io.harry.doodlenow.model.cloudant.CreatedAtQuery;
import io.harry.doodlenow.serviceinterface.IDoodleService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoodleRestfulService implements IDoodleService {
    DoodleApi doodleApi;

    public DoodleRestfulService(DoodleApi doodleApi) {
        this.doodleApi = doodleApi;
    }

    public void saveDoodle(Doodle doodle, final ServiceCallback<Void> serviceCallback) {
        Call<Void> postDoodleCall = doodleApi.postDoodle(new DoodleJson(doodle));
        postDoodleCall.enqueue(new Callback<Void>() {
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

    public void getDoodles(long from, long to, final ServiceCallback<List<Doodle>> serviceCallback) {
        Call<CloudantQueryResponse> call = doodleApi.getDoodles(new CreatedAtQuery(from, to));
        call.enqueue(new Callback<CloudantQueryResponse>() {
            @Override
            public void onResponse(Call<CloudantQueryResponse> call, Response<CloudantQueryResponse> response) {
                if(!response.isSuccessful()) {
                    return;
                }

                CloudantQueryResponse body = response.body();
                List<DoodleJson> documents = body.docs;

                List<Doodle> doodles = new ArrayList<>();

                for(DoodleJson document : documents) {
                    doodles.add(new Doodle(document));
                }
                serviceCallback.onSuccess(doodles);
            }

            @Override
            public void onFailure(Call<CloudantQueryResponse> call, Throwable t) {

            }
        });
    }
}
