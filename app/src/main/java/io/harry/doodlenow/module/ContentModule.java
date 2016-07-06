package io.harry.doodlenow.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.harry.doodlenow.api.DoodleApi;
import io.harry.doodlenow.service.DoodleService;
import retrofit2.Retrofit;

@Module
public class ContentModule {
    @Provides @Singleton
    public DoodleService provideContentService(DoodleApi doodleApi) {
        return new DoodleService(doodleApi);
    }

    @Provides @Singleton
    public DoodleApi provideContentApi(Retrofit retrofit) {
        return retrofit.create(DoodleApi.class);
    }
}
