package com.klo.example.domain.usecase

import com.klo.example.domain.repository.SystemRepository


class GetSystemInfoUseCase(private val systemRepository: SystemRepository){
    fun execute() = systemRepository.getData()
}