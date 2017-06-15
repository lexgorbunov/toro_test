package com.example.lex.torotest;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import im.ene.toro.PlaybackState;
import im.ene.toro.PlayerManager;
import im.ene.toro.ToroAdapter;
import im.ene.toro.ToroPlayer;

public class FeedAdapter extends ToroAdapter<FeedHolder> implements PlayerManager {
    private final static int TYPE_TEXT = 1;
    private final static int TYPE_VIDEO = 2;

    @Override
    public FeedHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        switch (viewType) {
            case TYPE_VIDEO:
                return new VideoFeedHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.holder_feed_video, parent, false));
            case TYPE_TEXT:
            default:
                return new TextFeedHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.holder_feed_item, parent, false));

        }
    }

    @Nullable
    @Override
    protected Object getItem(final int position) {
        return "https://staging-static.life.ru/posts/2017/01/961484/video/71585449ccb21f25ec65047e024da435.mp4";
    }

    @Override
    public int getItemCount() {
        return 50;
    }

    @Override
    public int getItemViewType(final int position) {
        return (position % 2 == 0) ? TYPE_VIDEO : TYPE_TEXT;
    }

    @Nullable
    @Override
    public ToroPlayer getPlayer() {
        return null;
    }

    @Override
    public void setPlayer(final ToroPlayer player) {

    }

    @Override
    public void onRegistered() {

    }

    @Override
    public void onUnregistered() {

    }

    @Override
    public void startPlayback() {

    }

    @Override
    public void pausePlayback() {

    }

    @Override
    public void stopPlayback() {

    }

    @Override
    public void saveVideoState(final String videoId, @Nullable final Long position, final long duration) {

    }

    @Override
    public void savePlaybackState(final String mediaId, @Nullable final Long position, final long duration) {

    }

    @Override
    public void restoreVideoState(final String videoId) {

    }

    @Override
    public void restorePlaybackState(final String mediaId) {

    }

    @Nullable
    @Override
    public PlaybackState getSavedState(final String videoId) {
        return null;
    }

    @Nullable
    @Override
    public PlaybackState getPlaybackState(final String mediaId) {
        return null;
    }

    @NonNull
    @Override
    public ArrayList<PlaybackState> getPlaybackStates() {
        return null;
    }

    @Override
    public void remove() throws Exception {

    }
}
