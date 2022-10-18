package com.klo.example.data.repository

import android.util.Log
import com.klo.example.data.storage.FacebookStorage
import com.klo.example.domain.repository.FacebookRepository

class FacebookRepositoryImpl (private val facebookStorage: FacebookStorage) : FacebookRepository {
    override suspend fun getData(): String? {
        return facebookStorage.getData()
    }
    override fun getStatusFacebook(id: String, token: String): Boolean {
        return facebookStorage.getStatus(id = id, token = token)
    }
}