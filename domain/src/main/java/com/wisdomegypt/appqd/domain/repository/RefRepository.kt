package com.wisdomegypt.appqd.domain.repository

interface RefRepository {
    suspend fun getData() : HashMap<String, String>?
}