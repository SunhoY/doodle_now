package io.harry.doodlenow.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.harry.doodlenow.wrapper.PicassoWrapper;

import static org.mockito.Mockito.mock;

@Module
public class TestPicassoModule {
    @Provides @Singleton
    public PicassoWrapper providePicassoWrapper() {
        return mock(PicassoWrapper.class);
    }
}
