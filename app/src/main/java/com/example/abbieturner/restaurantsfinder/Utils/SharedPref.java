package com.example.abbieturner.restaurantsfinder.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private Context context;
    private static final String SHARED_PREF_KEY = "shared_key";
    private static final String APP_LANG = "app_lang";
    public static final String USER_TOKEN = "user_token";
    public static String USER_LOGIN = "user_login";

    public SharedPref(Context context) {
        this.context = context;
    }

    public void setLang(String lang) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(APP_LANG, lang);
        editor.apply();
    }

    public String getLang() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY, 0);
        return sharedPreferences.getString(APP_LANG, "en");
    }

    public void setToken(String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY, 0);
        return sharedPreferences.getString(USER_TOKEN, "");
    }

    public void setUserLogin(Boolean isLogged){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(USER_LOGIN, isLogged);
        editor.apply();
    }

    public Boolean isUserLogged(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY, 0);
        return sharedPreferences.getBoolean(USER_LOGIN, false);
    }
}