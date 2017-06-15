package com.example.lex.torotest;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import im.ene.toro.ToroAdapter;

public abstract class FeedHolder extends ToroAdapter.ViewHolder {
    public FeedHolder(final View itemView) {
        super(itemView);
    }

    public abstract void setContent(@NonNull final String content);

    @Override
    public void onAttachedToWindow() {

    }

    @Override
    public void onDetachedFromWindow() {

    }

    @Override
    public void bind(final RecyclerView.Adapter adapter, @Nullable final Object object) {
        setContent((String) object);
    }
}
