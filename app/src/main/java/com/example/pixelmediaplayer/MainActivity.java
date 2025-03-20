// MainActivity.java
package com.example.pixelmediaplayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SignalRService signalRService;
    private static final String SERVER_URL = "https://your-server-url/advertisementHub";
    private static final String SCREEN_ID = "ScreenID_123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signalRService = new SignalRService(SERVER_URL);
        signalRService.start();

        signalRService.handshake()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(response -> {
                Log.d(TAG, "Handshake response: " + response);
                registerScreen();
            }, throwable -> {
                Log.e(TAG, "Handshake error", throwable);
            });

        signalRService.onScheduleVideo(args -> {
            // Handle the ScheduleVideo message
            Log.d(TAG, "Received ScheduleVideo message: " + Arrays.toString(args));
        });

        // Start the VideoPlayerActivity
        Intent intent = new Intent(this, VideoPlayerActivity.class);
        startActivity(intent);
    }

    private void registerScreen() {
        signalRService.registerScreen(SCREEN_ID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(response -> {
                Log.d(TAG, "Screen registered: " + response);
                requestScheduleUpdate();
            }, throwable -> {
                Log.e(TAG, "Register screen error", throwable);
            });
    }

    private void requestScheduleUpdate() {
        signalRService.sendScheduleUpdate(SCREEN_ID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(response -> {
                Log.d(TAG, "Schedule update requested: " + response);
            }, throwable -> {
                Log.e(TAG, "Schedule update error", throwable);
            });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        signalRService.stop();
    }
}