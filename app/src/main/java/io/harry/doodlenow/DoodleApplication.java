package io.harry.doodlenow;

import android.app.Application;

import io.harry.doodlenow.component.DaggerDoodleComponent;
import io.harry.doodlenow.component.DoodleComponent;
import io.harry.doodlenow.module.DoodleModule;

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
        doodleComponent = DaggerDoodleComponent.builder()
                .doodleModule(new DoodleModule(this))
                .build();
    }
}
