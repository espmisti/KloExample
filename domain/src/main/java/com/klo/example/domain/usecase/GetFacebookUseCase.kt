package com.klo.example.domain.usecase

import com.klo.example.domain.model.FacebookModel
import com.klo.example.domain.repository.FacebookRepository

class GetFacebookUseCase (private val facebookRepository: FacebookRepository) {
    suspend fun execute() : FacebookModel? {
        val data = facebookRepository.getCampaign()
        if (data != null) {
           return FacebookModel(
               campaign = data.campaign
           )
        } else return null
    }
}