package com.example.pixelmediaplayer;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.util.VLCVideoLayout;
import java.util.ArrayList;

public class VideoPlayerActivity extends AppCompatActivity {
    private static final String TAG = "VideoPlayerActivity";
    private LibVLC libVLC;
    private MediaPlayer mediaPlayer;
    private VLCVideoLayout videoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoLayout = findViewById(R.id.video_layout);

        ArrayList<String> options = new ArrayList<>();
        libVLC = new LibVLC(this, options);
        mediaPlayer = new MediaPlayer(libVLC);

        mediaPlayer.attachViews(videoLayout, null, false, false);

        // Play local videos or stream from URL
        playVideo("file:///path/to/local/video.mp4");
    }

    private void playVideo(String videoPath) {
        Media media = new Media(libVLC, videoPath);
        mediaPlayer.setMedia(media);
        mediaPlayer.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        libVLC.release();
    }
}