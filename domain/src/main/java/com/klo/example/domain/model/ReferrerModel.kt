package com.klo.example.domain.model

data class ReferrerModel (
    val installVersion : String?,
    val installReferrer : String?,
    val installBeginTimestampSeconds : String?,
    val referrerClickTimestampSeconds : String?,
    val googlePlayInstantParam : String?,
    val installBeginTimestampServerSeconds : String?,
    val referrerClickTimestampServerSeconds : String?
)