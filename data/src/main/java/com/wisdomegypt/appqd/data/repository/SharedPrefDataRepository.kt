package com.wisdomegypt.appqd.data.repository

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.wisdomegypt.appqd.domain.repository.SharedPrefRepository

class SharedPrefDataRepository(private val context: Context) : SharedPrefRepository {
    val masterKey = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
    val sPrefs = EncryptedSharedPreferences.create(context, "setting", masterKey, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

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