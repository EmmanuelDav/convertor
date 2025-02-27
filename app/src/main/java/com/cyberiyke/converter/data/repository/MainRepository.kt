package com.cyberiyke.converter.data.repository

import com.cyberiyke.converter.data.remote.model.ExchangeResponse
import com.cyberiyke.converter.utils.Resource

interface MainRepository {
    suspend fun convertRate(
        from:String,
        to:String,
        amount:String
    ): Resource<ExchangeResponse>
}