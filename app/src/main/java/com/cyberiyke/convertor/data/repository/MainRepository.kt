package com.cyberiyke.convertor.data.repository

import com.cyberiyke.convertor.data.local.entity.RatesEntity
import com.cyberiyke.convertor.data.remote.model.RatesResult
import com.cyberiyke.convertor.utils.Resource

interface MainRepository {
    suspend fun convertRate(
        from:String,
        to:String,
    ): Resource<RatesResult>
}