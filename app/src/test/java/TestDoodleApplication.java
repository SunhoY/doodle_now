import org.robolectric.TestLifecycleApplication;

import java.lang.reflect.Method;

import io.harry.doodlenow.DoodleApplication;

public class TestDoodleApplication extends DoodleApplication implements TestLifecycleApplication {

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public void beforeTest(Method method) {

    }

    @Override
    public void prepareTest(Object test) {

    }

    @Override
    public void afterTest(Method method) {

    }
}
