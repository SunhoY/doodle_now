package io.harry.doodlenow.component;

import javax.inject.Singleton;

import dagger.Component;
import io.harry.doodlenow.activity.DoodleActivityTest;
import io.harry.doodlenow.activity.LandingActivityTest;
import io.harry.doodlenow.module.TestDoodleModule;
import io.harry.doodlenow.module.TestHTMLModule;
import io.harry.doodlenow.module.TestNetworkModule;

@Singleton
@Component(modules = {
        TestDoodleModule.class,
        TestNetworkModule.class,
        TestHTMLModule.class,
    }
)
public interface TestDoodleComponent extends DoodleComponent {
    void inject(LandingActivityTest landingActivityTest);
    void inject(DoodleActivityTest doodleActivityTest);
}
