package io.harry.doodlenow;

import android.app.Application;

import io.harry.doodlenow.component.ContentComponent;
import io.harry.doodlenow.component.DaggerContentComponent;
import io.harry.doodlenow.module.NetworkModule;

public class DoodleApplication extends Application {
    private ContentComponent contentComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        initComponent();
    }

    public ContentComponent getContentComponent() {
        return contentComponent;
    }

    private void initComponent() {
        String backendUrl = getString(R.string.backend_url);
        String authentication = getString(R.string.authentication_string);
        contentComponent = DaggerContentComponent.builder()
                .networkModule(new NetworkModule(backendUrl, authentication))
                .build();
    }
}
