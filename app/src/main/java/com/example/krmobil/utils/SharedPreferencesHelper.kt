package com.example.krmobil.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesHelper {
    private const val PREFS_NAME = "user_prefs"
    private const val EMAIL_KEY = "user_email"

    fun saveUserEmail(context: Context, email: String) {
        val sharedPrefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putString(EMAIL_KEY, email)
        editor.apply()
    }

    fun getUserEmail(context: Context): String? {
        val sharedPrefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPrefs.getString(EMAIL_KEY, null)
    }
}
