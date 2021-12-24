package com.cikarastudio.cikarajantungdesafix.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.cikarastudio.cikarajantungdesafix.ui.login.LoginActivity;
import com.cikarastudio.cikarajantungdesafix.ui.main.MainActivity;

import java.util.HashMap;

public class SessionManager {

    public static final String ID = "ID";
    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public SharedPreferences.Editor editor;
    public Context context;
    SharedPreferences sharedPreferences;
    int PRIVATE_MODE = 0;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("LOGIN", PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String id) {

        editor.putBoolean(LOGIN, true);
        editor.putString(ID, id);
        editor.apply();
    }

    public boolean isLoggin() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin() {
        if (!this.isLoggin()) {
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((MainActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(ID, sharedPreferences.getString(ID, null));
        return user;
    }

    public void logout() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
    }

}
