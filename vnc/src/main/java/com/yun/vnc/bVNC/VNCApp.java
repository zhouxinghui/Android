package com.yun.vnc.bVNC;

import ebag.hd.base.BaseApp;

public class VNCApp extends BaseApp {

    private Database database;

    @Override
    public void onCreate() {
        super.onCreate();
        Constants.DEFAULT_PROTOCOL_PORT = Utils.getDefaultPort();
        database = new Database(this);
    }

    public Database getDatabase() {
        return database;
    }
}
