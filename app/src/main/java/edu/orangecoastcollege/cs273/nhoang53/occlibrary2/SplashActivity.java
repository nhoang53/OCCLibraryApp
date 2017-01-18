package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    /**
     * Create SharePreferences splash
     * It will go to MainActivity after 5 seconds
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences splash = getSharedPreferences(Main2Activity.SPLASH_PREF, 0);
        SharedPreferences.Editor editor = splash.edit();
        editor.putInt("splash", 1); // 1 mean it already splash 1 times.
        editor.apply();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, Main2Activity.class));
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, 6000); // 6 sec
    }
}
