package io.harry.doodlenow;

import android.content.Context;
import android.util.Base64;

import java.io.IOException;

import dagger.Module;
import dagger.Provides;
import io.harry.doodlenow.activity.LandingActivity;
import io.harry.doodlenow.api.ContentApi;
import io.harry.doodlenow.service.ContentService;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(
        injects = {
                ContentService.class,
                LandingActivity.class,
        }
)
public class ApplicationModule {
    private Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    public ContentService provideContentService() {
        return new ContentService();
    }

    @Provides
    public ContentApi provideContentApi() {
        String authenticationString = context.getString(R.string.authentication_string);
        final String encodedString = new String(Base64.encode(authenticationString.getBytes(), Base64.DEFAULT));
        OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                builder.header("Authorization", "Basic " + encodedString.trim());

                return chain.proceed(builder.build());
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://zillaci.cloudant.com/doodles/_design/simpledocument/_view/")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ContentApi.class);
    }
}
