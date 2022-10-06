package com.klo.example.data.repository

import android.os.Build
import com.klo.example.domain.repository.LogDataRepository
import java.util.*

class SysDataRepository(private val ) : LogDataRepository {
    override fun getData() {
        val map = hashMapOf<String, String>()
        map["carrier_name"] = tm.simOperatorName
        map["carrier_id"] = tm.networkOperator
        map["carrier_country"] = tm.networkCountryIso
        map["carrier_sim_name"] = tm.simOperatorName
        map["device_manufacturer"] = Build.MANUFACTURER
        map["device_model"] = Build.MODEL
        map["device_locale"] = Locale.getDefault().language
        map["os_ver"] = Build.VERSION.RELEASE
        map["time_offset"] = TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)
        map["time_zone"] = TimeZone.getDefault().id
        map["package_name"] = pkg
        map["app_ver"] = "error"
        map["app_id"] = Constants.SYSTEM.APP_ID
        return map
    }
}