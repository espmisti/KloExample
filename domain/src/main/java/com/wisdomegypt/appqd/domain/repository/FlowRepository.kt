package com.wisdomegypt.appqd.domain.repository

import com.wisdomegypt.appqd.domain.model.FlowModel

interface FlowRepository {
    suspend fun getData() : FlowModel?
}