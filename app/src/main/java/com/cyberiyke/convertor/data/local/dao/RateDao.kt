package com.cyberiyke.convertor.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cyberiyke.convertor.data.local.entity.RatesEntity

@Dao
interface RatesDao {

    @Query("SELECT * FROM exchange_rates WHERE base = :from LIMIT 1")
    suspend fun getRates(from: String): RatesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: RatesEntity)

    @Query("DELETE FROM exchange_rates")
    suspend fun clearRates()
}




