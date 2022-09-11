package com.klo.example.domain.usecase

import com.klo.example.domain.model.AppsflyerModel
import com.klo.example.domain.repository.AppsflyerRepository

class GetAppsflyerUseCase (private val appsflyerRepository: AppsflyerRepository) {
    suspend fun execute() : AppsflyerModel? {
        val data = appsflyerRepository.getData()
        if (data != null) {
            return AppsflyerModel(
                campaign = data.campaign,
                advertising_id = data.advertising_id,
                appsflyer_id = data.appsflyer_id,

                adgroup = data.adgroup,
                adgroup_id = data.adgroup_id,
                adset = data.adset,
                adset_id = data.adset_id,
                af_cpi = data.af_cpi,
                af_r = data.af_r,
                af_click_lookback = data.af_click_lookback,
                af_siteid = data.af_siteid,
                af_status = data.af_status,
                af_sub1 = data.af_sub1,
                af_sub2 = data.af_sub2,
                af_sub3 = data.af_sub3,
                af_sub4 = data.af_sub4,
                install_time = data.install_time,
                is_branded_link = data.is_branded_link,
                is_first_launch = data.is_first_launch,
                is_incentivized = data.is_incentivized,
                is_retargeting = data.is_retargeting,
                iscache = data.iscache,
                is_universal_link = data.is_universal_link,
                campaign_id = data.campaign_id,
                click_time = data.click_time,
                clickid = data.clickid,
                cost_cents_USD = data.cost_cents_USD,
                orig_cost = data.orig_cost,
                engmnt_source = data.engmnt_source,
                http_referrer = data.http_referrer,
                media_source = data.media_source,
                redirect_response_data = data.redirect_response_data,
                retargeting_conversion_type = data.retargeting_conversion_type,
                match_type = data.match_type,
                ts = data.ts
            )
        } else return null
    }
}