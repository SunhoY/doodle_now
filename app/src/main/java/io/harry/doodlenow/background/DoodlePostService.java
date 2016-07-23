package io.harry.doodlenow.background;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.joda.time.DateTime;

import javax.inject.Inject;

import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.callback.JsoupCallback;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.service.DoodleService;
import io.harry.doodlenow.service.ServiceCallback;
import io.harry.doodlenow.view.DoodleIcon;
import io.harry.doodlenow.wrapper.JsoupWrapper;

public class DoodlePostService extends Service {
    @Inject
    DoodleService doodleService;
    @Inject
    JsoupWrapper jsoupWrapper;
    @Inject
    DoodleIcon doodleIcon;

    private BackgroundServiceBinder backgroundServiceBinder;

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

        backgroundServiceBinder = new BackgroundServiceBinder();

        return START_STICKY;
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

    private void saveDoodle(String title, String content, String imageUrl, String url) {
        doodleService.saveDoodle(new Doodle(title, content, url, imageUrl, new DateTime().getMillis()),
                new ServiceCallback<Void>() {
            @Override
            public void onSuccess(Void item) {
                showDoodled();
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    public void showDoodled() {
        doodleIcon.show();
        doodleIcon.animate()
                .setDuration(2000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        doodleIcon.hide();
                    }
                }).start();
    }
}
