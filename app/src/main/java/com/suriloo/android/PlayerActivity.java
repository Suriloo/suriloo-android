package com.suriloo.android;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.suriloo.android.home.Content;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class PlayerActivity extends AppCompatActivity {

    private static final String TAG = "PlayerActivity";

    private ImageView coverImage;
    private TextView trackTitle, trackAuthor;
    private SeekBar seekBar;
    private ImageButton playPauseBtn, prevBtn, nextBtn, closeBtn;

    private MediaPlayer mediaPlayer;
    private List<Content> playlist;
    private int currentPosition;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        coverImage = findViewById(R.id.player_cover_image);
        trackTitle = findViewById(R.id.player_track_title);
        trackAuthor = findViewById(R.id.player_track_author);
        seekBar = findViewById(R.id.player_seek_bar);
        playPauseBtn = findViewById(R.id.player_play_pause_btn);
        prevBtn = findViewById(R.id.player_prev_btn);
        nextBtn = findViewById(R.id.player_next_btn);
        closeBtn = findViewById(R.id.player_close_btn);

        playlist = (List<Content>) getIntent().getSerializableExtra("playlist");
        currentPosition = getIntent().getIntExtra("position", 0);

        if (playlist != null && !playlist.isEmpty()) {
            loadTrack(currentPosition);
        }

        playPauseBtn.setOnClickListener(v -> togglePlayPause());
        nextBtn.setOnClickListener(v -> playNext());
        prevBtn.setOnClickListener(v -> playPrevious());
        closeBtn.setOnClickListener(v -> finish()); // Close the activity
    }

    private void loadTrack(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        currentPosition = position;
        Content currentContent = playlist.get(currentPosition);

        setupUI(currentContent);
        prepareMediaPlayer(currentContent);
    }

    private void setupUI(Content content) {
        trackTitle.setText(content.getTitle());
        trackAuthor.setText(content.getAuthor());

        if (content.getImageUrl() != null) {
            String fullImageUrl = ApiClient.BASE_URL + content.getImageUrl();
            Glide.with(this).load(fullImageUrl).placeholder(R.drawable.main_logo).into(coverImage);
        }
    }

    private void prepareMediaPlayer(Content content) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(content.getStreamUrl());
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e(TAG, "MediaPlayer Error: what=" + what + ", extra=" + extra);
                return true;
            });
            mediaPlayer.prepareAsync();

            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                playPauseBtn.setImageResource(R.drawable.ic_pause);
                seekBar.setMax(mediaPlayer.getDuration());
                startUpdatingSeekBar();
            });

            mediaPlayer.setOnCompletionListener(mp -> playNext());

        } catch (IOException e) {
            Log.e(TAG, "MediaPlayer IOException", e);
        }
    }

    private void togglePlayPause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playPauseBtn.setImageResource(R.drawable.ic_play_arrow);
        } else if (mediaPlayer != null) {
            mediaPlayer.start();
            playPauseBtn.setImageResource(R.drawable.ic_pause);
        }
    }

    private void playNext() {
        currentPosition = (currentPosition + 1) % playlist.size();
        loadTrack(currentPosition);
    }

    private void playPrevious() {
        currentPosition = (currentPosition - 1 + playlist.size()) % playlist.size();
        loadTrack(currentPosition);
    }

    private void startUpdatingSeekBar() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacksAndMessages(null);
    }
}
