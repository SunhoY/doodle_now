package io.harry.doodlenow.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirebaseHelper {
    private final FirebaseDatabase firebaseDatabase;
    private final DatabaseReference databaseReference;

    public FirebaseHelper(String child) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(child);
    }

    public <T> String saveDoodle(T object) {
        String key = databaseReference.push().getKey();
        databaseReference.child(key).setValue(object);

        return key;
    }

    public Query getOrderByChildQuery(String field, long startAt, long endAt) {
        return databaseReference.orderByChild(field).startAt(startAt).endAt(endAt);
    }

    public void addSingleValueChangeListener(String key, ValueEventListener eventListener) {
        databaseReference.child(key).addListenerForSingleValueEvent(eventListener);
    }
}
