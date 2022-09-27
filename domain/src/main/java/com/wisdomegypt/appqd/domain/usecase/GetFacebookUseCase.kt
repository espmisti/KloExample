package com.wisdomegypt.appqd.domain.usecase

import com.wisdomegypt.appqd.domain.repository.FacebookRepository

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