package com.dyukov.dyuandrolearn.utils

import android.content.Context
import android.content.SharedPreferences

const val PREFS_FILE_NAME = "prefs"

class PreferenceStorage constructor(private val context: Context) {
    companion object {
        private const val PREFS_DATA_LOADED = "Storage PREFS_DATA_LOADED requested"
    }

    fun getDataLoaded(): Boolean {
        val prefs = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(PREFS_DATA_LOADED, false)
    }

    fun setIsDataLoaded(b: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
        val prefsEdit = prefs.edit()

        prefsEdit.putBoolean(PREFS_DATA_LOADED, b)
        prefsEdit.apply()
    }

    fun clearIsDataLoaded() {
        val preferences = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE).edit()
            .remove(PREFS_DATA_LOADED).commit()
    }
}