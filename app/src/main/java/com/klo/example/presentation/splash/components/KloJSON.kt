package com.klo.example.presentation.splash.components

import com.klo.example.domain.model.AppsflyerModel
import com.klo.example.domain.model.FacebookModel
import com.klo.example.domain.model.ReferrerModel
import com.klo.example.domain.model.SystemModel
import org.json.JSONObject

class KloJSON {
    fun getAppsflyer(jsonObject: JSONObject, model: AppsflyerModel) = with(jsonObject){
        put("redirect_response_data", model.redirect_response_data)
        put("adgroup_id", model.adgroup_id)
        put("engmnt_source", model.engmnt_source)
        put("retargeting_conversion_type", model.retargeting_conversion_type)
        put("is_incentivized", model.is_incentivized)
        put("orig_cost", model.orig_cost)
        put("is_first_launch", model.is_first_launch)
        put("af_click_lookback", model.af_click_lookback)
        put("af_cpi", model.af_cpi)
        put("iscache", model.iscache)
        put("click_time", model.click_time)
        put("is_branded_link", model.is_branded_link)
        put("match_type", model.match_type)
        put("adset", model.adset)
        put("campaign_id", model.campaign_id)
        put("install_time", model.install_time)
        put("media_source", model.media_source)
        put("af_sub1", model.af_sub1)
        put("clickid", model.clickid)
        put("af_siteid", model.af_siteid)
        put("af_status", model.af_status)
        put("cost_cents_USD", model.cost_cents_USD)
        put("af_r", model.af_r)
        put("af_sub4", model.af_sub4)
        put("af_sub3", model.af_sub3)
        put("af_sub2", model.af_sub2)
        put("adset_id", model.adset_id)
        put("http_referrer", model.http_referrer)
        put("is_universal_link", model.is_universal_link)
        put("is_retargeting", model.is_retargeting)
        put("adgroup", model.adgroup)
        put("ts", model.ts)
    }
    fun getFacebook(jsonObject: JSONObject, model: FacebookModel) = with(jsonObject) {

    }
    fun getRefferer(jsonObject: JSONObject, model: ReferrerModel) = with(jsonObject) {
        put("installVersion", model.installVersion)
        put("installReferrer", model.installReferrer)
        put("installBeginTimestampSeconds", model.installBeginTimestampSeconds)
        put("referrerClickTimestampSeconds", model.referrerClickTimestampSeconds)
        put("googlePlayInstantParam", model.googlePlayInstantParam)
        put("installBeginTimestampServerSeconds", model.installBeginTimestampServerSeconds)
        put("referrerClickTimestampServerSeconds", model.referrerClickTimestampServerSeconds)
    }
    fun getSystem(jsonObject: JSONObject, model: SystemModel) = with(jsonObject) {
        put("carrier_name", model.carrier_name)
        put("carrier_id", model.carrier_id)
        put("carrier_country", model.carrier_country)
        put("carrier_sim_name", model.carrier_sim_name)
        put("device_manufacturer", model.device_manufacturer)
        put("device_model", model.device_model)
        put("device_locale", model.device_locale)
        put("os_ver", model.os_ver)
        put("time_offset", model.time_offset)
        put("time_zone", model.time_zone)
        put("package_name", model.package_name)
        put("app_ver", model.app_ver)
        put("app_id", model.app_id)
    }
}