package com.wisdomegypt.appqd.domain.usecase

import com.wisdomegypt.appqd.domain.repository.SystemRepository


class GetSystemInfoUseCase(private val systemRepository: SystemRepository){
    fun execute() = systemRepository.getData()
}