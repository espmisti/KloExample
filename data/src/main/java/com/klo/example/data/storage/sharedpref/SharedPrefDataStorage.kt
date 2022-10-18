package com.klo.example.data.storage.sharedpref

import android.content.Context
import com.klo.example.data.storage.SharedPrefStorage

class SharedPrefDataStorage(context: Context) : SharedPrefStorage {
    val sPrefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    //////////////////////////////////////////////////////////////
    override fun getLastURL(): String? {
        return sPrefs.getString("last_url", null)
    }

    override fun saveLastURL(value: String): Boolean {
        with(sPrefs.edit()) {
            putString("last_url", value)
            apply()
        }
        return true
    }
    //////////////////////////////////////////////////////////////
    override fun getFullScreen(): Int {
        return sPrefs.getInt("fullscreen", 0)
    }

    override fun saveFullScreen(value: Int): Boolean {
        with(sPrefs.edit()) {
            putInt("fullscreen", value)
            apply()
        }
        return true
    }
    //////////////////////////////////////////////////////////////
    override fun getOrientationScreen(): Int {
        return sPrefs.getInt("orientation", 0)
    }

    override fun saveOrientationScreen(value: Int): Boolean {
        with(sPrefs.edit()) {
            putInt("orientation", value)
            apply()
        }
        return true
    }
}