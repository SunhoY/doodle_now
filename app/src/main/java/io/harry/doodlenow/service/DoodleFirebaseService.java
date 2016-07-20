package io.harry.doodlenow.service;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.harry.doodlenow.database.DatabaseReferenceWrapper;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.serviceinterface.IDoodleService;

public class DoodleFirebaseService implements IDoodleService{

    DatabaseReferenceWrapper databaseReferenceWrapper;

    public DoodleFirebaseService(DatabaseReferenceWrapper databaseReferenceWrapper) {
        this.databaseReferenceWrapper = databaseReferenceWrapper;
    }

    @Override
    public void saveDoodle(Doodle doodle, ServiceCallback<Void> serviceCallback) {
        DatabaseReference databaseReference = databaseReferenceWrapper.getDatabaseReference("doodle-now");
//        String doodleKey = databaseReference.child("doodles").push().getKey();
        Map<String, Object> map = new HashMap<>();
        map.put("doodles", new HashMap<String, Object>());
        map.put("groups", new HashMap<String, Object>());
        databaseReference.setValue(map);
//        databaseReference.child("doodles").child(doodleKey).setValue(doodle);
    }

    @Override
    public void getDoodles(long from, long to, ServiceCallback<List<Doodle>> serviceCallback) {

    }
}
