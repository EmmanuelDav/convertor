package com.cyberiyke.converter.data.repository

import com.cyberiyke.converter.data.local.entity.RatesEntity
import com.cyberiyke.converter.data.remote.model.RatesResult
import com.cyberiyke.converter.utils.Resource

interface MainRepository {
    suspend fun convertRate(
        from:String,
        to:String,
    ): Resource<RatesResult>
}