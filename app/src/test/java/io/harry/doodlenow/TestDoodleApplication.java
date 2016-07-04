package io.harry.doodlenow;

import org.robolectric.TestLifecycleApplication;

import java.lang.reflect.Method;

import io.harry.doodlenow.component.ContentComponent;
import io.harry.doodlenow.component.DaggerContentComponent;
import io.harry.doodlenow.module.TestContentModule;
import io.harry.doodlenow.module.TestNetworkModule;

public class TestDoodleApplication extends DoodleApplication implements TestLifecycleApplication {

    private ContentComponent contentComponent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void beforeTest(Method method) {
    }

    @Override
    public void prepareTest(Object test) {
        contentComponent = DaggerContentComponent.builder()
                .contentModule(new TestContentModule())
                .networkModule(new TestNetworkModule("some backend", "some authentication"))
                .build();
    }

    @Override
    public void afterTest(Method method) {

    }

    public ContentComponent getContentComponent() {
        return contentComponent;
    }
}
