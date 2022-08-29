package com.klo.example.domain.usecase

import com.klo.example.domain.model.SystemModel
import com.klo.example.domain.repository.SystemRepository
import java.util.*


class GetSystemInfoUseCase(private val systemRepository: SystemRepository){
    suspend fun execute() : SystemModel{
        val data = systemRepository.getData()
        return SystemModel(
            carrier_name = data.carrier_name,
            carrier_id = data.carrier_id,
            carrier_country = data.carrier_country,
            carrier_sim_name = data.carrier_sim_name,
            device_manufacturer = data.device_manufacturer,
            device_model = data.device_model,
            device_locale = data.device_locale,
            os_ver = data.os_ver,
            time_offset = data.time_offset,
            time_zone = data.time_zone,
            package_name = data.package_name,
            app_ver = data.app_ver,
            app_id = data.app_id
        )
    }
}