package com.example.pixelmediaplayer.utils;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class VideoUtils {
    private static final String TAG = "VideoUtils";

    public static String getVideoMetadata(String videoPath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);
        String title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        retriever.release();
        return "Title: " + title + ", Duration: " + duration + " ms";
    }

    public static File getOutputMediaFile(String fileName) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "PixelMediaPlayer");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        return new File(mediaStorageDir, fileName);
    }

    public static boolean deleteVideoFile(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.delete();
    }

    public static Uri getVideoUri(String filePath) {
        return Uri.fromFile(new File(filePath));
    }

    public static Uri getVideoUri(Context context, String videoPath) {
        File file = new File(videoPath);
        if (file.exists()) {
            return Uri.fromFile(file);
        } else {
            Log.e(TAG, "Video file not found: " + videoPath);
            return null;
        }
    }
}