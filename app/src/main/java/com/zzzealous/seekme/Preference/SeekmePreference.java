package com.zzzealous.seekme.Preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.zzzealous.seekme.MyApplication;

public class SeekmePreference {
    private static String FILENAME = "loginToken";

    public static void save(String key, String value) {
        SharedPreferences.Editor editor = MyApplication.getInstance().getSharedPreferences(FILENAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void save(String key, int value) {
        SharedPreferences.Editor editor = MyApplication.getInstance().getSharedPreferences(FILENAME, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void save(String key, boolean value) {
        SharedPreferences.Editor editor = MyApplication.getInstance().getSharedPreferences(FILENAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static String getString(String key) {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static int getInt(String key) {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }

    public static boolean getBoolean(String key) {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

}
