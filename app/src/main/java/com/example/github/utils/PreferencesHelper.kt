package com.example.github.utils

import android.content.Context.MODE_PRIVATE
import com.example.github.R
import com.example.github.base.GithubApplication.Companion.context
import com.example.github.utils.PreferencesHelper.Keys.KEY_ACCESS_TOKEN


object PreferencesHelper {

    private val sharedPreferences = context.getSharedPreferences(context.getString(R.string.package_name), MODE_PRIVATE)

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    fun putAccessToken(accessToken: String) {
        sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, accessToken).apply()
    }

    fun getAccessToken(): String {
        return sharedPreferences.getString(
            KEY_ACCESS_TOKEN,
            ""
        ) ?: ""
    }

    object Keys {
        const val KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN"
    }
}