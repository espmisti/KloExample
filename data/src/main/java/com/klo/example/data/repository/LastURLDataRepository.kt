package com.klo.example.data.repository

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.klo.example.domain.model.LastURLModel
import com.klo.example.domain.repository.LastURLRepository

class LastURLDataRepository(private val context: Context) : LastURLRepository {
    val masterKey = MasterKey.Builder(context.applicationContext).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
    val sPrefs = EncryptedSharedPreferences.create(context.applicationContext, "setting", masterKey, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

    override suspend fun getData(): LastURLModel {
        return LastURLModel(last_url = sPrefs.getString("last_url", null))
    }

    override suspend fun saveData(url: String?): Boolean {
        with(sPrefs.edit()) {
            putString("last_url", url)
            apply()
        }
        return true
    }
}