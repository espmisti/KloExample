package com.klo.example.data.repository

import com.klo.example.data.storage.SharedPrefStorage
import com.klo.example.domain.repository.SharedPrefRepository

class SharedPrefDataRepositoryImpl(private val sharedPrefStorage: SharedPrefStorage) : SharedPrefRepository {
    override fun getLastURL(): String? {
        return sharedPrefStorage.getLastURL()
    }
    override fun saveLastURL(value: String): Boolean {
        return sharedPrefStorage.saveLastURL(value = value)
    }
    override fun getFullScreen(): Int {
        return sharedPrefStorage.getFullScreen()
    }
    override fun saveFullScreen(value: Int): Boolean {
        return sharedPrefStorage.saveFullScreen(value = value)
    }
    override fun getOrientationScreen(): Int {
        return sharedPrefStorage.getOrientationScreen()
    }
    override fun saveOrientationScreen(value: Int): Boolean {
        return sharedPrefStorage.saveOrientationScreen(value = value)
    }
}