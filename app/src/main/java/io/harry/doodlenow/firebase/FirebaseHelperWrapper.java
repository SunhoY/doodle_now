package io.harry.doodlenow.firebase;

public class FirebaseHelperWrapper {
    public FirebaseHelper getFirebaseHelper(String child) {
        return new FirebaseHelper(child);
    }
}
