package com.intkhabahmed.quicknote.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.intkhabahmed.quicknote.database.AppDatabase;

public class Global extends Application {

    private static Global sGlobalInstance;

    public static Global getInstance() {
        return sGlobalInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sGlobalInstance = (Global) getApplicationContext();
    }

    public static AppDatabase getDbInstance() {
        return AppDatabase.getInstance(sGlobalInstance);
    }
}
