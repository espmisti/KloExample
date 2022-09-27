package com.klo.example.domain.repository

interface InternetRepository {
    fun getConnection() : Boolean
}