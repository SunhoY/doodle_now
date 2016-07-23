package io.harry.doodlenow;

import android.content.res.Resources;

import org.robolectric.TestLifecycleApplication;

import java.lang.reflect.Method;

import io.harry.doodlenow.component.DaggerTestDoodleComponent;
import io.harry.doodlenow.component.DoodleComponent;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

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

    @Override
    public Resources getResources() {
        Resources resources = spy(super.getResources());
        when(resources.getInteger(R.integer.stand_up_starts_at)).thenReturn(10);
        when(resources.getInteger(R.integer.archive_duration)).thenReturn(7);

        return resources;
    }

    public DoodleComponent getDoodleComponent() {
        return doodleComponent;
    }
}
