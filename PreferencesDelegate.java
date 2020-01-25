package com.dealsheel.thememodetest;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesDelegate {
    SharedPreferences sp;
    Context context;
    public static final String NIGHT_MODE="NIGHT_MODE";

    public PreferencesDelegate(Context context) {
        this.context = context;
        sp=context.getSharedPreferences("sp", Context.MODE_PRIVATE);
    }

    public void setBoolean(String key, boolean value){
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean value){
        return sp.getBoolean(key, value);
    }
}
