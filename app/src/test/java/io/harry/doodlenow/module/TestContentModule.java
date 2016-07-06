package io.harry.doodlenow.module;

import io.harry.doodlenow.api.DoodleApi;
import io.harry.doodlenow.service.DoodleService;
import retrofit2.Retrofit;

import static org.mockito.Mockito.mock;

public class TestContentModule extends ContentModule {
    @Override
    public DoodleService provideContentService(DoodleApi doodleApi) {
        return mock(DoodleService.class);
    }

    @Override
    public DoodleApi provideContentApi(Retrofit retrofit) {
        return mock(DoodleApi.class);
    }
}