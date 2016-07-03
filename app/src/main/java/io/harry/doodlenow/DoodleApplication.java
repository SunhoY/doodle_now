package io.harry.doodlenow;

import android.app.Application;

import io.harry.doodlenow.component.ApplicationComponent;
import io.harry.doodlenow.component.DaggerApplicationComponent;
import io.harry.doodlenow.module.NetworkModule;

public class DoodleApplication extends Application {
    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        initComponent();
    }

    public ApplicationComponent getComponent() {
        return component;
    }

    private void initComponent() {
        String backendUrl = getString(R.string.backend_url);
        String authentication = getString(R.string.authentication_string);
        component = DaggerApplicationComponent.builder()
                .networkModule(new NetworkModule(backendUrl, authentication))
                .build();
    }
}
