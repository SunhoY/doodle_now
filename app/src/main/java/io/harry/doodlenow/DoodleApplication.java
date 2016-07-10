package io.harry.doodlenow;

import android.app.Application;

import io.harry.doodlenow.component.DaggerDoodleComponent;
import io.harry.doodlenow.component.DoodleComponent;
import io.harry.doodlenow.module.DoodleModule;
import io.harry.doodlenow.module.NetworkModule;

public class DoodleApplication extends Application {
    private DoodleComponent doodleComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        initComponent();
    }

    public DoodleComponent getDoodleComponent() {
        return doodleComponent;
    }

    private void initComponent() {
        String backendUrl = getString(R.string.backend_url);
        String authentication = getString(R.string.authentication_string);
        doodleComponent = DaggerDoodleComponent.builder()
                .doodleModule(new DoodleModule(this))
                .networkModule(new NetworkModule(backendUrl, authentication))
                .build();
    }
}
