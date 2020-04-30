package com.water.kaupool;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

public class IntroActivity extends AppCompatActivity {
    Handler handler_intro;
    Runnable run_into = new Runnable() {
        @Override
        public void run() {
            Intent i_Login = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(i_Login);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        handler_intro = new Handler();
        handler_intro.postDelayed(run_into, 3000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
