package com.dealsheel.thememodetest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class BaseActivity extends AppCompatActivity {
    PreferencesDelegate preferencesDelegate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        preferencesDelegate=new PreferencesDelegate(this);
        if (preferencesDelegate.getBoolean(PreferencesDelegate.NIGHT_MODE, false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.Dark);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.AppTheme);
        }


        if (AppCompatDelegate.getDefaultNightMode()== AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Dark);
        }else{
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
    }
}
