package com.x1unix.cashlytics;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.x1unix.cashlytics.ui.wallet.WalletsActivity;

public class MainActivity extends AppCompatActivity {

    protected final int SPLASH_DISPLAY_LENGTH = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, WalletsActivity.class);
                MainActivity.this.startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                MainActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
