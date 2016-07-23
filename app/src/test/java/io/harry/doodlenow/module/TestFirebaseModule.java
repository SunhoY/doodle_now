package io.harry.doodlenow.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.harry.doodlenow.firebase.FirebaseHelperWrapper;

import static org.mockito.Mockito.mock;

@Module
public class TestFirebaseModule {
    @Provides
    @Singleton
    public FirebaseHelperWrapper provideFirebaseHelperWrapper() {
        return mock(FirebaseHelperWrapper.class);
    }
}