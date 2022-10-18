package com.klo.example.data.repository

import android.os.Build
import android.telephony.TelephonyManager
import com.klo.example.domain.repository.FileDataRepository
import java.text.SimpleDateFormat
import java.util.*

class FileRepository() : FileDataRepository {
    override suspend fun getFileName(tm: TelephonyManager): String
        return "${Build.MODEL}-${Build.MANUFACTURER} $dateTime | ${tm.networkCountryIso}"
    }

    override suspend fun createFile(fileName: String): Boolean {
        TODO("Not yet implemented")
    }
}