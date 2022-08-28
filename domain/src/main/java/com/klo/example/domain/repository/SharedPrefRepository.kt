package com.klo.example.domain.repository

interface SharedPrefRepository {
    suspend fun getLastURL() : String?
    suspend fun saveLastURL(value: String) : Boolean
    //
    suspend fun getFullScreen() : Int
    suspend fun saveFullScreen(value: Int) : Boolean
    //
    suspend fun getOrientationScreen() : Int
    suspend fun saveOrientationScreen(value: Int) : Boolean
}