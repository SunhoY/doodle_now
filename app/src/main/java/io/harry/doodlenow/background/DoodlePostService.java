package io.harry.doodlenow.background;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import javax.inject.Inject;

import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.callback.JsoupCallback;
import io.harry.doodlenow.firebase.FirebaseHelper;
import io.harry.doodlenow.firebase.FirebaseHelperWrapper;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.model.DoodleJson;
import io.harry.doodlenow.view.DoodleIcon;
import io.harry.doodlenow.wrapper.JsoupWrapper;

public class DoodlePostService extends Service implements ValueEventListener {
    @Inject
    FirebaseHelperWrapper firebaseHelperWrapper;
    @Inject
    JsoupWrapper jsoupWrapper;
    @Inject
    DoodleIcon doodleIcon;

    private BackgroundServiceBinder backgroundServiceBinder;
    private FirebaseHelper firebaseHelper;
    private String createdDoodleKey;

    public class BackgroundServiceBinder extends Binder {
        public DoodlePostService getService() {
            return DoodlePostService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return backgroundServiceBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ((DoodleApplication) getApplication()).getDoodleComponent().inject(this);
        firebaseHelper = firebaseHelperWrapper.getFirebaseHelper("doodles");

        backgroundServiceBinder = new BackgroundServiceBinder();

        return START_STICKY;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.getKey().equals(createdDoodleKey)) {
            showDoodled();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public void postDoodle(final String url) {
        jsoupWrapper.getDocument(url, new JsoupCallback() {
            @Override
            public void onSuccess(String title, String content, String imageUrl) {
                saveDoodle(title, content, imageUrl, url);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    public void showDoodled() {
        doodleIcon.show();
        doodleIcon.animate()
                .setDuration(3000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        doodleIcon.hide();
                    }
                }).start();
    }

    private void saveDoodle(String title, String content, String imageUrl, String url) {
        Doodle doodle = new Doodle(title, content, url, imageUrl, new DateTime().getMillis());
        createdDoodleKey = firebaseHelper.saveDoodle(new DoodleJson(doodle));
        firebaseHelper.addSingleValueChangeListener(createdDoodleKey, this);
    }
}
