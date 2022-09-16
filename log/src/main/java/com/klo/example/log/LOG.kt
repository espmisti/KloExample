package com.klo.example.log

import android.app.Application
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import com.klo.example.log.text.HeaderText
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class LOG(private val context: Context) {
    private val sPrefs by lazy { context.getSharedPreferences("setting", Context.MODE_PRIVATE) }

    fun createFile() : File{
        val tm : TelephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val path = context.filesDir
        val letDirectory = File(path, "LET")
        letDirectory.mkdirs()
        val file = File(letDirectory, getFileName())
        HeaderText().write(file = file, tm = tm)
        return file
    }
    private fun getFileName() : String {
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
        val currentDate = sdf.format(Date())
        return "${Build.MANUFACTURER}|${Build.MODEL}|$currentDate"
    }
    fun getFile(file: File) {
        val text = FileInputStream(file).bufferedReader().use { it.readText() }
        Log.i("APP_CHECK", "$text")
    }
}