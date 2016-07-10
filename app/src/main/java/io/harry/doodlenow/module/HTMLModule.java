package io.harry.doodlenow.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.harry.doodlenow.wrapper.JsoupWrapper;

@Module
public class HTMLModule {
    @Provides @Singleton
    public JsoupWrapper provideJsoupWrapper() {
        return new JsoupWrapper();
    }
}
