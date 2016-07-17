package io.harry.doodlenow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import io.harry.doodlenow.R;
import io.harry.doodlenow.background.DoodlePostService;

public class SplashActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startService(new Intent(this, DoodlePostService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                SplashActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                startActivity(new Intent(SplashActivity.this, LandingActivity.class));
            }
        }, 1000);

    }
}
