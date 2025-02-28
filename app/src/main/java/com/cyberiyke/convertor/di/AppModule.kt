package com.cyberiyke.convertor.di

import com.cyberiyke.convertor.data.remote.RateRequest
import com.cyberiyke.convertor.data.repository.MainRepository
import com.cyberiyke.convertor.data.repository.MainRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getConverterApi(): RateRequest {
        return Retrofit
            .Builder()
            .baseUrl("http://data.fixer.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RateRequest::class.java)
    }

    @Singleton
    @Provides
    fun getMainRepository(api: RateRequest): MainRepository = MainRepositoryImpl(api)
}