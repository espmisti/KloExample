package com.klo.example.domain.model

data class SystemModel(
    val carrier_name: String,
    val carrier_id: String,
    val carrier_country: String,
    val carrier_sim_name: String,
    val device_manufacturer: String,
    val device_model: String,
    val device_locale: String,
    val os_ver: String,
    val time_offset: String,
    val time_zone: String,
    val package_name: String,
    val app_ver: String,
    val app_id: String
)