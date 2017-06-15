package com.example.lex.torotest;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import im.ene.toro.Toro;
import im.ene.toro.exoplayer.ExoVideoView;
import im.ene.toro.exoplayer.Media;

public class MainActivity extends AppCompatActivity {
    private ExoVideoView toro;

    private Button start;
    private Button close;

    private void assignViews() {
        toro = (ExoVideoView) findViewById(R.id.demo_video_view);
        start = (Button) findViewById(R.id.start);
        close = (Button) findViewById(R.id.close);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();
        toro.releasePlayer();
        toro.setMedia(new Media(Uri.parse("https://staging-static.life.ru/posts/2017/01/961484/video/71585449ccb21f25ec65047e024da435.mp4")));
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                toro.seekTo(0);
                toro.start();
            }
        });
    }

}
