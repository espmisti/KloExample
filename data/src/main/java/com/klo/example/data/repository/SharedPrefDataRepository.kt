package com.klo.example.data.repository

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.klo.example.domain.model.SharedPrefModel
import com.klo.example.domain.repository.SharedPrefRepository

class SharedPrefDataRepository(private val context: Context) : SharedPrefRepository {
    val masterKey = MasterKey.Builder(context.applicationContext).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
    val sPrefs = EncryptedSharedPreferences.create(context.applicationContext, "setting", masterKey, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

    //////////////////////////////////////////////////////////////
    override suspend fun getLastURL(): String? {
        return sPrefs.getString("last_url", null)
    }

    override suspend fun saveLastURL(value: String): Boolean {
        with(sPrefs.edit()) {
            putString("last_url", value)
            apply()
        }
        return true
    }
    //////////////////////////////////////////////////////////////
    override suspend fun getFullScreen(): Int {
        return sPrefs.getInt("fullscreen", 0)
    }

    override suspend fun saveFullScreen(value: Int): Boolean {
        with(sPrefs.edit()) {
            putInt("fullscreen", value)
            apply()
        }
        return true
    }
    //////////////////////////////////////////////////////////////
    override suspend fun getOrientationScreen(): Int {
        return sPrefs.getInt("orientation", 0)
    }

    override suspend fun saveOrientationScreen(value: Int): Boolean {
        with(sPrefs.edit()) {
            putInt("orientation", value)
            apply()
        }
        return true
    }
}