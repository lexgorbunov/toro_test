package com.example.lex.torotest;


import android.app.Application;

import im.ene.toro.Toro;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Toro.init(this);
    }
}
