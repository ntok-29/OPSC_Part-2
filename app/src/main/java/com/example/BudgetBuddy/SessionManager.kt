package com.example.BudgetBuddy


import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val pref: SharedPreferences
    private val editor: SharedPreferences.Editor
    private val PRIVATE_MODE = 0

    companion object {
        private const val PREF_NAME = "SmartBudgetPref"
        private const val IS_LOGIN = "IsLoggedIn"
        private const val KEY_EMAIL = "email"
        private const val KEY_NAME = "name"
    }

    init {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    fun createLoginSession(email: String, name: String) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_NAME, name)
        editor.commit()
    }

    fun getUserDetails(): HashMap<String, String?> {
        val user = HashMap<String, String?>()
        user[KEY_EMAIL] = pref.getString(KEY_EMAIL, null)
        user[KEY_NAME] = pref.getString(KEY_NAME, null)
        return user
    }

    fun logoutUser() {
        editor.clear()
        editor.commit()
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }
    fun saveDarkModeState(isEnabled: Boolean) {
        val editor = pref.edit()
        editor.putBoolean("dark_mode", isEnabled)
        editor.apply()
    }
    // In SessionManager.kt
    fun saveNotificationState(isEnabled: Boolean) {
        editor.putBoolean("notifications", isEnabled).apply()
    }

    fun areNotificationsEnabled(): Boolean {
        return pref.getBoolean("notifications", true)
    }

    fun isDarkModeEnabled(): Boolean {
        return pref.getBoolean("dark_mode", false)
    }

}
