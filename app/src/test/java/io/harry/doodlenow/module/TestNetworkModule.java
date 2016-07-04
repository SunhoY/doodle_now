package io.harry.doodlenow.module;

import dagger.Module;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import static org.mockito.Mockito.mock;

@Module
public class TestNetworkModule extends NetworkModule {

    public TestNetworkModule(String backendUrl, String authentication) {
        super(backendUrl, authentication);
    }

    @Override
    public OkHttpClient provideOkHttpClient() {
        return mock(OkHttpClient.class);
    }

    @Override
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl("http://someurl")
                .build();
    }
}