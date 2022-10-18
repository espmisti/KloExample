package com.klo.example.data

import com.klo.example.domain.model.AppDataModel
import com.klo.example.domain.model.FlowModel
import com.klo.example.domain.model.OrganicModel
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

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
        val okHttpClient by lazy {
            OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()
        }
        val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.API.URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIService::class.java)
        }
    }
}