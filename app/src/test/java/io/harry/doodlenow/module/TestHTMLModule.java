package io.harry.doodlenow.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.harry.doodlenow.wrapper.JsoupWrapper;

import static org.mockito.Mockito.mock;

@Module
public class TestHTMLModule {
    @Provides
    @Singleton
    public JsoupWrapper provideJsoupWrapper() {
        return mock(JsoupWrapper.class);
    }

}