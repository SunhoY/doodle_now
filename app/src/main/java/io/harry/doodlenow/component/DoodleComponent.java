package io.harry.doodlenow.component;

import javax.inject.Singleton;

import dagger.Component;
import io.harry.doodlenow.activity.CreateDoodleActivity;
import io.harry.doodlenow.activity.DoodleActivity;
import io.harry.doodlenow.activity.LandingActivity;
import io.harry.doodlenow.adapter.DoodleListAdapter;
import io.harry.doodlenow.adapter.DoodlePagerAdapter;
import io.harry.doodlenow.background.DoodlePostService;
import io.harry.doodlenow.chrometab.ChromeTabHelper;
import io.harry.doodlenow.fragment.DoodleListFragment;
import io.harry.doodlenow.module.DoodleModule;
import io.harry.doodlenow.module.FirebaseModule;
import io.harry.doodlenow.module.HTMLModule;
import io.harry.doodlenow.module.PicassoModule;

@Singleton
@Component(modules = {
        DoodleModule.class,
        HTMLModule.class,
        PicassoModule.class,
        FirebaseModule.class,
    }
)
public interface DoodleComponent {
    void inject(LandingActivity landingActivity);

    void inject(DoodlePostService doodlePostService);

    void inject(DoodleListAdapter doodleListAdapter);

    void inject(DoodleListFragment doodleListFragment);

    void inject(DoodlePagerAdapter doodlePagerAdapter);

    void inject(DoodleActivity doodleActivity);

    void inject(CreateDoodleActivity createDoodleActivity);

    void inject(ChromeTabHelper chromeTabHelper);
}
