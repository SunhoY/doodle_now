package io.harry.doodlenow.module;

import android.content.Context;

import java.util.ArrayList;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.harry.doodlenow.adapter.DoodleListAdapter;
import io.harry.doodlenow.api.DoodleApi;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.service.DoodleServiceCloudantAPI;
import retrofit2.Retrofit;

@Module
public class DoodleModule {
    private Context context;

    public DoodleModule(Context context) {
        this.context = context;
    }

    @Provides @Singleton
    public DoodleServiceCloudantAPI provideContentService(DoodleApi doodleApi) {
        return new DoodleServiceCloudantAPI(doodleApi);
    }

    @Provides @Singleton
    public DoodleApi provideContentApi(Retrofit retrofit) {
        return retrofit.create(DoodleApi.class);
    }

    @Provides
    public DoodleListAdapter provideDoodleListAdapter() {
        return new DoodleListAdapter(context, new ArrayList<Doodle>());
    }
}
