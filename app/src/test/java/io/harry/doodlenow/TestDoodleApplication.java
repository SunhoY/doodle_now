package io.harry.doodlenow;

import org.robolectric.TestLifecycleApplication;

import java.lang.reflect.Method;

import io.harry.doodlenow.component.DaggerTestDoodleComponent;
import io.harry.doodlenow.component.DoodleComponent;

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
        doodleComponent = DaggerTestDoodleComponent.builder()
                .build();
    }

    @Override
    public void afterTest(Method method) {

    }

    public DoodleComponent getDoodleComponent() {
        return doodleComponent;
    }
}
