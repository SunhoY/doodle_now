package io.harry.doodlenow.component;

import javax.inject.Singleton;

import dagger.Component;
import io.harry.doodlenow.activity.LandingActivity;
import io.harry.doodlenow.module.ContentModule;
import io.harry.doodlenow.module.NetworkModule;

@Singleton
@Component(modules = {
        ContentModule.class,
        NetworkModule.class,
    }
)
public interface ApplicationComponent {
    void inject(LandingActivity target);
}
