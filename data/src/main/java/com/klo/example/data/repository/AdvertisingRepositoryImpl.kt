package com.klo.example.data.repository

import com.klo.example.data.storage.AdvertisingStorage
import com.klo.example.domain.repository.AdvertisingRepository

class AdvertisingRepositoryImpl(private val advertisingStorage: AdvertisingStorage) : AdvertisingRepository {

    override fun getADID(): String {
        return advertisingStorage.getADID()
    }

    override suspend fun getAFID(): String {
        return advertisingStorage.getAFID()
    }

}