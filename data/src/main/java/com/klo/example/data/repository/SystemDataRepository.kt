package com.klo.example.data.repository

import android.os.Build
import android.telephony.TelephonyManager
import com.klo.example.data.BuildConfig
import com.klo.example.data.Constants
import com.klo.example.domain.model.SystemModel
import com.klo.example.domain.repository.SystemRepository
import java.sql.Time
import java.util.*

class SystemDataRepository(private val tm: TelephonyManager, private val pkg: String) : SystemRepository{
    override suspend fun getData(): SystemModel {
        return SystemModel(
            carrier_name = tm.simOperatorName,
            carrier_id = tm.networkOperator,
            carrier_country = tm.networkCountryIso,
            carrier_sim_name = tm.simOperatorName,
            device_manufacturer = Build.MANUFACTURER,
            device_model = Build.MODEL,
            device_locale = Locale.getDefault().language,
            os_ver = Build.VERSION.RELEASE,
            time_offset = TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT),
            time_zone = TimeZone.getDefault().id,
            package_name = pkg,
            app_ver = "error",
            app_id = Constants.SYSTEM.APP_ID
        )
    }
}