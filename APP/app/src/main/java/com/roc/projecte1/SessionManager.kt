package com.roc.projecte1

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class SessionManager private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_TYPE = "user_type"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"

        @Volatile
        private var INSTANCE: SessionManager? = null

        fun getInstance(context: Context): SessionManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SessionManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    fun saveSession(user: Any, userType: Char) {
        editor.putString(KEY_USER_TYPE, userType.toString())
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString("user_data", Gson().toJson(user))
        editor.apply()
    }

    fun getUser(): Any? {
        val userType = sharedPreferences.getString(KEY_USER_TYPE, null)
        val userJson = sharedPreferences.getString("user_data", null)

        return if (userJson != null) {
            when (userType) {
                "a" -> Gson().fromJson(userJson, Alumne::class.java)
                "p" -> Gson().fromJson(userJson, Professor::class.java)
                else -> null
            }
        } else {
            null
        }
    }

    fun getUserType(): String? = sharedPreferences.getString(KEY_USER_TYPE, null)
    fun isLoggedIn(): Boolean = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)

    fun clearSession() {
        editor.clear()
        editor.apply()
    }
}
