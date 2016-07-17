package io.harry.doodlenow.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import io.harry.doodlenow.background.DoodlePostService;

public class CatchDoodleActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                DoodlePostService service = ((DoodlePostService.BackgroundServiceBinder)iBinder).getService();

                service.postDoodle(intent.getStringExtra(Intent.EXTRA_TEXT));
                service.showDoodled();
                unbindService(this);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };

        bindService(new Intent(this, DoodlePostService.class),
                connection,BIND_AUTO_CREATE);

        finish();
    }
}
