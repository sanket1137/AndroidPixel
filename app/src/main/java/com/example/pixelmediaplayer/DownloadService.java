package com.example.pixelmediaplayer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class DownloadService extends Service {
    private static final String TAG = "DownloadService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String videoUrl = intent.getStringExtra("videoUrl");
        String videoPath = intent.getStringExtra("videoPath");

        new Thread(() -> downloadVideo(videoUrl, videoPath)).start();

        return START_NOT_STICKY;
    }

    private void downloadVideo(String videoUrl, String videoPath) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(videoUrl).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new RuntimeException("Failed to download video");

            File file = new File(videoPath);
            try (InputStream inputStream = response.body().byteStream();
                 FileOutputStream outputStream = new FileOutputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            Log.d(TAG, "Video downloaded: " + videoPath);
        } catch (Exception e) {
            Log.e(TAG, "Error downloading video", e);
        }
    }
}