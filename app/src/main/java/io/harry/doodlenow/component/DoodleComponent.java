package io.harry.doodlenow.component;

import javax.inject.Singleton;

import dagger.Component;
import io.harry.doodlenow.module.ContentModule;
import io.harry.doodlenow.module.NetworkModule;
import io.harry.doodlenow.service.DoodleService;

@Singleton
@Component(modules = {
        ContentModule.class,
        NetworkModule.class,
    }
)
public interface DoodleComponent {
    DoodleService contentService();
}
