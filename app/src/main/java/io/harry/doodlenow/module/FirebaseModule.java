package io.harry.doodlenow.module;

import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.harry.doodlenow.database.DatabaseReferenceWrapper;

@Module
public class FirebaseModule {
    @Provides
    FirebaseDatabase provideFirebaseDatabase() {
        return FirebaseDatabase.getInstance();
    }

    @Provides @Singleton
    DatabaseReferenceWrapper provideDatabaseReferenceWrapper(FirebaseDatabase firebaseDatabase) {
        return new DatabaseReferenceWrapper(firebaseDatabase);
    }
}
