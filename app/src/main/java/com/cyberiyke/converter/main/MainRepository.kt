package com.cyberiyke.converter.main

import com.cyberiyke.converter.data.model.ExchangeResponse
import com.cyberiyke.converter.utils.Resource

interface MainRepository {
    suspend fun convertRate(
        from:String,
        to:String,
        amount:String
    ): Resource<ExchangeResponse>
}