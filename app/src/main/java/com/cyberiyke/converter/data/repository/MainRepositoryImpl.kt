package com.cyberiyke.converter.data.repository

import com.cyberiyke.converter.data.remote.ConverterApi
import com.cyberiyke.converter.data.remote.model.ExchangeResponse
import com.cyberiyke.converter.utils.Resource
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val api: ConverterApi
): MainRepository {
    override suspend fun convertRate(
        from: String,
        to: String,
        amount: String,
    ): Resource<ExchangeResponse> {
        return try {
            val response = api.convertRate(from,to,amount)
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