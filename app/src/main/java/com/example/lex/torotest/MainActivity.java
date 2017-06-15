package com.example.lex.torotest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.ads.interactivemedia.v3.api.AdDisplayContainer;
import com.google.ads.interactivemedia.v3.api.AdErrorEvent;
import com.google.ads.interactivemedia.v3.api.AdEvent;
import com.google.ads.interactivemedia.v3.api.AdsLoader;
import com.google.ads.interactivemedia.v3.api.AdsManagerLoadedEvent;
import com.google.ads.interactivemedia.v3.api.AdsRequest;
import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;
import com.google.ads.interactivemedia.v3.api.player.ContentProgressProvider;
import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate;

import im.ene.toro.Toro;
import im.ene.toro.exoplayer.PlayerCallback;
import im.ene.toro.exoplayer.internal.ExoMediaPlayer;

public class MainActivity extends AppCompatActivity {
    private RecyclerView list;

    private void assignViews() {
        list = (RecyclerView) findViewById(R.id.list);
        list.setAdapter(new FeedAdapter());
        list.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toro.register(list);
    }

    @Override
    protected void onPause() {
        Toro.unregister(list);
        super.onPause();
    }
}
