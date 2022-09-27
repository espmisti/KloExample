package com.wisdomegypt.appqd.data.repository

import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import com.wisdomegypt.appqd.data.APIService
import com.wisdomegypt.appqd.data.Constants
import com.wisdomegypt.appqd.domain.repository.InstallLogRepository
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class InstallLogDataRepository(private val pkg: String, private val tm: TelephonyManager, private val join_type: String) : InstallLogRepository {
    override suspend fun sendInstalLog() {
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
        sdf.timeZone = TimeZone.getDefault()
        val currentDate = sdf.format(Date())
        val params = HashMap<String?, String?>()
        params["token"] = Constants.API.TOKEN
        params["pkg"] = pkg
        params["device"] = "${Build.MANUFACTURER} ${Build.MODEL}"
        params["join_type"] = join_type
        params["join_time"] = currentDate
        params["geo"] = tm.networkCountryIso

        val response = APIService.retrofit.sendInstallLog(params = params)
        if(!response.isSuccessful) {
            val result = response.code()
            Log.e("APP_CHECK", "[INSTALL LOG] NOT WORK! | $result")
        }
    }
}