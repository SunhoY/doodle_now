package io.harry.doodlenow.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.harry.doodlenow.wrapper.PicassoWrapper;

@Module
public class PicassoModule {
    @Provides @Singleton
    public PicassoWrapper providePicassoWrapper() {
        return new PicassoWrapper();
    }
}
