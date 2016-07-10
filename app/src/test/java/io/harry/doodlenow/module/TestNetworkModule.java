package io.harry.doodlenow.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

import static org.mockito.Mockito.mock;

@Module
public class TestNetworkModule {
    @Provides @Singleton
    public OkHttpClient provideOkHttpClient() {
        return mock(OkHttpClient.class);
    }
}