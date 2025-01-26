package com.example.krmobil.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesHelper {
    private const val PREFS_NAME = "user_prefs"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_LOGIN = "user_login"

    fun saveUserEmail(context: Context, email: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(KEY_USER_EMAIL, email)
        editor.apply()
    }

    fun getUserEmail(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_USER_EMAIL, null)
    }

    fun saveUserLogin(context: Context, login: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(KEY_USER_LOGIN, login)
        editor.apply()
    }

    fun getUserLogin(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_USER_LOGIN, null)
    }
}
