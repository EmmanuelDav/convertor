package com.cyberiyke.convertor.data.remote

import com.cyberiyke.convertor.data.remote.model.RatesResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Endpoint to get the current currency rate my country symbol
 */

interface RateRequest {

    @GET("latest")
    suspend fun convertRate(
        @Query("access_key") access_key : String,
        @Query("symbol") symbol:String
    ): Response<RatesResult>
}