package io.harry.doodlenow.component;

import javax.inject.Singleton;

import dagger.Component;
import io.harry.doodlenow.activity.DoodleActivity;
import io.harry.doodlenow.activity.LandingActivity;
import io.harry.doodlenow.module.DoodleModule;
import io.harry.doodlenow.module.HTMLModule;
import io.harry.doodlenow.module.NetworkModule;

@Singleton
@Component(modules = {
        DoodleModule.class,
        NetworkModule.class,
        HTMLModule.class,
    }
)
public interface DoodleComponent {
    void inject(LandingActivity landingActivity);
    void inject(DoodleActivity doodleActivity);
}
