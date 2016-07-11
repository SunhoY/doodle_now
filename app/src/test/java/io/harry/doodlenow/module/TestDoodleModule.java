package io.harry.doodlenow.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.harry.doodlenow.adapter.DoodleListAdapter;
import io.harry.doodlenow.api.DoodleApi;
import io.harry.doodlenow.service.DoodleServiceCloudantAPI;

import static org.mockito.Mockito.mock;

@Module
public class TestDoodleModule {
    @Provides @Singleton
    public DoodleServiceCloudantAPI provideContentService() {
        return mock(DoodleServiceCloudantAPI.class);
    }

    @Provides @Singleton
    public DoodleApi provideContentApi() {
        return mock(DoodleApi.class);
    }

    @Provides @Singleton
    public DoodleListAdapter provideDoodleListAdapter() { return mock(DoodleListAdapter.class); }
}