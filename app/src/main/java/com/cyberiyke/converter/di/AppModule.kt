package com.cyberiyke.converter.di

import com.cyberiyke.converter.data.remote.ConverterApi
import com.cyberiyke.converter.data.repository.MainRepository
import com.cyberiyke.converter.data.repository.MainRepositoryImpl
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
    fun getConverterApi(): ConverterApi {
        return Retrofit
            .Builder()
            .baseUrl("http://data.fixer.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ConverterApi::class.java)
    }

    @Singleton
    @Provides
    fun getMainRepository(api: ConverterApi): MainRepository = MainRepositoryImpl(api)
}