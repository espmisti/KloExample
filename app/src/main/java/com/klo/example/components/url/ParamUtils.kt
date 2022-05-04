 package com.klo.example.components.url

import com.klo.example.components.url.URLConstants.ADVERTISING_ID
import com.klo.example.components.url.URLConstants.AF_ADSET_ID
import com.klo.example.components.url.URLConstants.AF_C_ID
import com.klo.example.components.url.URLConstants.APPSFLYER_ID
import com.klo.example.components.url.URLConstants.GEO
import com.klo.example.components.url.URLConstants.PACKAGE
import com.klo.example.components.url.URLConstants.SUB1
import com.klo.example.components.url.URLConstants.SUB2
import com.klo.example.components.url.URLConstants.SUB3
import com.klo.example.components.url.URLConstants.SUB4
import com.klo.example.components.url.URLConstants.SUB5
import com.klo.example.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ParamUtils {
    suspend fun replace_param(sub: HashMap<Int, String>, advertising_id: String, appsflyer_id: String, campaign_id: String, adset_id: String, sys_package: String, geo: String) : String = withContext(Dispatchers.IO){
        var url = generateUrl()
        with(url){
            if(contains(SUB1)) url = url.replace(SUB1, sub[0].toString())
            if(contains(SUB2)) url = url.replace(SUB2, sub[1].toString())
            if(contains(SUB3)) url = url.replace(SUB3, sub[2].toString())
            if(contains(SUB4)) url = url.replace(SUB4, sub[3].toString())
            if(contains(SUB5)) url = url.replace(SUB5, sub[4].toString())

            if(contains(ADVERTISING_ID)) url = url.replace(ADVERTISING_ID, advertising_id)
            if(contains(APPSFLYER_ID)) url = url.replace(APPSFLYER_ID, appsflyer_id)
            if(contains(AF_C_ID)) url = url.replace(AF_C_ID, campaign_id)
            if(contains(AF_ADSET_ID)) url = url.replace(AF_ADSET_ID, adset_id)

            if(contains(PACKAGE)) url = url.replace(PACKAGE, sys_package)
            if(contains(GEO)) url = url.replace(GEO, geo)
        }
        return@withContext url
    }
    private fun generateUrl() : String {
        return "${Constants.URL}?$SUB1&$SUB2&$SUB3&$SUB4&$SUB5&$ADVERTISING_ID&$APPSFLYER_ID&$AF_C_ID&$AF_ADSET_ID&$PACKAGE&$GEO"
    }
}