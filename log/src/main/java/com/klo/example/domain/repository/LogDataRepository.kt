package com.klo.example.domain.repository

interface LogDataRepository {
    fun getData() : HashMap<String, String>
}