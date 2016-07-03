package io.harry.doodlenow.component;

import dagger.Component;
import io.harry.doodlenow.activity.LandingActivity;
import io.harry.doodlenow.activity.LandingActivityTest;
import io.harry.doodlenow.module.TestContentModule;

@Component(modules = TestContentModule.class)
public interface TestContentComponent {
    void inject(LandingActivity landingActivity);
    void inject(LandingActivityTest landingActivityTest);
}
