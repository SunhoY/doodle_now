package io.harry.doodlenow.module;

import javax.inject.Singleton;

import dagger.Provides;
import io.harry.doodlenow.api.ContentApi;
import io.harry.doodlenow.service.ContentService;
import retrofit2.Retrofit;

import static org.mockito.Mockito.mock;

public class TestContentModule {
    @Provides @Singleton
    public ContentService provideContentService() {
        return new ContentService(mock(ContentApi.class));
    }

    @Provides @Singleton
    public ContentApi provideContentApi(Retrofit retrofit) {
        return mock(ContentApi.class);
    }
}