package com.klo.example.domain.repository

interface SystemRepository {
    fun getData() : HashMap<String, String>
}