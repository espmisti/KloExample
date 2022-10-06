package com.klo.example

import android.annotation.SuppressLint
import android.os.Build
import android.telephony.TelephonyManager
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class LogSystem {
    private var file: File? = null
    fun initial(tm: TelephonyManager) {
        file = File(getFileName(tm = tm))
    }
    fun sendFile() {}

    fun writeFile(text: String) {
        file?.writeText(text)
    }

    private fun getFileName(tm: TelephonyManager) : String {
        val d = SimpleDateFormat("HH:mm:ss dd.MM.yyyy", Locale.getDefault()).format(Date())
        return "${Build.MODEL}-${Build.MANUFACTURER} $d | ${tm.networkCountryIso}"
    }
}