package com.klo.example.log.text

import android.os.Build
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import java.io.File
import java.util.*

class HeaderText {
    @RequiresApi(Build.VERSION_CODES.P)
    fun write(file: File, tm: TelephonyManager) {
        file.appendText("==========================================")
        file.appendText("")
        file.appendText("")
        file.appendText("${Build.MANUFACTURER} ${Build.MODEL} (GEO: ${Locale.getDefault().language})")
        file.appendText("OS VERSION: ${Build.VERSION.RELEASE}")
        file.appendText("BUILD HOST: ${Build.HOST} ")
        file.appendText("")
        file.appendText("---------- SIM INFO ----------")
        file.appendText("SIM OPERATOR NAME: ${tm.simOperatorName}")
        file.appendText("SIM OPERATOR: ${tm.simOperator}")
        file.appendText("SIM CARRIER ID: ${tm.simCarrierId}")
        file.appendText("SIM STATE: ${tm.simState}")
        file.appendText("SIM CARRIER ID NAME: ${tm.simCarrierIdName}")
        file.appendText("SIM COUNTRY ISO: ${tm.simCountryIso}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) file.appendText("SIM SPECIFIC CARRIER ID: ${tm.simSpecificCarrierId}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) file.appendText("SIM SPECIFIC CARRIER ID NAME: ${tm.simSpecificCarrierIdName}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) file.appendText("SIM CARRIER ID FROM SIM MCC MNC: ${tm.carrierIdFromSimMccMnc}")
        file.appendText("------------------------------")


    }
}