package com.klo.example.domain.repository

import com.klo.example.domain.model.LastURLModel

interface LastURLRepository {
    suspend fun getData() : LastURLModel
    suspend fun saveData(url: String?) : Boolean
}