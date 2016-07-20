package io.harry.doodlenow.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseReferenceWrapper {

    private FirebaseDatabase firebaseDatabase;

    public DatabaseReferenceWrapper(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    public DatabaseReference getDatabaseReference(String database) {
        return this.firebaseDatabase.getReference();
    }
}
