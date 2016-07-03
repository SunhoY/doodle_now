package io.harry.doodlenow.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import static org.mockito.Mockito.mock;

@Module
public class TestNetworkModule {
    @Provides @Singleton
    OkHttpClient provideOkHttpClient() {
        return mock(OkHttpClient.class);
    }

    @Provides @Singleton
    Retrofit provideRetrofit() {
        return mock(Retrofit.class);
    }
}