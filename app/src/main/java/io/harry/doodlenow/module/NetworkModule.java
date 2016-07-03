package io.harry.doodlenow.module;

import android.util.Base64;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {
    private String backendUrl;
    private String authentication;

    public NetworkModule(String backendUrl, String authentication) {
        this.authentication = authentication;
        this.backendUrl = backendUrl;
    }

    @Provides @Singleton
    OkHttpClient provideOkHttpClient() {
        final String encodedAuthentication = new String(Base64.encode(authentication.getBytes(), Base64.DEFAULT)).trim();
        OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                builder.header("Authorization", "Basic " + encodedAuthentication);

                return chain.proceed(builder.build());
            }
        });

        return builder.build();
    }

    @Provides @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(backendUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
