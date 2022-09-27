package com.wisdomegypt.appqd.domain.repository

import com.wisdomegypt.appqd.domain.model.AppDataModel

interface AppRepository {
    suspend fun getData() : AppDataModel?
}