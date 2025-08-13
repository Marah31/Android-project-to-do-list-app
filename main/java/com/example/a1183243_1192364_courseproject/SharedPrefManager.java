package com.example.a1183243_1192364_courseproject;

import android.content.Context;
import android.content.SharedPreferences;
public class SharedPrefManager {

    // Shared Preferences constants
    private static final String SHARED_PREF_NAME = "LoginPrefs";
    private static final int SHARED_PREF_PRIVATE = Context.MODE_PRIVATE;
    private static final String PREF_NAME = "app_prefs";
    private static final String KEY_LOGGED_IN = "logged_in";

    // Singleton instance and SharedPreferences reference
    private static SharedPrefManager ourInstance = null;
    private static SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor = null;
    private SharedPreferences prefs;  // This needs to be initialized
    private SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, SHARED_PREF_PRIVATE);
        prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE); // Initialize this line
        editor = sharedPreferences.edit();
    }


    // Get the Singleton instance of SharedPrefManager
    public static SharedPrefManager getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new SharedPrefManager(context);
        }
        return ourInstance;
    }

    // Save the email in SharedPreferences
    public boolean saveEmail(String email) {
        editor.putString("email", email);
        return editor.commit();
    }

    // Retrieve the saved email from SharedPreferences
    public String getSavedEmail() {
        return sharedPreferences.getString("email", null);
    }

    // Clear the saved email from SharedPreferences
    public boolean clearSavedEmail() {
        editor.remove("email");
        return editor.commit();
    }

    // Save the theme mode (Dark or Light)
    public void saveMode(String mode) {
        editor.putString("theme_mode", mode);
        editor.commit();
    }

    // Retrieve the theme mode (Dark or Light)
    public String getMode() {
        return sharedPreferences.getString("theme_mode", "light"); // Default is "light"
    }

    // Save the login status
    public void setLoggedIn(boolean loggedIn) {
        prefs.edit().putBoolean(KEY_LOGGED_IN, loggedIn).apply();
    }

    // Check if the user is logged in
    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_LOGGED_IN, false);
    }
}

