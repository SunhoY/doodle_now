package io.harry.doodlenow.component;

import javax.inject.Singleton;

import dagger.Component;
import io.harry.doodlenow.activity.CreateDoodleActivityTest;
import io.harry.doodlenow.activity.DoodleActivityTest;
import io.harry.doodlenow.activity.LandingActivityTest;
import io.harry.doodlenow.adapter.DoodleListAdapterTest;
import io.harry.doodlenow.adapter.DoodlePagerAdapterTest;
import io.harry.doodlenow.background.DoodlePostServiceTest;
import io.harry.doodlenow.fragment.DoodleListFragmentTest;
import io.harry.doodlenow.module.TestDoodleModule;
import io.harry.doodlenow.module.TestFirebaseModule;
import io.harry.doodlenow.module.TestHTMLModule;
import io.harry.doodlenow.module.TestNetworkModule;
import io.harry.doodlenow.module.TestPicassoModule;

@Singleton
@Component(modules = {
        TestDoodleModule.class,
        TestNetworkModule.class,
        TestHTMLModule.class,
        TestPicassoModule.class,
        TestFirebaseModule.class,
    }
)
public interface TestDoodleComponent extends DoodleComponent {
    void inject(LandingActivityTest landingActivityTest);

    void inject(DoodlePostServiceTest doodlePostServiceTest);

    void inject(DoodleListAdapterTest doodleListAdapterTest);

    void inject(DoodleListFragmentTest doodleListFragmentTest);

    void inject(DoodlePagerAdapterTest doodlePagerAdapterTest);

    void inject(DoodleActivityTest doodleActivityTest);

    void inject(CreateDoodleActivityTest createDoodleActivityTest);
}
