package io.harry.doodlenow.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.harry.doodlenow.firebase.FirebaseHelperWrapper;

@Module
public class FirebaseModule {
    @Provides @Singleton
    public FirebaseHelperWrapper provideFirebaseHelperWrapper() {
        return new FirebaseHelperWrapper();
    }
}
