package com.wisdomegypt.appqd.presentation.splash.common

import org.json.JSONObject

class KloJSON {
    fun getAppsflyer(jsonObject: JSONObject, model: MutableMap<String, Any>) = with(jsonObject){
        for ((name, value) in model) {
            put(name, value)
        }
    }
    fun getFacebook(jsonObject: JSONObject, campaign: String) = with(jsonObject) {
        put("campaign", campaign)
    }
    fun getRefferer(jsonObject: JSONObject, model: HashMap<String, String>) = with(jsonObject) {
        put("campaign", model["installReferrer"].toString().substringAfter("&c="))
        for((name, value) in model) {
            put(name, value)
        }
    }
    fun getSystem(jsonObject: JSONObject, model: HashMap<String, String>) = with(jsonObject) {
        for((name, value) in model) {
            put(name, value)
        }
    }
}