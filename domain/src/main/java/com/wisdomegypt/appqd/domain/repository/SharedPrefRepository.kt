package com.wisdomegypt.appqd.domain.repository

interface SharedPrefRepository {
    fun getLastURL() : String?
    fun saveLastURL(value: String) : Boolean
    //
    fun getFullScreen() : Int
    fun saveFullScreen(value: Int) : Boolean
    //
    fun getOrientationScreen() : Int
    fun saveOrientationScreen(value: Int) : Boolean
}