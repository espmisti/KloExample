package com.wisdomegypt.appqd.data

import com.wisdomegypt.appqd.domain.model.AppDataModel
import com.wisdomegypt.appqd.domain.model.FlowModel
import com.wisdomegypt.appqd.domain.model.OrganicModel
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface APIService {

    @GET(Constants.API.ROUTES.APP_INFO)
    suspend fun getAppInfo(
        @Query("token") token : String = Constants.API.TOKEN,
        @Query("package") pkg : String
    ) : Response<AppDataModel>

    @POST(Constants.API.ROUTES.URL_OFFER)
    suspend fun getFlow(
        @Body requestBody: RequestBody
    ) : Response<FlowModel>

    @FormUrlEncoded
    @POST(Constants.API.ROUTES.INSTALL_LOG)
    suspend fun sendInstallLog(
        @FieldMap params: HashMap<String?, String?>
    ) : Response<ResponseBody>

    @GET(Constants.API.ROUTES.ORGANIC)
    suspend fun getOrganic(
        @Query("token") token : String = Constants.API.TOKEN,
        @Query("package") pkg : String,
        @Query("geo") geo : String
    ) : Response<OrganicModel>

    companion object {
        val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.API.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIService::class.java)
        }
    }
}