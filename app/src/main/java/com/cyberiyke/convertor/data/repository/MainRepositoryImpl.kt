package com.cyberiyke.convertor.data.repository

import com.cyberiyke.convertor.data.remote.RateRequest
import com.cyberiyke.convertor.data.remote.model.RatesResult
import com.cyberiyke.convertor.utils.Resource
import com.cyberiyke.convertor.BuildConfig
import javax.inject.Inject

/**
 *  since all the endpoint are paid
 *  we will be using the free version to get the currency of two countries that the user would choose
 *  then we will convert it ourselves manually
 */

class MainRepositoryImpl @Inject constructor(
    private val api: RateRequest
): MainRepository {
    override suspend fun convertRate(
        from: String,
        to: String,
    ): Resource<RatesResult> {
        return try {

            val currencies = listOf(from, to )
            val result = currencies.joinToString(",")

            val response = api.convertRate(BuildConfig.API_KEY, result)
            if (response.isSuccessful && response.body() != null){
                Resource.Success(response.body())
            }else{
                Resource.Error(response.message())
            }
        }catch (e:Exception){
            Resource.Error(e.message)
        }
    }
}

