package com.example.snapintuit.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.snapintuit.models.User

class UserPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFERENCES_NAME = "user_preferences"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME= "user_name"
        private const val KEY_USER_EMAIL= "user_email"
    }

    // Save user ID
    fun saveUserId(userId: Int) {
        with(sharedPreferences.edit()) {
            putInt(KEY_USER_ID, userId)
            apply() // or commit() if you need synchronous saving
        }
    }

    fun saveUser(userName: String, userEmail: String) {
        with(sharedPreferences.edit()) {
            putString(KEY_USER_NAME, userName)
            putString(KEY_USER_EMAIL, userEmail)
            apply() // or commit() if you need synchronous saving
        }
    }

    fun getUserName(): String? {
        return sharedPreferences.getString(KEY_USER_NAME, null)
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString(KEY_USER_EMAIL, null)
    }

    // Retrieve user ID
    fun getUserId(): Int {
        return sharedPreferences.getInt(KEY_USER_ID, -1) // -1 is default value if no user ID is found
    }

    // Clear all preferences
    fun clear() {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }
}
