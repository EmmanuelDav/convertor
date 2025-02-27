package com.cyberiyke.converter.data.remote

import com.cyberiyke.converter.data.remote.model.RatesResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ConverterApi {
    @GET("latest")
    suspend fun convertRate(
        @Query("access_key") access_key : String,
        @Query("symbol") symbol:String
    ): Response<RatesResult>
}