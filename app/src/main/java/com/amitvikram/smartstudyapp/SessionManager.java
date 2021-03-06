package com.amitvikram.smartstudyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String NAME = "USER_NAME";
    public static final String EMAIL = "USER_EMAIL";
    public static final String MOBILE = "USER_MOBILE";
    public static final String USERTYPE = "USER_TYPE";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String name, String email, String mobile, String usertype) {

        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putString(MOBILE, mobile);
        editor.putString(USERTYPE, usertype);
        editor.apply();

    }

    public boolean isLoggin() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin() {
        if (!this.isLoggin()) {
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            if (context instanceof MainActivity)
                ((MainActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail() {

        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(MOBILE, sharedPreferences.getString(MOBILE, null));
        user.put(USERTYPE, sharedPreferences.getString(USERTYPE, null));
        return user;
    }

    public void logout() {

        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
        ((MainActivity) context).finish();

    }

    public void update() {
        editor.clear();
        editor.commit();
    }


}