package com.klo.example.domain.usecase

import com.klo.example.domain.repository.FacebookRepository

class GetFacebookUseCase (private val facebookRepository: FacebookRepository) {
    suspend fun execute() : ArrayList<String>? {
        val data = facebookRepository.getCampaign()
        return if(data != null) {
            val array = arrayListOf<String>()
            array.add(data.substringBefore('_'))
            array.add(data)
            array
        } else null
    }
}