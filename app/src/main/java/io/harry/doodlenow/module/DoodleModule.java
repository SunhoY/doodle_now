package io.harry.doodlenow.module;

import android.content.Context;

import java.util.ArrayList;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.harry.doodlenow.adapter.DoodleListAdapter;
import io.harry.doodlenow.api.DoodleApi;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.service.DoodleService;
import io.harry.doodlenow.view.DoodleIcon;
import io.harry.doodlenow.wrapper.DoodleListFragmentWrapper;
import io.harry.doodlenow.wrapper.DoodlePagerAdapterWrapper;
import retrofit2.Retrofit;

@Module
public class DoodleModule {
    private Context context;

    public DoodleModule(Context context) {
        this.context = context;
    }

    @Provides @Singleton
    public DoodleService provideDoodleService(DoodleApi doodleApi) {
        return new DoodleService(doodleApi);
    }

    @Provides @Singleton
    public DoodleApi provideDoodleApi(Retrofit retrofit) {
        return retrofit.create(DoodleApi.class);
    }

    @Provides @Singleton
    public DoodlePagerAdapterWrapper provideDoodlePagerAdapterWrapper() {
        return new DoodlePagerAdapterWrapper();
    }

    @Provides @Singleton
    public DoodleListFragmentWrapper provideDoodleListFragmentWrapper() {
        return new DoodleListFragmentWrapper();
    }

    @Provides
    public DoodleIcon provideDoodleIcon() {
        return new DoodleIcon(context);
    }

    @Provides
    public DoodleListAdapter provideDoodleListAdapter() {
        return new DoodleListAdapter(context, new ArrayList<Doodle>());
    }
}
