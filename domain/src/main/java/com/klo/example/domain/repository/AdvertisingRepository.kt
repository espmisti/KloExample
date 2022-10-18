package com.klo.example.domain.repository

import com.klo.example.domain.model.AdvertData

interface AdvertisingRepository {
    fun getData() : AdvertData
}