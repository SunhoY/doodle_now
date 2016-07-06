package io.harry.doodlenow;

import org.robolectric.TestLifecycleApplication;

import java.lang.reflect.Method;

import io.harry.doodlenow.component.DaggerDoodleComponent;
import io.harry.doodlenow.component.DoodleComponent;
import io.harry.doodlenow.module.TestContentModule;
import io.harry.doodlenow.module.TestNetworkModule;

public class TestDoodleApplication extends DoodleApplication implements TestLifecycleApplication {

    private DoodleComponent doodleComponent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void beforeTest(Method method) {
    }

    @Override
    public void prepareTest(Object test) {
        doodleComponent = DaggerDoodleComponent.builder()
                .contentModule(new TestContentModule())
                .networkModule(new TestNetworkModule("some backend", "some authentication"))
                .build();
    }

    @Override
    public void afterTest(Method method) {

    }

    public DoodleComponent getDoodleComponent() {
        return doodleComponent;
    }
}
