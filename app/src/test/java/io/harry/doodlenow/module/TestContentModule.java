package io.harry.doodlenow.module;

import io.harry.doodlenow.api.ContentApi;
import io.harry.doodlenow.service.ContentService;
import retrofit2.Retrofit;

import static org.mockito.Mockito.mock;

public class TestContentModule extends ContentModule {
    @Override
    public ContentService provideContentService(ContentApi contentApi) {
        return mock(ContentService.class);
    }

    @Override
    public ContentApi provideContentApi(Retrofit retrofit) {
        return mock(ContentApi.class);
    }
}