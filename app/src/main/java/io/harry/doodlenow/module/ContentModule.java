package io.harry.doodlenow.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.harry.doodlenow.api.ContentApi;
import io.harry.doodlenow.service.ContentService;
import retrofit2.Retrofit;

@Module
public class ContentModule {
    @Provides @Singleton
    public ContentService provideContentService(ContentApi contentApi) {
        return new ContentService(contentApi);
    }

    @Provides @Singleton
    public ContentApi provideContentApi(Retrofit retrofit) {
        return retrofit.create(ContentApi.class);
    }
}
