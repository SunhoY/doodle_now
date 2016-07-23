package io.harry.doodlenow.firebase;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
    private final String child;
    private final FirebaseDatabase firebaseDatabase;
    private final DatabaseReference databaseReference;

    public FirebaseHelper(String child) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(child);
        this.child = child;
    }

    public void addChildEventListener(ChildEventListener listener) {
        databaseReference.addChildEventListener(listener);
    }

    public void removeChildEventListener(ChildEventListener listener) {
        databaseReference.removeEventListener(listener);
    }
}
