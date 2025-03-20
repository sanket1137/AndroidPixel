package com.example.pixelmediaplayer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleManager {
    private static final String TAG = "ScheduleManager";
    private static final String PREFS_NAME = "VideoSchedulePrefs";
    private SharedPreferences sharedPreferences;
    private Context context;
    private List<String> regularPlaylist;
    private List<String> premiumPlaylist;

    public ScheduleManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.regularPlaylist = new ArrayList<>();
        this.premiumPlaylist = new ArrayList<>();
    }

    public void addSchedule(String videoId, long playbackTime) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(videoId, playbackTime);
        editor.apply();
        Log.d(TAG, "Schedule added for videoId: " + videoId + " at time: " + playbackTime);
    }

    public void removeSchedule(String videoId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(videoId);
        editor.apply();
        Log.d(TAG, "Schedule removed for videoId: " + videoId);
    }

    public Map<String, Long> getAllSchedules() {
        Map<String, Long> schedules = new HashMap<>();
        for (String key : sharedPreferences.getAll().keySet()) {
            schedules.put(key, sharedPreferences.getLong(key, 0));
        }
        Log.d(TAG, "Retrieved all schedules: " + schedules);
        return schedules;
    }

    public List<String> getScheduledVideoIds() {
        return new ArrayList<>(sharedPreferences.getAll().keySet());
    }

    public void addVideoToPlaylist(String videoPath, boolean isPremium) {
        if (isPremium) {
            premiumPlaylist.add(videoPath);
        } else {
            regularPlaylist.add(videoPath);
        }
    }

    public void playVideos() {
        // Logic to play videos from the playlist
        for (String videoPath : regularPlaylist) {
            playVideo(videoPath);
        }
    }

    private void playVideo(String videoPath) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra("videoPath", videoPath);
        context.startActivity(intent);
    }
}