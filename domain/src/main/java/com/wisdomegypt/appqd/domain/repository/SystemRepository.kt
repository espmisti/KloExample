package com.wisdomegypt.appqd.domain.repository

interface SystemRepository {
    fun getData() : HashMap<String, String>
}