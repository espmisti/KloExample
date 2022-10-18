package com.klo.example.data.storage

interface SharedPrefStorage {
    fun getLastURL() : String?
    fun saveLastURL(value: String) : Boolean
    //
    fun getFullScreen() : Int
    fun saveFullScreen(value: Int) : Boolean
    //
    fun getOrientationScreen() : Int
    fun saveOrientationScreen(value: Int) : Boolean
}